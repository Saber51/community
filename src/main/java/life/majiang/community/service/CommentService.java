package life.majiang.community.service;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.LikeCreateDTO;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.enums.LikeStatusEnum;
import life.majiang.community.enums.NotificationStatusEnum;
import life.majiang.community.enums.NotificationTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.*;
import life.majiang.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/24
 * @Description:life.majiang.community.service
 * @version:1.0
 */
@Transactional
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private LikedMapper likedMapper;

    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw  new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw  new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw  new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            //回复问题
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);

            //增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);

            //创建通知
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
        }else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            comment.setCommentCount(0);
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);

            //创建通知
            createNotify(comment,question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
    }

    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        if (receiver == comment.getCommentator()) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type, Long userId) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }

        //获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        //获取评论人并转换为 Map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换 comment 为 commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            if (userId!=null) {
                LikedExample likedExample = new LikedExample();
                likedExample.createCriteria().andLikeCommentEqualTo(comment.getId()).andLikeCreatorEqualTo(userId);
                List<Liked> liked = likedMapper.selectByExample(likedExample);
                if (liked.size() != 0) {
                    commentDTO.setLikeStatus(liked.get(0).getStatus());
                } else {
                    commentDTO.setLikeStatus((short) 0);
                }
            }else {
                commentDTO.setLikeStatus((short) 0);
            }
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return  commentDTOS;
    }

    public ResultDTO like(LikeCreateDTO likeCreateDTO, User user) {
        CommentDTO commentDTO = new CommentDTO();
        Comment comment = commentMapper.selectByPrimaryKey(likeCreateDTO.getCommentId());
        Comment temp = new Comment();
        temp.setId(comment.getId());
        temp.setLikeCount(1L);
        if (user.getId()!=comment.getCommentator()) {
            LikedExample example = new LikedExample();
            example.createCriteria()
                    .andLikeCreatorEqualTo(user.getId())
                    .andLikeCommentEqualTo(likeCreateDTO.getCommentId());
            List<Liked> dbliked = likedMapper.selectByExample(example);
            Liked liked = new Liked();
            if (dbliked.size()== 0) {
                liked.setLikeComment(likeCreateDTO.getCommentId());
                liked.setLikeCreator(user.getId());
                liked.setGmtCreate(System.currentTimeMillis());
                addLike(temp, liked, comment, commentDTO);
                likedMapper.insert(liked);
            } else {
                liked.setId(dbliked.get(0).getId());
                if (likeCreateDTO.getStatus() == 0) {
                    addLike(temp, liked, comment, commentDTO);
                } else {
                    liked.setGmtModified(System.currentTimeMillis());
                    liked.setStatus(LikeStatusEnum.CANCELED.getStatus());
                    commentExtMapper.cutLikeCount(temp);
                    comment.setLikeCount(comment.getLikeCount()-1);
                    BeanUtils.copyProperties(comment, commentDTO);
                    commentDTO.setLikeStatus((short)0);
                }
                likedMapper.updateByPrimaryKeySelective(liked);
            }
            commentDTO.setUser(user);
            return ResultDTO.okOf(commentDTO);
        }else {
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LIKE_YOURSELF);
        }
    }

    private void addLike(Comment temp, Liked liked,Comment comment, CommentDTO commentDTO) {
        liked.setGmtModified(System.currentTimeMillis());
        liked.setStatus(LikeStatusEnum.LIKED.getStatus());
        commentExtMapper.incLikeCount(temp);
        comment.setLikeCount(comment.getLikeCount()+1);
        BeanUtils.copyProperties(comment, commentDTO);
        commentDTO.setLikeStatus((short)1);
    }
}

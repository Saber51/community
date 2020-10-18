package life.majiang.community.controller;

import life.majiang.community.dto.CommentCreateDTO;
import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.LikeCreateDTO;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.enums.LikeStatusEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.model.Comment;
import life.majiang.community.model.Liked;
import life.majiang.community.model.User;
import life.majiang.community.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/24
 * @Description:life.majiang.community.controller
 * @version:1.0
 */
@Controller
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment, user);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public Object like(@RequestBody LikeCreateDTO likeCreateDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (likeCreateDTO == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LIKE_COMMENT);
        }
        return commentService.like(likeCreateDTO, user);
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public  ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id,
                                                 HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        Long userId = null;
        if (user!=null){
            userId = user.getId();
        }
        List<CommentDTO> commentDtos = commentService.listByTargetId(id, CommentTypeEnum.COMMENT, userId);
        return  ResultDTO.okOf(commentDtos);
    }

}

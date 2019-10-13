package life.majiang.community.dto;

import life.majiang.community.model.User;
import lombok.Data;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/26
 * @Description:life.majiang.community.dto
 * @version:1.0
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Integer commentCount;
    private User user;
}

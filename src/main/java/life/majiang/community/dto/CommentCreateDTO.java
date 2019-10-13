package life.majiang.community.dto;

import lombok.Data;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/24
 * @Description:life.majiang.community.dto
 * @version:1.0
 */
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}

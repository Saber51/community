package life.majiang.community.dto;

import life.majiang.community.model.User;
import lombok.Data;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/21
 * @Description:life.majiang.community.dto
 * @version:1.0
 */
@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
    private String description;
    private User user;
}

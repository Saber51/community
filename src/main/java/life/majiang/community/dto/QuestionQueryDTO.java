package life.majiang.community.dto;

import lombok.Data;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/10/15
 * @Description:life.majiang.community.dto
 * @version:1.0
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private String sort;
    private Long time;
    private String tag;
    private Integer page;
    private Integer size;
}

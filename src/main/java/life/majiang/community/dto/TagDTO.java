package life.majiang.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/28
 * @Description:life.majiang.community.dto
 * @version:1.0
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}

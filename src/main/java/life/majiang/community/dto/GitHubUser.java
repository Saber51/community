package life.majiang.community.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/19
 * @Description:life.majiang.community.dto
 * @version:1.0
 */
@Data
public class GitHubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;
}

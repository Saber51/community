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
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}

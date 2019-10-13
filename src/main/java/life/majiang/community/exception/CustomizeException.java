package life.majiang.community.exception;

import lombok.Getter;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/24
 * @Description:life.majiang.community.exception
 * @version:1.0
 */
public class CustomizeException extends RuntimeException{

    @Getter
    private String message;
    @Getter
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

}

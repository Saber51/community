package life.majiang.community.enums;

import lombok.Getter;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/28
 * @Description:life.majiang.community.enums
 * @version:1.0
 */
public enum NotificationStatusEnum {
    UNREAD(0),READ(1);

    @Getter
    private int status;

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}

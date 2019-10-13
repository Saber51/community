package life.majiang.community.enums;

import lombok.Getter;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/28
 * @Description:life.majiang.community.enums
 * @version:1.0
 */
public enum NotificationTypeEnum {
    REPLY_QUESTION(1,"回复了问题"),
    REPLY_COMMENT(2,"回复了评论")
    ;

    @Getter
    private int type;
    @Getter
    private String name;

    NotificationTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String nameOfType(int type){
        for (NotificationTypeEnum notificationTypeEnum: NotificationTypeEnum.values()) {
            if (notificationTypeEnum.getType() == type){
                return notificationTypeEnum.getName();
            }
        }
        return "";
    }
}

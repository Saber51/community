package life.majiang.community.enums;

import lombok.Getter;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/24
 * @Description:life.majiang.community.enums
 * @version:1.0
 */
public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);

    @Getter
    private Integer type;

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum:CommentTypeEnum.values()) {
            if (commentTypeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }
}

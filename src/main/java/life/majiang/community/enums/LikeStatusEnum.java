package life.majiang.community.enums;

import lombok.Getter;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/10/25
 * @Description:life.majiang.community.enums
 * @version:1.0
 */
public enum LikeStatusEnum {
    CANCELED((short)0),LIKED((short)1);

    @Getter
    private short status;

    LikeStatusEnum(short status) {
        this.status = status;
    }
}

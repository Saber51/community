package life.majiang.community.dto;

import lombok.Data;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/10/18
 * @Description:life.majiang.community.dto
 * @version:1.0
 */
@Data
public class HotTagDTO implements Comparable {
    private String name;
    private Integer priority;

    @Override
    public int compareTo(Object o) {
        return this.getPriority() - ((HotTagDTO)o).getPriority();
    }
}

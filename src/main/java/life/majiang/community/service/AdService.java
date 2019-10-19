package life.majiang.community.service;

import life.majiang.community.mapper.AdMapper;
import life.majiang.community.model.Ad;
import life.majiang.community.model.AdExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/10/19
 * @Description:life.majiang.community.service
 * @version:1.0
 */
@Service
public class AdService {

    @Autowired
    private AdMapper adMapper;

    public List<Ad> list(String pos) {
        AdExample adExample = new AdExample();
        adExample.createCriteria()
                .andStatusEqualTo(1)
                .andPosEqualTo(pos)
                .andGmtStartLessThan(System.currentTimeMillis())
                .andGmtEndGreaterThan(System.currentTimeMillis());
//        adExample.setOrderByClause("priority desc");
        return adMapper.selectByExample(adExample);
    }
}

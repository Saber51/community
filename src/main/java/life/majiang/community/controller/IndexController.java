package life.majiang.community.controller;

import life.majiang.community.cache.HotTagCache;
import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/18
 * @Description:life.majiang.community.controller
 * @version:1.0
 */
@Controller
public class IndexController {


    @Autowired
    private HotTagCache hotTagCache;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String hello(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag,
                        @RequestParam(name = "sort", required = false) String sort) {
        PaginationDTO pagination = questionService.list(search, tag, sort, page, size);
        List<String> tags = hotTagCache.getHots();
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("tags", tags);
        model.addAttribute("tag", tag);
        model.addAttribute("sort", sort);
        model.addAttribute("tagQuestions",hotTagCache.getTagQuestions());
        model.addAttribute("tagComments", hotTagCache.getTagComments());
        return "index";
    }

    @RequestMapping("/google1ccda0fc28d18c19.html")
    public String googleAdnalytics(){
        return "google1ccda0fc28d18c19";
    }

}

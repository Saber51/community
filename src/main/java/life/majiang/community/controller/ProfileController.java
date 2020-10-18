package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.service.NotificationService;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author saber-kings
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/22
 * @Description:life.majiang.community.controller
 * @version:1.0
 */
@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "10") Integer size) {

        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            return "redirect:/";
        }

        if ("questions".equals(action)) {
            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("section", "questions");
            model.addAttribute("user", user);
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "我的问题");
        } else if ("replies".equals(action)) {
            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            model.addAttribute("section", "replies");
            model.addAttribute("user", user);
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "最新回复");
        }

        return "profile";
    }

    @PostMapping("/upUser")
    public String doPublish(
            @RequestParam(value = "email",required = false) String email,
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "sex",required = false) String sex,
            @RequestParam(value = "birth",required = false) String birth,
            @RequestParam(value = "province",required = false) String province,
            @RequestParam(value = "city",required = false) String city,
            @RequestParam(value = "career",required = false) String career,
            @RequestParam(value = "bio",required = false) String bio,
            @RequestParam(value = "phone",required = false) String phone,
            @RequestParam(value = "qq",required = false) String qq,
            HttpServletRequest request,
            Model model
    ) {
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "redirect:/";
        }else {
            DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = fmt.parse(birth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            user.setEmail(email);
            user.setName(name);
            user.setSex(sex);
            user.setLocation(province+" "+city);
            user.setBirth(date);
            user.setCareer(career);
            user.setBio(bio);
            user.setPhone(phone);
            user.setQq(qq);
            userMapper.updateByPrimaryKey(user);
            return "redirect:/profile/questions";
        }

    }
}

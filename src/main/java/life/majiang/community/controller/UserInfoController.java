package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/22
 * @Description:life.majiang.community.controller
 * @version:1.0
 */
@Controller
public class UserInfoController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/userInfo/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") String id,
                          Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "10") Integer size) {

        User user = (User) request.getSession().getAttribute("user");
        Long userId;
        try {
            userId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new CustomizeException(CustomizeErrorCode.INVALID_INPUT);
        }

        if (user != null && user.getId() == userId) {
            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("user", user);
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "我的问题");
            return "redirect:/profile/questions";
        } else {
            PaginationDTO paginationDTO = questionService.list(userId, page, size);
            User thisUser = userMapper.selectByPrimaryKey(userId);
            model.addAttribute("id", userId);
            model.addAttribute("user", thisUser);
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "他的问题");
            return "userInfo";
        }

    }

}

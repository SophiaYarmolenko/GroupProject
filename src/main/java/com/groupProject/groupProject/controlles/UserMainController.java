package com.groupProject.groupProject.controlles;

import com.groupProject.groupProject.model.User;
import com.groupProject.groupProject.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class UserMainController
{
    @Autowired
    private UserRepository userRepository;

    @GetMapping("*/userMain/{userId}")
    public String userMain(@PathVariable(value = "userId") long userId, Model model)
    {
        if(!userRepository.existsById(userId))
        {
            return "redirect:/smartCourse";
        }
        User user = userRepository.findById(userId).get();
        model.addAttribute("greeting", "Hi, " + user.getName() + ", welcome back!");
        model.addAttribute("title", "SmartCourse");
        return "userMain";
    }

}

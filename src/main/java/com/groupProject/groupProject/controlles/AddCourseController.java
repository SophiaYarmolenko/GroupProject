package com.groupProject.groupProject.controlles;

import com.groupProject.groupProject.model.Course;
import com.groupProject.groupProject.model.Post;
import com.groupProject.groupProject.model.User;
import com.groupProject.groupProject.repo.CourseRepository;
import com.groupProject.groupProject.repo.PostRepository;
import com.groupProject.groupProject.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AddCourseController
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private PostRepository postRepository;

    @GetMapping("*/userMain/{userId}/createCourse")
    public String courseCreate
            (
                    @PathVariable(value = "userId") long userId,
                    Model model
            )
    {
        if (!userRepository.existsById(userId))
        {
            return "redirect:/userMain/" + userId;
        }
            model.addAttribute("title", "Smart Course");
            model.addAttribute("create", "createCourse");
            return "courseAdd";
    }

    @PostMapping("*/userMain/{userId}/createCourse")
    public String courseCreate
            (
                    @PathVariable(value = "userId") long userId,
                    @RequestParam String name,
                    Model model
            )
    {
        Course course = new Course(name);
        User user = userRepository.findById(userId).get();
        courseRepository.save(course);

        return "redirect:/coursePage/"+course.getId();
    }
}

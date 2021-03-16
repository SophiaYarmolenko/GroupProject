package com.groupProject.groupProject.controlles;

import com.groupProject.groupProject.model.Course;
import com.groupProject.groupProject.model.Post;
import com.groupProject.groupProject.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class TeacherPageController {
    @Autowired
    private CourseRepository courseRepository;
    @GetMapping("/coursePage/{id}")
    public String TeacherContr(@PathVariable(value = "id")long id,
                               Model model)
    {
        Course course =courseRepository.findById(id).get();
        model.addAttribute("courseName",course.getName());
        model.addAttribute("courseId",course.getId());
        Iterable <Post> posts= course.getPosts();
        model.addAttribute("posts",posts);
        return "courseTeacherPage";
    }
}

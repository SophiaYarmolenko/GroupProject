package com.groupProject.groupProject.controlles;


import com.groupProject.groupProject.model.Course;
import com.groupProject.groupProject.model.Document;
import com.groupProject.groupProject.model.Post;
import com.groupProject.groupProject.repo.CourseRepository;
import com.groupProject.groupProject.repo.DocumentRepository;
import com.groupProject.groupProject.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Date;

@Controller
public class DocumentsController {
    @Autowired
    private DocumentRepository docRep;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private PostRepository postRepository;
    @GetMapping("/course/{id}/materials")
    public String viewMaterialsPage(@PathVariable(value = "id")long id, Model model)
    {
        Course course = courseRepository.findById(id).get();
        Iterable <Document> docs= course.getDocuments();
        model.addAttribute("docs",docs);
        model.addAttribute("courseId",id);
        return "CourseTeacherMaterials";
    }
    @PostMapping("/course/{id}/upload/materials")
    public String uploadFile(@PathVariable(value = "id")long id, @RequestParam("document")MultipartFile multipartFile,
                             RedirectAttributes ra) throws IOException
    {
        String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Document document= new Document();
        document.setName(fileName);
        document.setContent(multipartFile.getBytes());
        document.setSize(multipartFile.getSize());
        document.setUpload_time(new Date());
        Course course = courseRepository.findById(id).get();
        document.setCourse(course);
        course.addDocument(document);
        docRep.save(document);
        ra.addFlashAttribute("message","The file has been upload successfuly ");
        return "redirect:/course/"+id+"/materials";
    }

}

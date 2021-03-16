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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Controller
public class PostsController {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private DocumentRepository docRep;
    @GetMapping("/post/{courseId}/{id}")
    public String BlogDetails(@PathVariable(value = "id")long id,@PathVariable(value = "courseId")long courseId, Model model)
    {
        if(!postRepository.existsById(id))
        {
            return "redirect:/coursePage/"+courseId;
        }
        Optional<Post> post=postRepository.findById(id);
        ArrayList<Post> res= new ArrayList<>();
        post.ifPresent(res::add);
        Post post1 = postRepository.findById(id).get();
        Iterable <Document> docs= post1.getDocuments();
        model.addAttribute("docs",docs);
        model.addAttribute("courseId",courseId);
        model.addAttribute("post",res);
        return "post-details";
    }
    public void uploadFile(MultipartFile multipartFile,Post post) throws IOException
    {
        String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Document document= new Document();
        document.setName(fileName);
        document.setContent(multipartFile.getBytes());
        document.setSize(multipartFile.getSize());
        document.setUpload_time(new Date());
        document.setPost(post);
        post.addDocument(document);
        docRep.save(document);
    }
    @PostMapping("/course/{id}")
    public String AddPost(@PathVariable(value = "id")long id, @RequestParam String title,
                          @RequestParam String announce, @RequestParam String post,
                          @RequestParam MultipartFile exampleInputFile,
                          @RequestParam MultipartFile exampleInputFile1,
                          @RequestParam MultipartFile exampleInputFile2,
                          Model model) throws IOException {
        Course course =courseRepository.findById(id).get();
        Post p=new Post();
        p.setTitle(title);
        p.setAnnounce(announce);
        p.setPost(post);
        course.addPost(p);
        postRepository.save(p);
        if(!exampleInputFile.isEmpty())
        {
            uploadFile(exampleInputFile,p);
        }
        if(!exampleInputFile1.isEmpty())
        {
            uploadFile(exampleInputFile1,p);
        }
        if(!exampleInputFile2.isEmpty())
        {
            uploadFile(exampleInputFile2,p);
        }

        return "redirect:/coursePage/"+course.getId();
    }
}

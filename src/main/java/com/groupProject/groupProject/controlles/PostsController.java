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

    @GetMapping("/post/{courseId}/{postId}")
    public String PostDetails(@PathVariable(value = "postId") long postId,
                              @PathVariable(value = "courseId") long courseId, Model model) {
        if (!postRepository.existsById(postId)) {
            return "redirect:/coursePage/" + courseId;
        }
        Optional<Post> post = postRepository.findById(postId);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        Post post1 = postRepository.findById(postId).get();
        Iterable<Document> docs = post1.getDocuments();
        model.addAttribute("docs", docs);
        model.addAttribute("courseId", courseId);
        model.addAttribute("post", res);
        model.addAttribute("postId",postId);
        return "post-details";
    }
    @GetMapping("/course/{courseId}/post/{postId}/edit")
    public String PostEdit(@PathVariable(value = "postId") long postId,
                           @PathVariable(value = "courseId") long courseId, Model model) {
        if (!postRepository.existsById(postId)) {
            return "redirect:/coursePage/" + courseId;
        }
        Optional<Post> post = postRepository.findById(postId);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        Post post1 = postRepository.findById(postId).get();
        Iterable<Document> docs = post1.getDocuments();
        model.addAttribute("docs", docs);
        model.addAttribute("courseId", courseId);
        model.addAttribute("post", res);
        model.addAttribute("postId",postId);
        return "post-edit";
    }
    @PostMapping ("/course/{courseId}/post/{postId}/edit")
    public String PostUpdate(@PathVariable(value = "postId") long postId,
                           @PathVariable(value = "courseId") long courseId,
                             @RequestParam String title,
                             @RequestParam String announce, @RequestParam String post,Model model) {
        if (!postRepository.existsById(postId)) {
            return "redirect:/coursePage/" + courseId;
        }
        Post post1 = postRepository.findById(postId).get();
        post1.setTitle(title);
        post1.setAnnounce(announce);
        post1.setPost(post);
        postRepository.save(post1);
        return "redirect:/post/" + courseId +"/"+ postId;
    }

    public void uploadFile(MultipartFile multipartFile, Post post, Course course) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Document document = new Document();
        document.setName(fileName);
        document.setContent(multipartFile.getBytes());
        document.setSize(multipartFile.getSize());
        document.setUpload_time(new Date());
        document.setPost(post);
        post.addDocument(document);
        document.setCourse(course);
        course.addDocument(document);
        docRep.save(document);
    }

    @PostMapping("/course/{id}")
    public String AddPost(@PathVariable(value = "id") long id, @RequestParam String title,
                          @RequestParam String announce, @RequestParam String post,
                          @RequestParam MultipartFile exampleInputFile,
                          @RequestParam MultipartFile exampleInputFile1,
                          @RequestParam MultipartFile exampleInputFile2,
                          Model model) throws IOException {
        Course course = courseRepository.findById(id).get();
        Post p = new Post();
        p.setTitle(title);
        p.setAnnounce(announce);
        p.setPost(post);
        p.setCourses(course);
        postRepository.save(p);
        course.addPost(p);
        if (!exampleInputFile.isEmpty()) {
            uploadFile(exampleInputFile, p,course);
        }
        if (!exampleInputFile1.isEmpty()) {
            uploadFile(exampleInputFile1, p,course);
        }
        if (!exampleInputFile2.isEmpty()) {
            uploadFile(exampleInputFile2, p,course);
        }

        return "redirect:/coursePage/" + course.getId();
    }
    @PostMapping("/course/{courseId}/post/{postId}/upload/materials")
    public String uploadFile(@PathVariable(value = "postId") long postId,
                             @PathVariable(value = "courseId") long courseId,
                             @RequestParam("document")MultipartFile multipartFile,
                             RedirectAttributes ra) throws IOException
    {

        Post post=postRepository.findById(postId).get();
        Course course = courseRepository.findById(courseId).get();
        if(!multipartFile.isEmpty())
        {
            uploadFile(multipartFile,post,course);
        }
        return "redirect:/post/" + courseId +"/"+postId;
    }

    @PostMapping("/course/{courseId}/post/{postId}/remove/doc/{docId}")
    public String CourseMaterialDelete(@PathVariable(value = "postId") long postId,
                                       @PathVariable(value = "docId") long docId,
                                       @PathVariable(value = "courseId") long courseId, Model model) {
        Post post = postRepository.findById(postId).get();
        Course course = courseRepository.findById(courseId).get();
        Document doc = docRep.findById(docId).get();
        post.removeDocument(doc);
        course.removeDocument(doc);
        docRep.deleteById(docId);
        return "redirect:/post/" + courseId +"/"+ postId;

    }
    @PostMapping("/course/{courseId}/post/{postId}/remove/post")
    public String CoursePostDelete(@PathVariable(value = "postId") long postId,
                                 @PathVariable(value = "courseId") long courseId
                                ,Model model)
    {
        Post post=postRepository.findById(postId).orElseThrow();
        Iterable<Document> docs=post.getDocuments();
        for ( Document doc: docs) {
            docRep.deleteById(doc.getId());
        }
        Course course=courseRepository.findById(courseId).get();
        course.removePost(post);
        postRepository.delete(post);
        return "redirect:/coursePage/"+courseId;
    }
}

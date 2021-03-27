package com.groupProject.groupProject.controlles;

import com.groupProject.groupProject.model.Course;
import com.groupProject.groupProject.model.Document;
import com.groupProject.groupProject.model.Post;
import com.groupProject.groupProject.model.Task;
import com.groupProject.groupProject.repo.CourseRepository;
import com.groupProject.groupProject.repo.DocumentRepository;
import com.groupProject.groupProject.repo.PostRepository;
import com.groupProject.groupProject.repo.TaskRepository;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Controller
public class TaskController {
    @Autowired
    private DocumentRepository docRep;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TaskRepository taskRepository;
    @GetMapping("/course/{courseId}/tasks")
    public String viewTasksPage(@PathVariable(value = "courseId")long courseId, Model model)
    {
        Course course =courseRepository.findById(courseId).get();
        model.addAttribute("courseName",course.getName());
        model.addAttribute("courseId",course.getId());
        Iterable <Task> tasks= course.getTasks();
        model.addAttribute("tasks",tasks);
        return"task";
    }
    @PostMapping("/course/{courseId}/taskAdd")
    public String addTask(@PathVariable(value = "courseId") long courseId, @RequestParam String title,
                          @RequestParam String date, @RequestParam String task,
                          @RequestParam MultipartFile exampleInputFile,
                          @RequestParam MultipartFile exampleInputFile1,
                          @RequestParam MultipartFile exampleInputFile2,
                          Model model) throws IOException, ParseException {
        Course course = courseRepository.findById(courseId).get();
        Task t= new Task();
        t.setTitle(title);
        t.setTask(task);
        t.setCourse(course);
        t.setTime(date);
        taskRepository.save(t);
        course.addTask(t);

        if (!exampleInputFile.isEmpty()) {
            uploadFile(exampleInputFile, t,course);
        }
        if (!exampleInputFile1.isEmpty()) {
            uploadFile(exampleInputFile1, t,course);
        }
        if (!exampleInputFile2.isEmpty()) {
            uploadFile(exampleInputFile2, t,course);
        }

        return "redirect:/course/"+courseId+"/tasks";
    }
    public void uploadFile(MultipartFile multipartFile, Task task, Course course) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Document document = new Document();
        document.setName(fileName);
        document.setContent(multipartFile.getBytes());
        document.setSize(multipartFile.getSize());
        document.setUpload_time(new Date());
        document.setTask(task);
        task.addDocument(document);
        document.setCourse(course);
        course.addDocument(document);
        docRep.save(document);
    }

    @PostMapping("/course/{courseId}/task/{taskId}/delete")
    public String taskDelete(@PathVariable(value = "taskId") long taskId,
                             @PathVariable(value = "courseId") long courseId,
                             Model model)
    {
        Task task=taskRepository.findById(taskId).orElseThrow();
        Iterable<Document> docs=task.getDocuments();
        for ( Document doc: docs) {
            docRep.deleteById(doc.getId());
        }
        Course course=courseRepository.findById(courseId).get();
        course.removeTask(task);
        taskRepository.delete(task);
        return "redirect:/course/"+courseId+"/tasks";
    }
    @GetMapping("/course/{courseId}/task/{taskId}/edit")
    public String PostEdit(@PathVariable(value = "taskId") long taskId,
                           @PathVariable(value = "courseId") long courseId, Model model) {
        if (!taskRepository.existsById(taskId)) {
            return "redirect:/course/"+courseId+"/tasks";
        }
         Course course=courseRepository.findById(courseId).get();
        Optional<Task> task = taskRepository.findById(taskId);
        ArrayList<Task> res = new ArrayList<>();
        task.ifPresent(res::add);
        Task t = taskRepository.findById(taskId).get();
        Iterable<Document> docs = t.getDocuments();
        model.addAttribute("docs", docs);
        model.addAttribute("courseId", courseId);
        model.addAttribute("courseName", course.getName());
        model.addAttribute("tasks", res);
        model.addAttribute("taskId",taskId);
        return "task-edit";
    }
    @PostMapping ("/course/{courseId}/task/{taskId}/edit")
    public String PostUpdate(@PathVariable(value = "taskId") long taskId,
                             @PathVariable(value = "courseId") long courseId,
                             @RequestParam String title,
                             @RequestParam String date, @RequestParam String task,Model model) {
        if (!taskRepository.existsById(taskId)) {
            return "redirect:/course/"+courseId+"/tasks";
        }
        Task t = taskRepository.findById(taskId).get();
        t.setTitle(title);
        t.setTime(date);
        t.setTask(task);
        taskRepository.save(t);
        return "redirect:/course/"+courseId+"/tasks";
    }
    @PostMapping("/course/{courseId}/task/{taskId}/upload/materials")
    public String uploadFile(@PathVariable(value = "taskId") long taskId,
                             @PathVariable(value = "courseId") long courseId,
                             @RequestParam("document")MultipartFile multipartFile,
                             RedirectAttributes ra) throws IOException
    {

       Task task=taskRepository.findById(taskId).get();
        Course course = courseRepository.findById(courseId).get();
        if(!multipartFile.isEmpty())
        {
            uploadFile(multipartFile,task,course);
        }
        return "redirect:/course/"+courseId+"/task/"+taskId+"/edit";
    }

    @PostMapping("/course/{courseId}/task/{taskId}/delete/doc/{docId}")
    public String TaskMaterialDelete(@PathVariable(value = "taskId") long taskId,
                                       @PathVariable(value = "docId") long docId,
                                       @PathVariable(value = "courseId") long courseId, Model model) {
        Task task=taskRepository.findById(taskId).get();
        Course course = courseRepository.findById(courseId).get();
        Document doc = docRep.findById(docId).get();
        task.removeDocument(doc);
        course.removeDocument(doc);
        docRep.deleteById(docId);
        return "redirect:/course/"+courseId+"/task/"+taskId+"/edit";

    }

}

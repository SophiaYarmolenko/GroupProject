package com.groupProject.groupProject.controlles;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController
{
    private final int ARTICLE_NUMBER = 3;

    private final ArticleGenerationController articleGenerationController;

    private List<String> articleNames = new ArrayList<>();
    private List<String> articleLinks = new ArrayList<>();

    @Autowired
    public MainController(ArticleGenerationController articleGenerationController)
    {
        for(int i = 0; i<ARTICLE_NUMBER; i++)
        {
            if(articleGenerationController == null)
            {
                System.out.println("null");
            }
            try {
                articleLinks.add(articleGenerationController.generateGetRequestLinkToWiki());
                articleNames.add(articleGenerationController.getArticleName());
            } catch (IOException e) {
                articleLinks.add("localhost:8080/smartCourse");
                articleNames.add("Something interesting");
            }
        }
        this.articleGenerationController = articleGenerationController;
    }

    @GetMapping("/smartCourse")
    public String home(Model model) throws IOException {
        model.addAttribute("title", "SmartCourse");
        for(int i = 1; i<=ARTICLE_NUMBER; i++)
        {
            model.addAttribute("articleName"+ i, articleNames.get(i-1));
        }
        return "home";
    }

    @RequestMapping(value = "/wikiArticle/{id}", method = RequestMethod.GET)
    public RedirectView wikiArticle(@PathVariable(value = "id") int id) {
        try {
            if(id - 1 < ARTICLE_NUMBER)
            {
                RedirectView redirectView = new RedirectView(articleLinks.get(id-1));
                articleLinks.remove(id-1);
                articleLinks.add(id - 1, articleGenerationController.generateGetRequestLinkToWiki());
                articleNames.remove(id-1);
                articleNames.add(id - 1, articleGenerationController.getArticleName());
                return redirectView;
            }
            else
                return new RedirectView("home");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RedirectView("home");
    }

    @RequestMapping(value = "/pomodoro", method = RequestMethod.GET)
    public RedirectView pomodoro()
    {
        return new RedirectView("https://pomofocus.io/");
    }

}

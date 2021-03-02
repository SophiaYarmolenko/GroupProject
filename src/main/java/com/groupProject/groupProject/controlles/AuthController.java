package com.groupProject.groupProject.controlles;

import com.groupProject.groupProject.exception.UserAlreadyExistException;
import com.groupProject.groupProject.model.User;
import com.groupProject.groupProject.Config.JwtProvider;
import com.groupProject.groupProject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;



    @RequestMapping(value="/register", method=RequestMethod.GET)
    public ModelAndView registration (@ModelAttribute("request")  RegistrationRequest request) {
        System.out.println("register");
        return new ModelAndView("register","reguest",request);
    }
    @RequestMapping(value="/auth", method=RequestMethod.GET)
    public ModelAndView login () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }
    @PostMapping("/register")
    public ModelAndView registerUser(@Valid @ModelAttribute("request")  RegistrationRequest request, BindingResult bindingResult) {
        User u = new User();
        System.out.println("here");
        System.out.println(request.getName());
        if (bindingResult.hasErrors())
        { System.out.println("has error...");
            return new ModelAndView("register");}
        u.setName(request.getName());
        u.setSurname(request.getSurname());
        u.setEmail(request.getEmail());
        u.setPassword(request.getPassword());

        try {
            userService.saveUser(u);
        } catch (UserAlreadyExistException uaeEx) {
            return new ModelAndView("errorpage","message", "An account for that username/email already exists.");
        }
        return  new ModelAndView("home", "user",u);
    }


    @PostMapping("/auth")
    public ModelAndView auth(@RequestParam("email") String email, @RequestParam("password") String password) {
        User u = userService.findByLoginAndPassword(email, password);
        if(u==null)
        {
            return new ModelAndView("errorpage","message", "There are no account for that username/email  exists.");
        }
        String token = jwtProvider.generateToken(u.getEmail());
        return new ModelAndView("home", "user",u);

    }
}
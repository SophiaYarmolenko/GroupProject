package com.groupProject.groupProject.Config;

import com.groupProject.groupProject.Service.UserService;
import com.groupProject.groupProject.model.Role;
import com.groupProject.groupProject.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;


    public CustomUserDetails loadUserByUsername(String email, Long courseId) throws UsernameNotFoundException {
        User userEntity = userService.findByEmail(email);
        Role role = userService.findByUserIdAndCourseId(courseId, userEntity.getId());
        return CustomUserDetails.fromUserEntityToCustomUserDetails(userEntity,role);
    }
    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userEntity = userService.findByEmail(email);
        return new  CustomUserDetails();
    }


}

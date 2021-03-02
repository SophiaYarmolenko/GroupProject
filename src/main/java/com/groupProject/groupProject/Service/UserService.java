package com.groupProject.groupProject.Service;

import com.groupProject.groupProject.exception.UserAlreadyExistException;
import com.groupProject.groupProject.model.RoleEntity;
import com.groupProject.groupProject.model.User;
import com.groupProject.groupProject.repo.RoleEntityRepository;
import com.groupProject.groupProject.repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleEntityRepository roleEntityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) throws UserAlreadyExistException {
        //RoleEntity userRole = roleEntityRepository.findByName("ROLE_USER");
        if (findByEmail(user.getEmail())!=null) {
            throw new UserAlreadyExistException(
                    "There is an account with that email address: "
                            +  user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByLoginAndPassword(String login, String password) {
        User userEntity = findByEmail(login);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        return null;
    }
}
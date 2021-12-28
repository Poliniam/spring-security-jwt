
package com.javamaster.springsecurityjwt.controller;

import com.javamaster.springsecurityjwt.config.jwt.JwtProvider;
import com.javamaster.springsecurityjwt.entity.CurrentUser;
import com.javamaster.springsecurityjwt.entity.RoleEntity;
import com.javamaster.springsecurityjwt.entity.UserEntity;
import com.javamaster.springsecurityjwt.exceptions.UserException;
import com.javamaster.springsecurityjwt.service.MailSender;
import com.javamaster.springsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private GameObjectController contr;

    static Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("D:\\spring-security-jwt-master\\src\\main\\resources\\log.config")){
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(RegistrationController.class.getName());
        }catch (Exception ignore){
            ignore.printStackTrace();
        }
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@Valid RegistrationRequest registrationRequest) throws UserException {
        if (registrationRequest.getLogin()!= null && registrationRequest.getPassword()!= null && registrationRequest.getEmail()!= null) {
            if (registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
                ModelAndView view = new ModelAndView();
                view.setViewName("main");
                UserEntity u = new UserEntity();
                u.setPassword(registrationRequest.getPassword());
                u.setLogin(registrationRequest.getLogin());
                u.setEmail(registrationRequest.getEmail());
                u.setActivationCode(UUID.randomUUID().toString());
                u.setStatus(true);
                userService.saveUser(u);
                CurrentUser.setRoleOfCurrentUser("ROLE_USER");
                String message = String.format(
                        "Hello, %s! \n" +
                                "Welcome to Trader application. Please, visit next link: http://localhost:8080/auth/confirm/%s",
                        u.getLogin(),
                        u.getActivationCode());
                //mailSender.send(u.getEmail(), "Activation code", message);
                List list = new ArrayList();
                list = contr.getAllGameObjects();
                view.addObject("list", list);
                LOGGER.log(Level.INFO,"The activation code was sent to user");
                return view;
            }
            else throw new UserException("Password mismatch");
        }
        else throw new UserException("Write all data, please");
    }

    @PostMapping("/auth")
    public ModelAndView auth(AuthRequest request) throws UserException {
        CurrentUser currentUser = new CurrentUser();
        UserEntity userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if (userEntity != null) {
                RoleEntity role = userEntity.getRoleEntity();
                currentUser.setIdOfCurrentUser(userEntity.getId());
                currentUser.setRoleOfCurrentUser(role.getName());
                String token = jwtProvider.generateToken(userEntity.getLogin());
                LOGGER.log(Level.INFO,"The user was authenticated");
                ModelAndView view = new ModelAndView();
                view.setViewName("main");
            List list = new ArrayList();
            list = contr.getAllGameObjects();
            view.addObject("list", list);
                return view;
        }
        else throw new UserException("Wrong password or login");
    }

    @GetMapping("/auth/confirm/{hash_code}")
    public String activate( @PathVariable String hash_code) throws UserException {
        UserEntity user = userService.findByActivationCode(hash_code);
        if(user != null){
            user.setStatus(true);
            userService.saveUserOnceMore(user);
            LOGGER.log(Level.INFO,"The activation code was approved");
            return "Okay, we can register you. Now, you should do an authorization.";
        }
        else throw new UserException("Incorrect activation code. Register again");
    }

    @GetMapping("/hello")
    public ModelAndView activate() throws UserException {
        ModelAndView view = new ModelAndView();
        view.setViewName("registration");
        return view;
    }

    @GetMapping("/backToMain")
    public ModelAndView backToMain() throws UserException {
        ModelAndView view = new ModelAndView();
        view.setViewName("main");
        List list = new ArrayList();
        list = contr.getAllGameObjects();
        view.addObject("list", list);
        return view;
    }

    @GetMapping("/createGameObject")
    @Retryable(value = UserException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ModelAndView createGameObject() throws UserException {
        if(CurrentUser.getRoleOfCurrentUser().equals("ROLE_USER")) {
            ModelAndView view = new ModelAndView();
            view.setViewName("gameObject");
            return view;
        }
        else throw new UserException("You are not a trader");
    }

    @Recover
    private ModelAndView recoverGameObject(Throwable throwable){
        ModelAndView view = new ModelAndView();
        view.setViewName("gameObjectError");
        System.out.println(throwable.getMessage());
        return view;
    }
}



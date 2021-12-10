
package com.javamaster.springsecurityjwt.controller;

import com.javamaster.springsecurityjwt.config.jwt.JwtProvider;
import com.javamaster.springsecurityjwt.entity.CurrentUser;
import com.javamaster.springsecurityjwt.entity.RoleEntity;
import com.javamaster.springsecurityjwt.entity.UserEntity;
import com.javamaster.springsecurityjwt.exceptions.UserException;
import com.javamaster.springsecurityjwt.service.MailSender;
import com.javamaster.springsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private MailSender mailSender;

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
    public String registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) throws UserException {
        if (registrationRequest.getLogin()!= null && registrationRequest.getPassword()!= null && registrationRequest.getEmail()!= null) {
            if (registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
                UserEntity u = new UserEntity();
                u.setPassword(registrationRequest.getPassword());
                u.setLogin(registrationRequest.getLogin());
                u.setEmail(registrationRequest.getEmail());
                u.setActivationCode(UUID.randomUUID().toString());
                u.setStatus(false);
                userService.saveUser(u);
                String message = String.format(
                        "Hello, %s! \n" +
                                "Welcome to Trader application. Please, visit next link: http://localhost:8080/auth/confirm/%s",
                        u.getLogin(),
                        u.getActivationCode());

                mailSender.send(u.getEmail(), "Activation code", message);

                LOGGER.log(Level.INFO,"The activation code was sent to user");
                return "Successful registration! Check you mail we send you activation code";
            }
            else throw new UserException("Password mismatch");
        }
        else throw new UserException("Write all data, please");
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) throws UserException {
        CurrentUser currentUser = new CurrentUser();
        UserEntity userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if (userEntity != null) {
            if (userEntity.getStatus().equals(false)) {
                throw new UserException("Confirm the activation code using the link in your mail");
            } else {
                RoleEntity role = userEntity.getRoleEntity();
                currentUser.setIdOfCurrentUser(userEntity.getId());
                currentUser.setRoleOfCurrentUser(role.getName());
                String token = jwtProvider.generateToken(userEntity.getLogin());
                LOGGER.log(Level.INFO,"The user was authenticated");
                return new AuthResponse(token);
            }
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
}



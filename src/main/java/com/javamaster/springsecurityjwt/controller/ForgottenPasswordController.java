package com.javamaster.springsecurityjwt.controller;

import com.javamaster.springsecurityjwt.entity.UserEntity;
import com.javamaster.springsecurityjwt.exceptions.UserException;
import com.javamaster.springsecurityjwt.service.MailSender;
import com.javamaster.springsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class ForgottenPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailSender mailSender;

    @PostMapping("/forgetPassword")
    public String registerUser(@RequestBody @Valid RegistrationRequest newForgottenPassword) throws UserException {
        if (newForgottenPassword.getEmail()!= null) {
            UserEntity u = userService.findByEmail(newForgottenPassword.getEmail());
            u.setActivationCode(UUID.randomUUID().toString());
            u.setStatus(false);
            userService.saveUserOnceMore(u);
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Trader application. Please, visit next link: http://localhost:8080/forgetPassword/confirm/%s",
                    u.getLogin(),
                    u.getActivationCode());

            mailSender.send(u.getEmail(), "Activation code", message);
            return "Successful registration! Check you mail we send you activation code";
        }
        else throw new UserException("Write all data, please");
    }

    @GetMapping("/forgetPassword/confirm/{hash_code}")
    public String forgetPasswordActivate( @PathVariable String hash_code) throws UserException {
        UserEntity user = userService.findByActivationCode(hash_code);
        if(user != null){
            userService.saveUserOnceMore(user);
            return "Okay, we can register you. Now, make a new password";
        }

        else throw new UserException("Incorrect activation code. Register again");

    }

    @PostMapping("/newPassword")
    public String makeNewPassword( @RequestBody @Valid RegistrationRequest request) throws UserException {
        UserEntity user = userService.findByEmail(request.getEmail());
        if (request.getEmail()!= null && request.getPassword()!=null && request.getConfirmPassword() != null) {
            if (request.getPassword().equals(request.getConfirmPassword())) {
                if (user != null) {
                    user.setPassword(request.getPassword());
                    user.setStatus(true);
                    userService.saveUserWithNewPassword(user);
                    return "Fine! Now, you should do an authorization.";
                }
                else throw new UserException("Incorrect email. Try again");
            }
            else throw new UserException("Password mismatch");
        }
        else throw new UserException("Write all data please");
    }
}

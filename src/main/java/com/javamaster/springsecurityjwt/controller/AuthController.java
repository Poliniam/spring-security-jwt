package com.javamaster.springsecurityjwt.controller;

import com.javamaster.springsecurityjwt.config.jwt.JwtProvider;
import com.javamaster.springsecurityjwt.entity.UserEntity;
import com.javamaster.springsecurityjwt.service.MailSender;
import com.javamaster.springsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private MailSender mailSender;

    @PostMapping("/register")
    public String registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        UserEntity u = new UserEntity();
        u.setPassword(registrationRequest.getPassword());
        u.setLogin(registrationRequest.getLogin());
        u.setEmail(registrationRequest.getEmail());
        u.setActivationCode(UUID.randomUUID().toString());
        userService.saveUser(u);

        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to Sweater. Please, visit next link: http://localhost:8080/auth/confirm/%s",
                u.getLogin(),
                u.getActivationCode() );

        mailSender.send(u.getEmail(), "Activation code", message);

        return "OK";
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) {
        UserEntity userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(userEntity.getLogin());
        return new AuthResponse(token);
    }

    @GetMapping("/auth/confirm/{hash_code}")
    public String activate( @PathVariable String hash_code) {
       if(userService.findByActivationCode(hash_code) != null){
           return "Okay, you we can register you";
       }
       else {
           return "Go away";
       }

    }
}

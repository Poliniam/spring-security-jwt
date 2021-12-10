package com.javamaster.springsecurityjwt.service;

import com.javamaster.springsecurityjwt.entity.UserEntity;
import com.javamaster.springsecurityjwt.exceptions.UserException;
import com.javamaster.springsecurityjwt.repository.UserEntityRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserEntityRepository userRepo;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void saveUser() throws UserException {
        UserEntity user = new UserEntity();
        user.setEmail("some@mail.ru");
        boolean isUserCreated = userService.saveUserOnce(user);
        Assert.assertTrue(isUserCreated);
        Mockito.verify(userRepo, Mockito.times(1)).save(user);

    }
    @Test
    public void addUserFailTest() throws UserException {
        UserEntity user = new UserEntity();
        user.setEmail("5");
        Mockito.doReturn(new UserEntity())
                .when(userRepo)
                .findByEmail("5");
        boolean isUserCreated = userService.saveUserOnce(user);
        Assert.assertFalse(isUserCreated);
        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(UserEntity.class));

    }

}
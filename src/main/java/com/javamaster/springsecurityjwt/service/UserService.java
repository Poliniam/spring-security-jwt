package com.javamaster.springsecurityjwt.service;

import com.javamaster.springsecurityjwt.entity.RoleEntity;
import com.javamaster.springsecurityjwt.entity.UserEntity;
import com.javamaster.springsecurityjwt.exceptions.UserException;
import com.javamaster.springsecurityjwt.repository.RoleEntityRepository;
import com.javamaster.springsecurityjwt.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private RoleEntityRepository roleEntityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity saveUserWithNewPassword(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userEntityRepository.save(userEntity);
    }

    public Boolean saveUser(UserEntity userEntity) throws UserException {
        if(findByEmail(userEntity.getEmail())==null && findByLogin(userEntity.getLogin())==null) {
            RoleEntity userRole = roleEntityRepository.findByName("ROLE_USER");
            userEntity.setRoleEntity(userRole);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
             userEntityRepository.save(userEntity);
             return true;
        }
        else throw new UserException("That user already exists");
    }

    public UserEntity saveUserOnceMore (UserEntity userEntity){
        return userEntityRepository.save(userEntity);
    }


    public UserEntity findById(int id){
        UserEntity user = userEntityRepository.findById(id);
        return user;
    }

    public UserEntity findByLogin(String login) {
        return userEntityRepository.findByLogin(login);
    }

    public UserEntity findByActivationCode(String activationCode) {
        UserEntity userWithThatCode = userEntityRepository.findByActivationCode((activationCode));
        return userWithThatCode;
    }

    public UserEntity findByLoginAndPassword(String login, String password) {
        UserEntity userEntity = findByLogin(login);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        return null;
    }

    public UserEntity findByEmail(String email){
        return userEntityRepository.findByEmail(email);
    }
}

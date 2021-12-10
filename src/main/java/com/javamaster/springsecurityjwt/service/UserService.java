package com.javamaster.springsecurityjwt.service;

import com.javamaster.springsecurityjwt.entity.RoleEntity;
import com.javamaster.springsecurityjwt.entity.UserEntity;
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

    public UserEntity saveUser(UserEntity userEntity) {
        RoleEntity userRole = roleEntityRepository.findByName("ROLE_USER");
        userEntity.setRoleEntity(userRole);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userEntityRepository.save(userEntity);
    }

    public UserEntity saveUserOnceMore (UserEntity userEntity){
        return userEntityRepository.save(userEntity);
    }

    public boolean saveUserOnce (UserEntity userEntity){
        userEntityRepository.save(userEntity);
        return true;
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

    public boolean activateUser(String code) {
        UserEntity user = userEntityRepository.findByActivationCode(code);

        if(user == null)
        {
            return false;
        }

        user.setActivationCode(null);
        userEntityRepository.save(user);

        return true;
    }

    public UserEntity findByEmail(String email){
        return userEntityRepository.findByEmail(email);
    }
}

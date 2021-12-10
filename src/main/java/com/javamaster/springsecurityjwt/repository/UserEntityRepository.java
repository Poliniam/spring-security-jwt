package com.javamaster.springsecurityjwt.repository;

import com.javamaster.springsecurityjwt.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByLogin(String login);

    UserEntity findByActivationCode(String code);

    UserEntity findById(int id);

    UserEntity findByEmail(String email);

}

package com.javamaster.springsecurityjwt.repository;

import com.javamaster.springsecurityjwt.entity.CommentEntity;
import com.javamaster.springsecurityjwt.entity.GameObjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Optional;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {


    void deleteById(Integer id);

    @Query("select id from CommentEntity where post_id = :gameObjectEntity")
    ArrayList<Integer> getAllByPost_id(@Param("gameObjectEntity") Integer gameObjectEntity);

   Optional<CommentEntity> findById(Integer id);


    //CommentEntity getAllByPost_id(GameObjectEntity gameObjectEntity);

}

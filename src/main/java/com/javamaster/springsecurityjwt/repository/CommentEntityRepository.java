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

    @Query("select f from CommentEntity f where f.post_id = :gameObjectEntity")
    ArrayList<CommentEntity> getAllByPost_id(@Param("gameObjectEntity") GameObjectEntity gameObjectEntity);

    Optional<CommentEntity> findById(Integer id);

    @Query("select f from CommentEntity f")
    ArrayList<CommentEntity> getAll();

    CommentEntity getById(Integer id);



}

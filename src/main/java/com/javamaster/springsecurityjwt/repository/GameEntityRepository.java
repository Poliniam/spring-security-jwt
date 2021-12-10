package com.javamaster.springsecurityjwt.repository;

import com.javamaster.springsecurityjwt.entity.GameEntity;
import com.javamaster.springsecurityjwt.entity.GameObjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface GameEntityRepository extends JpaRepository<GameEntity, Integer> {

   GameEntity findByGameName(String gameName);

   ArrayList<GameEntity> findAll();

   GameEntity findById(int id);

   @Query("select id from CommentEntity where post_id = :gameObjectEntity")
   ArrayList<Integer> getAllByPost_id(@Param("gameObjectEntity") GameObjectEntity gameObjectEntity);

}

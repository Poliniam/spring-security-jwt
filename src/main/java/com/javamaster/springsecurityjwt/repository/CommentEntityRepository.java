package com.javamaster.springsecurityjwt.repository;

import com.javamaster.springsecurityjwt.entity.CommentEntity;
import com.javamaster.springsecurityjwt.entity.GameEntity;
import com.javamaster.springsecurityjwt.entity.GameObjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {


    void deleteById(Integer id);

    @Query("select id from CommentEntity where post_id = :gameObjectEntity")
    ArrayList<Integer> getAllByPost_id(@Param("gameObjectEntity") GameObjectEntity gameObjectEntity);


}

package com.javamaster.springsecurityjwt.repository;
import com.javamaster.springsecurityjwt.entity.CommentEntity;
import com.javamaster.springsecurityjwt.entity.GameObjectEntity;
import com.javamaster.springsecurityjwt.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GameObjectEntityRepository extends JpaRepository <GameObjectEntity, Integer> {

    GameObjectEntity findByTitle(String Title);

    GameObjectEntity findById(int id);

    @Transactional
    @Modifying
    @Query("update GameObjectEntity set status = true WHERE id = :id")
    void updateById(@Param("id") Integer id);

    void deleteById(Integer id);

    List<GameObjectEntity> findAll();

    Optional<GameObjectEntity> findById(Integer id);

    //List<GameObjectEntity> findAllByAuthor_id(UserEntity author);


}

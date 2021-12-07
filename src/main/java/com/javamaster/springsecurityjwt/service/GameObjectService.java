package com.javamaster.springsecurityjwt.service;

import com.javamaster.springsecurityjwt.entity.CommentEntity;
import com.javamaster.springsecurityjwt.entity.GameObjectEntity;
import com.javamaster.springsecurityjwt.entity.UserEntity;
import com.javamaster.springsecurityjwt.repository.GameObjectEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameObjectService {

    @Autowired
    private GameObjectEntityRepository gameObjectEntityRepository;

    public GameObjectEntity findById(int id) {
        GameObjectEntity gameObject = gameObjectEntityRepository.findById(id);

        return gameObject;
    }

    public GameObjectEntity findByTitle(String title) {

        return gameObjectEntityRepository.findByTitle(title);
    }
    public void approveById(Integer id) {

        gameObjectEntityRepository.updateById(id);
    }
    public void deleteById(Integer id) {

        gameObjectEntityRepository.deleteById(id);
    }
    public boolean saveRequest(GameObjectEntity reqEntity) {

        gameObjectEntityRepository.save(reqEntity);
        return true;
    }
    public List<GameObjectEntity> getAll( ) {

        return gameObjectEntityRepository.findAll();
    }

     /*public List<GameObjectEntity> getByAuthor_id(UserEntity user){
        return gameObjectEntityRepository.findAllByAuthor_id(user);
    } */
}

package com.javamaster.springsecurityjwt.service;

import com.javamaster.springsecurityjwt.entity.GameEntity;
import com.javamaster.springsecurityjwt.entity.GameObjectEntity;
import com.javamaster.springsecurityjwt.repository.GameEntityRepository;
import com.javamaster.springsecurityjwt.repository.GameObjectEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    @Autowired
    private GameEntityRepository gameEntityRepository;

    public GameEntity findByGameName(String gameName) {
        GameEntity gameEntity = gameEntityRepository.findByGameName(gameName);
        return gameEntity;
    }

    public ArrayList<GameEntity> getAll( ) {
        return gameEntityRepository.findAll();
    }

    public boolean saveGame(GameEntity game) {
        gameEntityRepository.save(game);
        return true;
    }

    public GameEntity findById(int id) {
        GameEntity game = gameEntityRepository.findById(id);
        return game;
    }
}

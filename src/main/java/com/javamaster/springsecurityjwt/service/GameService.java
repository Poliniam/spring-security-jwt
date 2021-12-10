package com.javamaster.springsecurityjwt.service;

import com.javamaster.springsecurityjwt.entity.GameEntity;
import com.javamaster.springsecurityjwt.exceptions.UserException;
import com.javamaster.springsecurityjwt.repository.GameEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GameService {

    @Autowired
    private GameEntityRepository gameEntityRepository;

    public GameEntity findByGameName(String gameName) throws UserException {
        GameEntity gameEntity = gameEntityRepository.findByGameName(gameName);
        if(gameEntity != null) {
            return gameEntity;
        }
        else throw new UserException("There is no such game");
    }

    public ArrayList<GameEntity> getAll( ) {
        return gameEntityRepository.findAll();
    }

    public boolean saveGame(GameEntity game) throws UserException {
        if (findByGameName(game.getGameName()) == null) {
            gameEntityRepository.save(game);
            return true;
        }
     else throw new UserException("That game already exists");
    }

    public boolean saveUpdatedGame(GameEntity game) throws UserException {
            gameEntityRepository.save(game);
            return true;
    }

    public GameEntity findById(int id) {
        GameEntity game = gameEntityRepository.findById(id);
        return game;
    }
}

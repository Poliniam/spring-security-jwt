package com.javamaster.springsecurityjwt.controller;

import com.javamaster.springsecurityjwt.entity.GameEntity;
import com.javamaster.springsecurityjwt.exceptions.UserException;
import com.javamaster.springsecurityjwt.repository.GameEntityRepository;
import com.javamaster.springsecurityjwt.service.GameService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class RequestControllerTest {

    @Autowired
    private GameService gameService;
    @MockBean
    private GameEntityRepository gameEntityRepository;
    @Test
    public void createGame() throws UserException {
        GameEntity game = new GameEntity();
        game.setId(7);
        boolean isUserCreated = gameService.saveGame(game);
        Assert.assertTrue(isUserCreated);
        Mockito.verify(gameEntityRepository, Mockito.times(1)).save(game);
    }

    @Test
    public void addGameFailTest() throws UserException {
        GameEntity game = new GameEntity();
        Boolean isReqCreated;
        game.setGameName("fifa");
        Mockito.doReturn(new GameEntity())
                .when(gameEntityRepository)
                .findByGameName("fifa");
        try{
            gameService.saveGame(game);
             isReqCreated=true;
        } catch (Exception e) {
            isReqCreated=false;
        }
        Assert.assertFalse(isReqCreated);
        Mockito.verify(gameEntityRepository, Mockito.times(0)).save(ArgumentMatchers.any(GameEntity.class));

    }
}
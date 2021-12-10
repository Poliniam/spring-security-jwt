package com.javamaster.springsecurityjwt.controller;

import com.javamaster.springsecurityjwt.entity.GameEntity;
import com.javamaster.springsecurityjwt.exceptions.UserException;
import com.javamaster.springsecurityjwt.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    static Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("D:\\spring-security-jwt-master\\src\\main\\resources\\log.config")){
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(GameController.class.getName());
        }catch (Exception ignore){
            ignore.printStackTrace();
        }
    }

    @GetMapping("/games")
    public List<GameEntity> getAllGames() throws UserException {
        ArrayList<GameEntity> listOfGames = new ArrayList<GameEntity>();
        listOfGames = gameService.getAll();
        if(listOfGames.size() == 0){
            throw new UserException("There is no games");
        }
        else return listOfGames;
    }

    @PostMapping("/admin/game")
    public String addNewGame(@RequestBody @Valid NewGame newGame) throws UserException {
            GameEntity game = new GameEntity();
                game.setGameName(newGame.getGameName());
                gameService.saveGame(game);
                LOGGER.log(Level.INFO,"New game added");
                return "New game added";
    }

    @PutMapping("admin/games/{id}")
    public String editTheGame(@PathVariable Integer id, @RequestBody @Valid NewGame newGame) throws UserException {
            GameEntity game = new GameEntity();
            if (gameService.findById(id) != null) {
                game.setId(id);
                game.setGameName(newGame.getGameName());
                gameService.saveUpdatedGame(game);
                LOGGER.log(Level.INFO,"The game edited");
                return "The game edited";
            } else throw new UserException("There is no game with such id");
    }
}

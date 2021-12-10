package com.javamaster.springsecurityjwt.controller;

import com.javamaster.springsecurityjwt.entity.GameEntity;
import com.javamaster.springsecurityjwt.exceptions.UserException;
import com.javamaster.springsecurityjwt.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    java.util.logging.Logger log = java.util.logging.Logger.getLogger(GameController.class.getName());

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
                log.info("New game added");
                return "New game added";
    }

    @PutMapping("admin/games/{id}")
    public String editTheGame(@PathVariable Integer id, @RequestBody @Valid NewGame newGame) throws UserException {
            GameEntity game = new GameEntity();
            if (gameService.findById(id) != null) {
                game.setId(id);
                game.setGameName(newGame.getGameName());
                gameService.saveUpdatedGame(game);
                log.info("The game edited");
                return "The game edited";
            } else throw new UserException("There is no game with such id");
    }
}

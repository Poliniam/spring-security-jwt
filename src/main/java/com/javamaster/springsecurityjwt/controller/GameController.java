package com.javamaster.springsecurityjwt.controller;

import com.javamaster.springsecurityjwt.entity.CurrentUser;
import com.javamaster.springsecurityjwt.entity.GameEntity;
import com.javamaster.springsecurityjwt.exceptions.UserException;
import com.javamaster.springsecurityjwt.service.GameService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {

    java.util.logging.Logger log = java.util.logging.Logger.getLogger(GameController.class.getName());
    @Autowired
    private GameService gameService;

    // to get all games | GET /games
    @GetMapping("/games")
    public List<GameEntity> getAllGames() {
        ArrayList<GameEntity> listOfGames = new ArrayList<GameEntity>();
        listOfGames = gameService.getAll();
        return listOfGames;
    }

    // to add new game | POST /games
    @PostMapping("games")
    public String addNewGame(@RequestBody @Valid NewGame newGame) throws UserException {
        if (CurrentUser.getRoleOfCurrentUser().equals("ADMIN")) {
            GameEntity game = new GameEntity();
            if (gameService.findByGameName(newGame.getGameName()) == null) {
                game.setGameName(newGame.getGameName());
                gameService.saveGame(game);
                log.info("New game added");
                return "New game added";
            } else throw new UserException("That game already exists");
        } else throw new UserException("You are not the admin");
    }

    // to update a game | PUT /games/:id
    @PutMapping("games/{id}")
    public String editTheGame(@PathVariable Integer id, @RequestBody @Valid NewGame newGame) throws UserException {
        if (CurrentUser.getRoleOfCurrentUser().equals("ADMIN")) {
            GameEntity game = new GameEntity();
            if (gameService.findById(id) != null) {
                game.setId(id);
                game.setGameName(newGame.getGameName());
                gameService.saveGame(game);
                log.info("The game edited");
                return "The game edited";
            } else throw new UserException("There is no game with such id");
        } else throw new UserException("You are not the admin");
    }
}

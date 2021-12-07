package com.javamaster.springsecurityjwt.controller;

import com.javamaster.springsecurityjwt.entity.CurrentUser;
import com.javamaster.springsecurityjwt.entity.GameEntity;
import com.javamaster.springsecurityjwt.entity.GameObjectEntity;
import com.javamaster.springsecurityjwt.service.GameService;
import com.javamaster.springsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {

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
    public String addNewGame(@RequestBody @Valid NewGame newGame) {
        if (CurrentUser.getRoleOfCurrentUser().equals("ADMIN")) {
            GameEntity game = new GameEntity();
            if (gameService.findByGameName(newGame.getGameName()) == null) {
                game.setGameName(newGame.getGameName());
                gameService.saveGame(game);
                return "New game added";
            } else return "That game already exists";
        } else return "You are not the admin";
    }

    // to update a game | PUT /games/:id
    @PutMapping("games/{id}")
    public String editTheGame(@PathVariable Integer id, @RequestBody @Valid NewGame newGame) {
        if (CurrentUser.getRoleOfCurrentUser().equals("ADMIN")) {
            GameEntity game = new GameEntity();
            if (gameService.findById(id) != null) {
                game.setId(id);
                game.setGameName(newGame.getGameName());
                gameService.saveGame(game);
                return "The game edited";
            } else return "There is no game with such id";
        } else return "You are not the admin";
    }
}

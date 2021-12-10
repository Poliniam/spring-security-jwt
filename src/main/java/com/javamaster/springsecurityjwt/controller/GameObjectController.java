package com.javamaster.springsecurityjwt.controller;


import com.javamaster.springsecurityjwt.entity.*;
import com.javamaster.springsecurityjwt.exceptions.UserException;
import com.javamaster.springsecurityjwt.service.CommentService;
import com.javamaster.springsecurityjwt.service.GameObjectService;
import com.javamaster.springsecurityjwt.service.GameService;
import com.javamaster.springsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class GameObjectController {

    @Autowired
    private GameObjectService gameObjectService;
    @Autowired
    private GameService gameService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    private CurrentUser currentUserNow;

    java.util.logging.Logger log = java.util.logging.Logger.getLogger(GameController.class.getName());

    //to create new game object
    @PostMapping("/object")
    public String createGameObject(@RequestBody @Valid NewForm gameObject) throws UserException {
        if ((CurrentUser.getRoleOfCurrentUser()).equals("ROLE_USER")) {
            Date date = new Date();
            GameObjectEntity gameOb = new GameObjectEntity();
            if (gameObjectService.findByTitle(gameObject.getTitle()) == null) {
                gameOb.setTitle(gameObject.getTitle());
                gameOb.setText(gameObject.getText());
                UserEntity currentUser = userService.findById(currentUserNow.getIdOfCurrentUser());
                gameOb.setAuthor_id(currentUser);
                gameOb.setStatus(false);
                gameOb.setCreatedAt(date);
                GameEntity game = (gameService.findByGameName(gameObject.getGameName()));
                gameOb.setGame_id(game);
                gameObjectService.saveRequest(gameOb);
                log.info("New game object added");
                return "New game object added";
            } else throw new UserException("Such request already exists");
        }
        else throw new UserException("Admin can't add new game object");
    }

    //to get all game objects
    @GetMapping("/object")
    public List<GameObjectEntity> getAllGameObjects(){
        List<GameObjectEntity> listOfObjects = new ArrayList<GameObjectEntity>();
        listOfObjects = gameObjectService.getAll();
        return listOfObjects;
    }

    // to get one definite game object
    @GetMapping("/object/{id}")
    public GameObjectEntity getTheGameObjects(@PathVariable String id){
        int gameObjectId = Integer.parseInt(id);
        GameObjectEntity gameObject = gameObjectService.findById(gameObjectId);
        return gameObject;
    }

    //to delete the game object ( the author has a right for that ) | DELETE /object/:id
   @DeleteMapping("/object/{id}")
    public String deleteTheGameObject(@PathVariable Integer id) throws UserException {
       if ((CurrentUser.getRoleOfCurrentUser()).equals("ROLE_USER")) {
           GameObjectEntity theGameObject = gameObjectService.findById(id);
           if (userService.findById(CurrentUser.getIdOfCurrentUser()).equals(theGameObject.getId())) {
               if (gameObjectService.findById(id) != null) {
                   //commentService.deleteAllByPostId(theGameObject);
                   gameObjectService.deleteById(id);
                   log.info("This is removed");
                   return "This is removed";
               } else throw new UserException ("There is no such game object");
           } else throw new UserException ("It's not your game object, so you can't delete it");
       }
       else throw new UserException ("Admin can't do it");
    }

    //to edit a game object | PUT /object/:id
    @PutMapping("/object/{id}")
    public String editTheGameObject(@PathVariable String id, @RequestBody @Valid NewForm newGameObject) throws UserException {
        int gameObjectId = Integer.parseInt(id);
        Date date = new Date();
        GameObjectEntity gameObject = new GameObjectEntity();
        GameObjectEntity presentGameObject = gameObjectService.findById(gameObjectId);
        if ((CurrentUser.getRoleOfCurrentUser()).equals("ROLE_USER")) {
            if (userService.findById(CurrentUser.getIdOfCurrentUser()).equals(presentGameObject.getId())) {
                if (gameObjectService.findById(gameObjectId) != null) {
                    gameObject.setId(gameObjectId);
                    gameObject.setUpdatedAt(date);
                    GameEntity game = (gameService.findByGameName(newGameObject.getGameName()));
                    gameObject.setGame_id(game);
                    gameObject.setTitle(newGameObject.getTitle());
                    gameObject.setText(newGameObject.getText());
                    gameObject.setCreatedAt(presentGameObject.getCreatedAt());
                    gameObject.setStatus(presentGameObject.getStatus());
                    gameObject.setAuthor_id(presentGameObject.getAuthor_id());
                    gameObjectService.saveRequest(gameObject);
                    log.info("The game object edited");
                    return "The game object edited";
                } else throw new UserException ("There is no such game object");
            } else throw new UserException ("It's not your game object, so you can't edit it");
        } else throw new UserException ("Admin can't do it");

    }

    //to get game objects of an authorised user | GET /my
    @GetMapping("/my")
    public List<GameObjectEntity> showAuthorizedUserGameObjects(){
        UserEntity authorizedUser = userService.findById(CurrentUser.getIdOfCurrentUser());
        List<GameObjectEntity> firstList = gameObjectService.getAll();
        ArrayList<GameObjectEntity> secondList = new ArrayList<GameObjectEntity>();
        for (GameObjectEntity gameObject : firstList) {
            if ((gameObject.getAuthor_id()).equals(authorizedUser)){
                secondList.add(gameObject);
            }
        }
        return secondList;
    }

}

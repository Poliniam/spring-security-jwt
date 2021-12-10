package com.javamaster.springsecurityjwt.controller;

import com.javamaster.springsecurityjwt.entity.CurrentUser;
import com.javamaster.springsecurityjwt.entity.GameEntity;
import com.javamaster.springsecurityjwt.entity.GameObjectEntity;
import com.javamaster.springsecurityjwt.entity.UserEntity;
import com.javamaster.springsecurityjwt.exceptions.UserException;
import com.javamaster.springsecurityjwt.service.CommentService;
import com.javamaster.springsecurityjwt.service.GameObjectService;
import com.javamaster.springsecurityjwt.service.GameService;
import com.javamaster.springsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

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
    static Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("D:\\spring-security-jwt-master\\src\\main\\resources\\log.config")){
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(GameObjectController.class.getName());
        }catch (Exception ignore){
            ignore.printStackTrace();
        }
    }


    @PostMapping("/user/object")
    public String createGameObject(@RequestBody @Valid NewForm gameObject) throws UserException {
            Date date = new Date();
            GameObjectEntity gameOb = new GameObjectEntity();
            if(CurrentUser.getIdOfCurrentUser()!= null) {
                if (gameObject.getGameName() != null && gameObject.getTitle() != null && gameObject.getText() != null) {
                    if (gameObjectService.findByTitle(gameObject.getTitle()) == null) {
                        gameOb.setTitle(gameObject.getTitle());
                        gameOb.setText(gameObject.getText());
                        UserEntity currentUser = userService.findById(CurrentUser.getIdOfCurrentUser());
                        gameOb.setAuthor_id(currentUser);
                        gameOb.setStatus(false);
                        gameOb.setCreatedAt(date);
                        GameEntity game = (gameService.findByGameName(gameObject.getGameName()));
                        gameOb.setGame_id(game);
                        gameObjectService.saveRequest(gameOb);
                        LOGGER.log(Level.INFO,"New game object added");
                        return "New game object added";
                    } else throw new UserException("Such game object already exists");
                } else throw new UserException("Write all data, please");
            }
            else throw new UserException("Please do an authentication");
    }

    @GetMapping("/object")
    public List<GameObjectEntity> getAllGameObjects() throws UserException {
        List<GameObjectEntity> listOfObjects = new ArrayList<GameObjectEntity>();
        List<GameObjectEntity> resultList = new ArrayList<GameObjectEntity>();
        listOfObjects = gameObjectService.getAll();
        if(listOfObjects.size()!=0) {
            for (GameObjectEntity gameObject : listOfObjects) {
                if (gameObject.getStatus() == true) {
                    resultList.add(gameObject);
                }
            }
            if(resultList.size()!=0) {
                return resultList;
            }
            else throw new UserException ("There is no game objects");
        }
          else throw new UserException ("There is no game objects");
    }

    @GetMapping("/object/{id}")
    public GameObjectEntity getTheGameObjects(@PathVariable Integer id) throws UserException {
        GameObjectEntity gameObject = gameObjectService.findById(id);
        if(gameObject!= null) {
            if(gameObject.getStatus()==true) {
                return gameObject;
            }
            else throw new UserException ("There is no such game object");
        }
        else throw new UserException ("There is no such game object");
    }

    @DeleteMapping("/user/object/{id}")
    public String deleteTheGameObject(@PathVariable Integer id) throws UserException {
        if (CurrentUser.getIdOfCurrentUser() != null) {
            GameObjectEntity theGameObject = gameObjectService.findById(id);
            if (theGameObject != null) {
            if (userService.findById(CurrentUser.getIdOfCurrentUser()).equals(theGameObject.getAuthor_id())) {
                    commentService.deleteAllByPostId(theGameObject);
                    gameObjectService.deleteById(id);
                    return "This is removed";
            }
            else throw new UserException ("It's not your game object, so you can't delete it");
            }
            else  throw new UserException ("There is no such game object");
        }
        else  throw new UserException ("Please do an authentication");
    }

    @PutMapping("/user/object/{id}")
    public String editTheGameObject(@PathVariable String id, @RequestBody @Valid NewForm newGameObject) throws UserException {
        int gameObjectId = Integer.parseInt(id);
        Date date = new Date();
        GameObjectEntity gameObject = gameObjectService.findById(gameObjectId);
        if (gameObject != null) {
            if (CurrentUser.getIdOfCurrentUser() != null) {
                if (userService.findById(CurrentUser.getIdOfCurrentUser()).equals(gameObject.getAuthor_id())) {
                    if (gameObjectService.findById(gameObjectId) != null) {
                        gameObject.setId(gameObjectId);
                        gameObject.setUpdatedAt(date);
                        GameEntity game = (gameService.findByGameName(newGameObject.getGameName()));
                        gameObject.setGame_id(game);
                        gameObject.setTitle(newGameObject.getTitle());
                        gameObject.setText(newGameObject.getText());
                        gameObjectService.saveRequest(gameObject);

                        LOGGER.log(Level.INFO,"The game object edited");
                        return "The game object edited";
                    }
                    else throw new UserException("There is no such game object");
                }
                else throw new UserException("It's not your game object, so you can't edit it");
            }
            else throw new UserException("Please do an authentication");
        }
        else throw new UserException("There is no such game object");
    }

    @GetMapping("/user/my")
    public List<GameObjectEntity> showAuthorizedUserGameObjects() throws UserException {
        if (CurrentUser.getIdOfCurrentUser() != null) {
            UserEntity authorizedUser = userService.findById(CurrentUser.getIdOfCurrentUser());
            List<GameObjectEntity> firstList = gameObjectService.getAll();
            ArrayList<GameObjectEntity> secondList = new ArrayList<GameObjectEntity>();
            for (GameObjectEntity gameObject : firstList) {
                if ((gameObject.getAuthor_id()).equals(authorizedUser)) {
                    if(gameObject.getStatus()== true) {
                        secondList.add(gameObject);
                    }
                }
            }
            if(secondList.size()!=0) {
                return secondList;
            }
            else throw new UserException ("There is no game objects");
        }
        else throw new UserException("Please do an authentication");
    }

    @GetMapping("/filterByGames/{gameName}")
    public ArrayList<GameObjectEntity> filterByGames(@PathVariable String gameName) throws UserException {
        GameEntity game = gameService.findByGameName(gameName);
        if(game != null) {
            ArrayList<GameObjectEntity> resultList = new ArrayList<GameObjectEntity>();
            List<GameObjectEntity> gameObjectList = getAllGameObjects();
            if(gameObjectList.size()!=0 ) {
                for (GameObjectEntity gameObject : gameObjectList) {
                    if (gameObject.getGame_id().equals(game) && gameObject.getStatus() == true) {
                        resultList.add(gameObject);
                    }
                }
                return resultList;
            }
            else throw new UserException("No game objects");
        }
        else throw new UserException("No such game");
    }

}

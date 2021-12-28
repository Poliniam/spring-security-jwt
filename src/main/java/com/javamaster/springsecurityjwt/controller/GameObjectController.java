package com.javamaster.springsecurityjwt.controller;

import com.javamaster.springsecurityjwt.entity.*;
import com.javamaster.springsecurityjwt.exceptions.UserException;
import com.javamaster.springsecurityjwt.service.CommentService;
import com.javamaster.springsecurityjwt.service.GameObjectService;
import com.javamaster.springsecurityjwt.service.GameService;
import com.javamaster.springsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Controller
public class GameObjectController {

    @Autowired
    private GameObjectService gameObjectService;
    @Autowired
    private GameService gameService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentController commContr;
    static Logger LOGGER;

    static {
        try (FileInputStream ins = new FileInputStream("D:\\spring-security-jwt-master\\src\\main\\resources\\log.config")) {
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(GameObjectController.class.getName());
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }


    @PostMapping("/fuser/object")
    public ModelAndView createGameObject(@Valid NewForm gameObject) throws UserException {
            Date date = new Date();
            GameObjectEntity gameOb = new GameObjectEntity();
            if (CurrentUser.getIdOfCurrentUser() != null) {
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
                        LOGGER.log(Level.INFO, "New game object added");
                        ModelAndView view = new ModelAndView();
                        view.setViewName("main");
                        List list = new ArrayList();
                        list = getAllGameObjects();
                        view.addObject("list", list);
                        return view;
                    } else throw new UserException("Such game object already exists");
                } else throw new UserException("Write all data, please");
            } else throw new UserException("Please do an authentication");
    }

    @GetMapping("/object")
    public List<GameObjectEntity> getAllGameObjects() throws UserException {
        List<GameObjectEntity> listOfObjects = new ArrayList<GameObjectEntity>();
        List<GameObjectEntity> resultList = new ArrayList<GameObjectEntity>();
        listOfObjects = gameObjectService.getAll();
        if (listOfObjects.size() != 0) {
            for (GameObjectEntity gameObject : listOfObjects) {
                if (gameObject.getStatus() == true) {
                    resultList.add(gameObject);
                }
            }
            if (resultList.size() != 0) {
                return resultList;
            } else throw new UserException("There is no game objects");
        } else throw new UserException("There is no game objects");
    }

    @GetMapping("/object/{id}")
    public GameObjectEntity getTheGameObjects(@PathVariable Integer id) throws UserException {
        GameObjectEntity gameObject = gameObjectService.findById(id);
        if (gameObject != null) {
            if (gameObject.getStatus() == true) {
                return gameObject;
            } else throw new UserException("There is no such game object");
        } else throw new UserException("There is no such game object");
    }

    @PostMapping("/user/object/delete")
    public ModelAndView deleteTheGameObject(@Valid NewForm gameObject) throws UserException {
        if (CurrentUser.getIdOfCurrentUser() != null) {
            GameObjectEntity theGameObject = gameObjectService.findByTitle(gameObject.getTitle());
            if (theGameObject != null) {
                if (userService.findById(CurrentUser.getIdOfCurrentUser()).equals(theGameObject.getAuthor_id())) {
                    commentService.deleteAllByPostId(theGameObject);
                    gameObjectService.deleteById(theGameObject.getId());
                    ModelAndView view = new ModelAndView();
                    view.setViewName("main");
                    List list = new ArrayList();
                    list = getAllGameObjects();
                    view.addObject("list", list);
                    return view;
                } else throw new UserException("It's not your game object, so you can't delete it");
            } else throw new UserException("There is no such game object");
        } else throw new UserException("Please do an authentication");
    }

    @PostMapping("/user/object/update")
    public ModelAndView editTheGameObject(@Valid NewForm newGameObject) throws UserException {
        Date date = new Date();
        GameObjectEntity gameObject = gameObjectService.findById(newGameObject.getId());
        if (gameObject != null) {
            if (CurrentUser.getIdOfCurrentUser() != null) {
                if (userService.findById(CurrentUser.getIdOfCurrentUser()).equals(gameObject.getAuthor_id())) {
                    if (gameObjectService.findById(newGameObject.getId()) != null) {
                        gameObject.setId(newGameObject.getId());
                        gameObject.setUpdatedAt(date);
                        GameEntity game = (gameService.findByGameName(newGameObject.getGameName()));
                        gameObject.setGame_id(game);
                        gameObject.setTitle(newGameObject.getTitle());
                        gameObject.setText(newGameObject.getText());
                        gameObjectService.saveRequest(gameObject);
                        LOGGER.log(Level.INFO, "The game object edited");
                        ModelAndView view = new ModelAndView();
                        view.setViewName("main");
                        List list = new ArrayList();
                        list = getAllGameObjects();
                        view.addObject("list", list);
                        return view;
                    } else throw new UserException("There is no such game object");
                } else throw new UserException("It's not your game object, so you can't edit it");
            } else throw new UserException("Please do an authentication");
        } else throw new UserException("There is no such game object");
    }

    @GetMapping("/user/my")
    public List<GameObjectEntity> showAuthorizedUserGameObjects() throws UserException {
        if (CurrentUser.getIdOfCurrentUser() != null) {
            UserEntity authorizedUser = userService.findById(CurrentUser.getIdOfCurrentUser());
            List<GameObjectEntity> firstList = gameObjectService.getAll();
            ArrayList<GameObjectEntity> secondList = new ArrayList<GameObjectEntity>();
            for (GameObjectEntity gameObject : firstList) {
                if ((gameObject.getAuthor_id()).equals(authorizedUser)) {
                    if (gameObject.getStatus() == true) {
                        secondList.add(gameObject);
                    }
                }
            }
            if (secondList.size() != 0) {
                return secondList;
            } else throw new UserException("There is no game objects");
        } else throw new UserException("Please do an authentication");
    }

    @GetMapping("/filterByGames/{gameName}")
    public ArrayList<GameObjectEntity> filterByGames(@PathVariable String gameName) throws UserException {
        GameEntity game = gameService.findByGameName(gameName);
        if (game != null) {
            ArrayList<GameObjectEntity> resultList = new ArrayList<GameObjectEntity>();
            List<GameObjectEntity> gameObjectList = getAllGameObjects();
            if (gameObjectList.size() != 0) {
                for (GameObjectEntity gameObject : gameObjectList) {
                    if (gameObject.getGame_id().equals(game) && gameObject.getStatus() == true) {
                        resultList.add(gameObject);
                    }
                }
                return resultList;
            } else throw new UserException("No game objects");
        } else throw new UserException("No such game");
    }

    @GetMapping("/traderAccount")
    @Retryable(value = UserException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ModelAndView traderAccount() throws UserException {
        if(CurrentUser.getRoleOfCurrentUser().equals("ROLE_USER")) {
            ModelAndView view = new ModelAndView();
            ArrayList<CommentEntity> commList = new ArrayList<>();
            commList = commContr.getTraderComments(CurrentUser.getIdOfCurrentUser());
            Integer rating = commContr.getTraderRating(CurrentUser.getIdOfCurrentUser());
            view.setViewName("traderAccount");
            view.addObject("commList", commList);
            view.addObject("rating", rating);
            return view;
        }
        else throw new UserException("You are not a trader");
    }

    @Recover
    private ModelAndView recoverGameObject(Throwable throwable){
        ModelAndView view = new ModelAndView();
        view.setViewName("gameObjectError");
        System.out.println(throwable.getMessage());
        return view;
    }
}

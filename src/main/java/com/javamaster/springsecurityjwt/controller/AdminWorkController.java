package com.javamaster.springsecurityjwt.controller;

import com.javamaster.springsecurityjwt.entity.CommentEntity;
import com.javamaster.springsecurityjwt.entity.GameObjectEntity;
import com.javamaster.springsecurityjwt.exceptions.UserException;
import com.javamaster.springsecurityjwt.service.CommentService;
import com.javamaster.springsecurityjwt.service.GameObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AdminWorkController {

    @Autowired
    GameObjectService gameObjectService;

    @Autowired
    CommentService commentService;

    @GetMapping("/admin/objects")
    public List<GameObjectEntity> getAllGameObjects() throws UserException {
            List<GameObjectEntity> listOfObjects = new ArrayList<GameObjectEntity>();
            listOfObjects = gameObjectService.getAll();
            if(listOfObjects.size() == 0){
                throw new UserException("There is no game objects");
            }
            else return listOfObjects;
    }

    @GetMapping("/admin/objects/{id}")
    public GameObjectEntity approveTheGameObjects(@PathVariable Integer id) throws UserException {
            GameObjectEntity gameObject = gameObjectService.findById(id);
            if (gameObject == null){
                throw new UserException("There is no such game object");
            }
            else {
                gameObject.setStatus(true);
                gameObjectService.saveRequest(gameObject);
                return gameObject;
            }
    }

    @GetMapping("/admin/comments")
    public ArrayList<CommentEntity> getAllComments() throws UserException {
        ArrayList<CommentEntity> list = commentService.getAll();
        if(list.size()!=0) {
            return list;
        }
        throw new UserException("There is no comments");
    }

    @PutMapping("/admin/comments/{id}")
    public CommentEntity approveTheComment(@PathVariable Integer id) throws UserException {
        CommentEntity commentEntity = commentService.findById(id);
        if (commentEntity != null){
            if(commentEntity.getApproved()==false) {
                commentEntity.setApproved(true);
                commentService.saveComment(commentEntity);
                return commentEntity;
            }
            else throw new UserException("It's already true");
        }
        else throw new UserException("There is no such comment");
    }
}

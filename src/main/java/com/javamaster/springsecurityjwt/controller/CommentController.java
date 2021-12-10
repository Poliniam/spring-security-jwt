package com.javamaster.springsecurityjwt.controller;

import com.javamaster.springsecurityjwt.entity.*;
import com.javamaster.springsecurityjwt.service.CommentService;
import com.javamaster.springsecurityjwt.service.GameObjectService;
import com.javamaster.springsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@RestController
public class CommentController {
    @Autowired
    private UserService userService;

    @Autowired
    private GameObjectService gameObjectService;
    @Autowired
    private CommentService commentService;


    // to add a new comment
    @PostMapping("/articles/{id}/comments")
    public String createComment(@RequestBody @Valid NewComment newComment, @PathVariable String id) {
        int articleId = Integer.parseInt(id);
        Date date = new Date();
        CommentEntity comment = new CommentEntity();
        if(gameObjectService.findById(articleId)!=null){
            comment.setMessage(newComment.getMessage());
            comment.setRate(newComment.getRate());
            comment.setApproved(false);
            comment.setCreatedAt(date);
            UserEntity currentUser = userService.findById(CurrentUser.getIdOfCurrentUser());
            comment.setAuthor_id(currentUser);
            GameObjectEntity gameObject = (gameObjectService.findById(articleId));
            comment.setPost_id(gameObject);
            commentService.saveComment(comment);

            return "OK";
        }
        else return "There is no such post";
    }

    // to get all comments of the author | GET /users/:id/comments
   @GetMapping("/users/{id}/comments")
    public ArrayList<CommentEntity> getTraderComments(@PathVariable Integer id){
        UserEntity user = userService.findById(id);
        ArrayList<Integer> list =gameObjectService.getByPostId(user);
        ArrayList<GameObjectEntity> gameObjectEntities = new ArrayList<>();
        ArrayList<Integer> listOfEntities = new ArrayList<>();
        ArrayList<CommentEntity> commentEntities = new ArrayList<>();
        GameObjectEntity gameObjectEntity=new GameObjectEntity();

        for (Integer x: list) {
            gameObjectEntities.add(gameObjectService.findById(x));
            //commentService.getByPost_id(gameObjectEntity);
        }
        for (GameObjectEntity entity: gameObjectEntities){
            int size=0;
            ArrayList<Integer> listtt=new ArrayList<>();

           /* listtt=commentService.getByPost_id(entity);*/
            listOfEntities.add(listtt.get(0));

        }

    /*    for (Integer commentId: listOfEntities){
             Optional<CommentEntity> comment=commentService.getById(commentId);
             commentEntities.add(comment.get());
        }*/
        return commentEntities;
    }

    // to get the comment | GET /users/:id/comments/:id
    @GetMapping("/users/{id}/comments/{idComm}")
    public CommentEntity getOneTraderComment(@PathVariable Integer id, @PathVariable Integer idComm){
        ArrayList<CommentEntity> listOfComments = getTraderComments(id);
        for (CommentEntity comment : listOfComments){
            if(comment.getId() == idComm){
                return comment;
            }
            else {
                return null;
            }
        }
        return null;
    }

    // to delete a comment ( the author has right for that ) | DELETE /users /:id/comments/:id
    @DeleteMapping("/users/{id}/comments/{idComm}")
    public void deleteOneComment(@PathVariable Integer id, @PathVariable Integer idComm){
        if(getOneTraderComment(id, idComm) != null){
            CommentEntity comment = getOneTraderComment(id, idComm);
           // commentService.deleteById(comment.getId());
        }
    }

    // to update the comment | PUT /articles/:id/comments
    @PutMapping("/users/{id}/comments/{idComm}")
    public void updateOneComment(@RequestBody @Valid NewComment writeNewComment, @PathVariable Integer id, @PathVariable Integer idComm){
        if(getOneTraderComment(id, idComm) != null){
            CommentEntity presentComment = getOneTraderComment(id, idComm);
            CommentEntity newComment = new CommentEntity();
            newComment.setId(presentComment.getId());
            newComment.setMessage(newComment.getMessage());
            newComment.setCreatedAt(presentComment.getCreatedAt());
            newComment.setAuthor_id(presentComment.getAuthor_id());
            newComment.setPost_id(presentComment.getPost_id());
            newComment.setRate(presentComment.getRate());
            newComment.setApproved(false);
            commentService.saveComment(newComment);
        }
    }

}

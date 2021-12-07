package com.javamaster.springsecurityjwt.controller;

import com.javamaster.springsecurityjwt.entity.CommentEntity;
import com.javamaster.springsecurityjwt.entity.GameEntity;
import com.javamaster.springsecurityjwt.entity.GameObjectEntity;
import com.javamaster.springsecurityjwt.service.CommentService;
import com.javamaster.springsecurityjwt.service.GameObjectService;
import com.javamaster.springsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

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
            GameObjectEntity gameObject = (gameObjectService.findById(articleId));
            comment.setPost_id(gameObject);
            commentService.saveComment(comment);

            return "OK";
        }
        else return "There is no such post";
    }

    // to get all comments of the author | GET /users/:id/comments
    /*@GetMapping("/users/{id}/comments")
    public List<CommentEntity> getTraderComments(@PathVariable Integer id){
    }*/

    // to get the comment | GET /users/:id/comments/:id

    // to delete a comment ( the author has right for that ) | DELETE /users /:id/comments/:id

    // to update the comment

}

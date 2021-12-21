package com.javamaster.springsecurityjwt.controller;

import com.javamaster.springsecurityjwt.entity.CommentEntity;
import com.javamaster.springsecurityjwt.entity.CurrentUser;
import com.javamaster.springsecurityjwt.entity.GameObjectEntity;
import com.javamaster.springsecurityjwt.entity.UserEntity;
import com.javamaster.springsecurityjwt.exceptions.UserException;
import com.javamaster.springsecurityjwt.service.CommentService;
import com.javamaster.springsecurityjwt.service.GameObjectService;
import com.javamaster.springsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class CommentController {
    @Autowired
    private UserService userService;

    @Autowired
    private GameObjectService gameObjectService;
    @Autowired
    private CommentService commentService;

    @ResponseBody
    @PostMapping("/articles/{id}/comments")
    public ModelAndView createComment(@Valid NewComment newComment, @PathVariable Integer id) throws UserException {
        Date date = new Date();
        CommentEntity comment = new CommentEntity();
            if(newComment.getMessage()!=null && newComment.getRate()!=null) {
                if (gameObjectService.findById(id) != null && (gameObjectService.findById(id)).getStatus() == true) {
                    comment.setMessage(newComment.getMessage());
                    comment.setRate(newComment.getRate());
                    comment.setApproved(false);
                    comment.setCreatedAt(date);
                    GameObjectEntity gameObject = (gameObjectService.findById(id));
                    comment.setPost_id(gameObject);
                    commentService.saveComment(comment);
                    ModelAndView view = new ModelAndView();
                    view.setViewName("commentSended");
                    return view;
                }
                else throw new UserException("There is no such game object");
            }
            else throw new UserException("Write all data please");
    }

   @GetMapping("/users/{id}/comments")
    public ArrayList<CommentEntity> getTraderComments(@PathVariable Integer id){
         ArrayList<CommentEntity> resultList = new ArrayList<>();
        UserEntity user = userService.findById(id);
        ArrayList<GameObjectEntity> list =gameObjectService.getByPostId(user);
        for (GameObjectEntity entity: list){
            for(CommentEntity comment: commentService.getByPost_id(entity)){
                if(comment.getApproved()==true) {
                    resultList.add(comment);
                }
            }
        }
        return resultList;
    }

    @GetMapping("/users/{id}/comments/{idComm}")
    public CommentEntity getOneTraderComment(@PathVariable Integer id, @PathVariable Integer idComm){
        ArrayList<CommentEntity> listOfComments = getTraderComments(id);
        for (CommentEntity comment : listOfComments){
            if(comment.getId() == idComm){
                if(comment.getApproved()==true) {
                    return comment;
                }
            }
            else {
                return null;
            }
        }
        return null;
    }

    @DeleteMapping("/admin/users/{id}/comments/{idComm}")
    public CommentEntity deleteOneComment(@PathVariable Integer id, @PathVariable Integer idComm){
        CommentEntity comment = getOneTraderComment(id, idComm);
        if(comment!= null){
            commentService.deleteById(comment.getId());
            return comment;
        }
        return null;
    }

    @PutMapping("/users/{id}/comments/{idComm}")
    public CommentEntity updateOneComment(@RequestBody @Valid NewComment newComment, @PathVariable Integer id, @PathVariable Integer idComm){
        if(getOneTraderComment(id, idComm) != null){
            CommentEntity presentComment = getOneTraderComment(id, idComm);
            presentComment.setMessage(newComment.getMessage());
            presentComment.setApproved(false);
            commentService.saveComment(presentComment);
            return presentComment;
        }
        return null;
    }

    @GetMapping("/rating/{id}")
    public Integer getTraderRating (@PathVariable Integer id) throws UserException {
        ArrayList<CommentEntity> resultList = getTraderComments(id);
        int sum =0;
        if(resultList.size()!=0) {
            for (CommentEntity comment : resultList) {
                sum += comment.getRate();
            }
            int result = sum / resultList.size();
            return result;
        }
        else throw new UserException("This trader don't have a rating.");
    }
}

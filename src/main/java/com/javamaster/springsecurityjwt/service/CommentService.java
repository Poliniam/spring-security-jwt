package com.javamaster.springsecurityjwt.service;

import com.javamaster.springsecurityjwt.entity.CommentEntity;
import com.javamaster.springsecurityjwt.entity.GameObjectEntity;
import com.javamaster.springsecurityjwt.repository.CommentEntityRepository;
import com.javamaster.springsecurityjwt.repository.GameObjectEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentEntityRepository commentEntityRepository;

    public boolean saveComment(CommentEntity commEntity) {
        commentEntityRepository.save(commEntity);
        return true;
    }

    public void deleteById(Integer id){
        commentEntityRepository.deleteById(id);
    }

    public void deleteAllByPostId(GameObjectEntity gameObject){
        ArrayList<Integer> list = commentEntityRepository.getAllByPost_id(gameObject);
        for (Integer id : list )
        {
            deleteById(id);
        }
    }

}

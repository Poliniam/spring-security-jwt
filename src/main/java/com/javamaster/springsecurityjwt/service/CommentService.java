package com.javamaster.springsecurityjwt.service;

import com.javamaster.springsecurityjwt.entity.CommentEntity;
import com.javamaster.springsecurityjwt.entity.GameObjectEntity;
import com.javamaster.springsecurityjwt.repository.CommentEntityRepository;
import com.javamaster.springsecurityjwt.repository.GameEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentEntityRepository commentEntityRepository;

    @Autowired
    private GameEntityRepository gameEntityRepository;

    public boolean saveComment(CommentEntity commEntity) {
        commentEntityRepository.save(commEntity);
        return true;
    }

    public void deleteById(Integer id){
        commentEntityRepository.deleteById(id);
    }

    public void deleteAllByPostId(GameObjectEntity gameObject){
        ArrayList<Integer> list = gameEntityRepository.getAllByPost_id(gameObject);
        for (Integer id : list )
        {
            if(list.size()!=0) {
                deleteById(id);
            }
        }
    }

    public ArrayList<CommentEntity> getByPost_id(GameObjectEntity gameObject){
        return commentEntityRepository.getAllByPost_id(gameObject);
    }

    public ArrayList<CommentEntity> getAll(){
        return commentEntityRepository.getAll();
    }

    public CommentEntity findById(Integer id){
        return commentEntityRepository.getById(id);
    }
}

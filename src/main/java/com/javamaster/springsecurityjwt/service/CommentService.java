package com.javamaster.springsecurityjwt.service;

import com.javamaster.springsecurityjwt.entity.CommentEntity;
import com.javamaster.springsecurityjwt.entity.GameObjectEntity;
import com.javamaster.springsecurityjwt.repository.CommentEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentEntityRepository commentEntityRepository;

    public boolean saveComment(CommentEntity commEntity) {
        commentEntityRepository.save(commEntity);
        return true;
    }

    /*public void deleteById(Integer id){
        commentEntityRepository.deleteById(id);
    }

    /*public void deleteAllByPostId(GameObjectEntity gameObject){
        ArrayList<Integer> list =new ArrayList<>();
        list.add(getByPost_id(gameObject));
        for (Integer id : list )
        {
            deleteById(id);
        }
    } */
    //тут на интеджер
    /*public CommentEntity getByPost_id(GameObjectEntity gameObject){
        return commentEntityRepository.getAllByPost_id()
    } */

    public Optional<CommentEntity> getById(Integer id){
        return commentEntityRepository.findById(id);
    }
}

package com.javamaster.springsecurityjwt.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "gameobject_table")
@Data
public class GameObjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;

    @Column
    private String text;

    @Column
    private Boolean status;

    @Column
    @Type(type="date")
    private Date createdAt;

    @Column
    @Type(type="date")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author_id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity game_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isApproved() {
        return status;
    }

    public void setApproved(boolean approved) {
        this.status = approved;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserEntity getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(UserEntity author_id) {
        this.author_id = author_id;
    }

    public GameEntity getGame_id() {
        return game_id;
    }

    public void setGame_id(GameEntity game_id) {
        this.game_id = game_id;
    }
}

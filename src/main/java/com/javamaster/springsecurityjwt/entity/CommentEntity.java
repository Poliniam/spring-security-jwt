package com.javamaster.springsecurityjwt.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comment_table")
@Data
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @Type(type="date")
    private Date createdAt;

    @Column
    private String message;

    @Column
    private boolean approved;

    @Column
    private Integer rate;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author_id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private GameObjectEntity post_id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public UserEntity getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(UserEntity author_id) {
        this.author_id = author_id;
    }
}

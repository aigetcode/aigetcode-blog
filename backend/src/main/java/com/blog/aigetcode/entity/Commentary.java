package com.blog.aigetcode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "commentary")
public class Commentary extends SuperEntity {

    @Column(nullable = false)
    private String name;

    @Column(length = 1000, nullable = false)
    private String comment;

    @Column(nullable = false)
    private Long createAt;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    private Post post;

    public Commentary() {
    }

    public Commentary(String name, String comment, Long createAt, Post post) {
        this.name = name;
        this.comment = comment;
        this.createAt = createAt;
        this.post = post;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}

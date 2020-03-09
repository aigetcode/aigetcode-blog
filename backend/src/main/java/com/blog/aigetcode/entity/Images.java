package com.blog.aigetcode.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@javax.persistence.Entity
public class Images extends SuperEntity {

    @ManyToOne
    @JoinColumn(name = "post_id")
    @Fetch(value = FetchMode.SELECT)
    private Post post;

    private String nameFile;

    public Images() {
    }

    public Images(String nameFile, Post post) {
        this.nameFile = nameFile;
        this.post = post;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }
}

package com.blog.aigetcode.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "post")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Post extends SuperEntity {

    @Fetch(value = FetchMode.SELECT)
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
            name = "postList",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") }
    )
    private List<Category> categoriesPosts;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    private List<Images> images;

    @Column(columnDefinition="varchar")
    private String text;

    @Column(columnDefinition="varchar(350)")
    private String previewPost;

    private String title;
    private Long createAt;
    private String image;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
    private List<Commentary> commentaries = new LinkedList<>();


    public Post() {
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Category> getCategoriesPosts() {
        return categoriesPosts;
    }

    public void setCategoriesPosts(List<Category> categoriesPosts) {
        this.categoriesPosts = categoriesPosts;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPreviewPost() {
        return previewPost;
    }

    public void setPreviewPost(String previewPost) {
        this.previewPost = previewPost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

    public List<Commentary> getCommentaries() {
        return commentaries;
    }

    public void setCommentaries(List<Commentary> commentaries) {
        this.commentaries = commentaries;
    }

    @Override
    public String toString() {
        return "Post{" +
                "author=" + author +
                ", categoriesPosts=" + categoriesPosts +
                ", images=" + images +
                ", text='" + text + '\'' +
                ", previewPost='" + previewPost + '\'' +
                ", title='" + title + '\'' +
                ", createAt=" + createAt +
                ", image='" + image + '\'' +
                ", commentaries=" + commentaries +
                '}';
    }
}

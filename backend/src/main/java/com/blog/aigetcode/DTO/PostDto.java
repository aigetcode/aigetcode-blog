package com.blog.aigetcode.DTO;

import com.blog.aigetcode.entity.Category;
import com.blog.aigetcode.entity.Post;

import java.util.List;

public class PostDto {
    private Long id;
    private String author;
    private String title;
    private String text;
    private List<Category> categoriesPosts;

    public PostDto() {
    }

    public PostDto(Post post) {
        this.id = post.getId();
        this.author = post.getAuthor().getFullName();
        this.title = post.getTitle();
        this.text = post.getText();
        this.categoriesPosts = post.getCategoriesPosts();
    }

    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public List<Category> getCategoriesPosts() {
        return categoriesPosts;
    }
}

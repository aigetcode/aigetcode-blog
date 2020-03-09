package com.blog.aigetcode.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category extends SuperEntity {

    @Fetch(value = FetchMode.SELECT)
    @ManyToMany(mappedBy = "categoriesPosts")
    @JsonBackReference(value="categoriesPosts")
    private List<Post> postList;

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

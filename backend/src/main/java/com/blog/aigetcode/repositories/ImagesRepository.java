package com.blog.aigetcode.repositories;

import com.blog.aigetcode.entity.Images;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImagesRepository extends CrudRepository<Images, Long> {

    @Query("select im from Images im where im.post.id = :postId")
    List<Images> findAllByPostId(Long postId);

}

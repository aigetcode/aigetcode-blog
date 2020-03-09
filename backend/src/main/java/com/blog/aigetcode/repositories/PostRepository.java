package com.blog.aigetcode.repositories;

import com.blog.aigetcode.entity.Category;
import com.blog.aigetcode.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    Page<Post> findPostsByTextLikeOrTitleLikeIgnoreCase(String text, String title, Pageable var1);
    Page<Post> findPostsByCategoriesPostsAndTitleLikeIgnoreCase(Category category, String text, Pageable var1);
    List<Post> findTop5ByOrderByCreateAtDesc();
    Long countAllBy();

    Long countAllByCreateAtBetween(Long startTime, Long endTime);
}

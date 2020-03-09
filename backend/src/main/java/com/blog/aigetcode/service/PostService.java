package com.blog.aigetcode.service;

import com.blog.aigetcode.DTO.PostDto;
import com.blog.aigetcode.entity.Commentary;
import com.blog.aigetcode.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto, MultipartFile mainImage, Principal principal);
    PostDto updatePost(PostDto postDto, MultipartFile mainImage, Principal principal);
    Page<Post> getPageNews(int numPage, String keyword, Long categoryId);
    List<Post> getRecentPost();
    Post getPostById(Long id);
    Long getCountPosts();
    List<Long> getCountPostsByMonth();
    void delete(Long id);
    void addCommentary(Long postId, String name, String comment);

}

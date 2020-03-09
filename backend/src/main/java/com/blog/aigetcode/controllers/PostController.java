package com.blog.aigetcode.controllers;

import com.blog.aigetcode.DTO.CommentaryDto;
import com.blog.aigetcode.DTO.EmailDto;
import com.blog.aigetcode.DTO.PostDto;
import com.blog.aigetcode.entity.Authority;
import com.blog.aigetcode.entity.Post;
import com.blog.aigetcode.service.MailService;
import com.blog.aigetcode.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private PostService newsService;
    private MailService mailService;

    @Autowired
    public PostController(PostService newsService, MailService mailService) {
        this.newsService = newsService;
        this.mailService = mailService;
    }

    @Secured({Authority.Role.Code.USER, Authority.Role.Code.ADMIN})
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<PostDto> createPost(@NonNull @RequestPart("post") PostDto post,
                                              @RequestPart(value = "file", required = false) MultipartFile image,
                                              Principal principal) {
        PostDto postDto = newsService.createPost(post, image, principal);
        return ResponseEntity.ok(postDto);
    }

    @Secured({Authority.Role.Code.USER, Authority.Role.Code.ADMIN})
    @PostMapping(value = "/update", consumes = {"multipart/form-data"})
    public ResponseEntity<PostDto> updatePost(@NonNull @RequestPart("post") PostDto post,
                                              @RequestPart(value = "file", required = false) MultipartFile image,
                                              Principal principal) {
        PostDto postDto = newsService.updatePost(post, image, principal);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping
    public ResponseEntity<Page<Post>> get(@RequestParam("page") int numPage,
                                          @RequestParam(value = "searchParam", required = false) String searchParam,
                                          @RequestParam(value = "category",  required = false) Long category) {
        Page<Post> news = newsService.getPageNews(numPage, searchParam, category);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Post>> getRecentPost() {
        List<Post> news = newsService.getRecentPost();
        return ResponseEntity.ok(news);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable("id") Long id) {
        Post post = newsService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    @Secured(Authority.Role.Code.ADMIN)
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id) {
        newsService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email")
    public ResponseEntity<?> sendEmail(@RequestBody @Valid EmailDto emailDTO) {
        mailService.sendEmail(emailDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comment/{postId}")
    public ResponseEntity<?> addComment(@PathVariable Long postId, @RequestBody @Valid CommentaryDto commentaryDto) {
        newsService.addCommentary(postId, commentaryDto.getName(), commentaryDto.getComment());
        return ResponseEntity.ok().build();
    }

}

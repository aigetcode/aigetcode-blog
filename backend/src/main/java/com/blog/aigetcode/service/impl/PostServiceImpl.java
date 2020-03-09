package com.blog.aigetcode.service.impl;

import com.blog.aigetcode.DTO.PostDto;
import com.blog.aigetcode.entity.Category;
import com.blog.aigetcode.entity.Commentary;
import com.blog.aigetcode.entity.Images;
import com.blog.aigetcode.entity.Post;
import com.blog.aigetcode.entity.User;
import com.blog.aigetcode.exceptions.Check;
import com.blog.aigetcode.repositories.CategoryRepository;
import com.blog.aigetcode.repositories.CommentaryRepository;
import com.blog.aigetcode.repositories.ImagesRepository;
import com.blog.aigetcode.repositories.PostRepository;
import com.blog.aigetcode.repositories.UserRepository;
import com.blog.aigetcode.service.FileService;
import com.blog.aigetcode.service.PostService;
import com.blog.aigetcode.service.model.ImagesEntry;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    @Value("${server.servlet.context-path}")
    private String servletPath;

    private final int previewTextLength = 300;

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;
    private final ImagesRepository imagesRepository;
    private final CommentaryRepository commentaryRepository;

    private String localFileImagePath = "";

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, FileService fileService,
                           ImagesRepository imagesRepository, CategoryRepository categoryRepository,
                           CommentaryRepository commentaryRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.imagesRepository = imagesRepository;
        this.categoryRepository = categoryRepository;
        this.commentaryRepository = commentaryRepository;
    }

    @PostConstruct
    public void init() {
        this.localFileImagePath = servletPath + "/downloadFile/";
    }

    @Override
    public PostDto createPost(PostDto postDto, MultipartFile mainImage, Principal principal) {
        Check.required(postDto, "Required object not found");
        Check.required(postDto.getCategoriesPosts(), "Required categories not found");

        ImagesEntry updatedTextPost = fileService.changeContentUrlOnLocal(postDto.getText(), localFileImagePath);

        Post post = new Post();
        String preview = Jsoup.parse(updatedTextPost.getText()).text();
        if (preview.length() > previewTextLength) {
            preview = preview.substring(0, previewTextLength) + "...";
        }

        saveMainImage(post, mainImage);

        String email = principal.getName();
        Optional<User> user = getUser(email);
        user.ifPresent(post::setAuthor);

        post.setPreviewPost(preview);
        post.setCategoriesPosts(postDto.getCategoriesPosts());
        post.setText(updatedTextPost.getText());
        post.setTitle(postDto.getTitle());
        post.setCreateAt((new Date()).getTime());
        postRepository.save(post);
        if (!updatedTextPost.getImages().isEmpty()) {
            this.savePostImage(updatedTextPost.getImages(), post);
        }

        return new PostDto(post);
    }

    private Optional<User> getUser(String email) {
        Optional<User> user = Optional.empty();
        if (!StringUtils.isEmpty(email)) {
            user = Optional.ofNullable(userRepository.findByEmail(email));
        }
        return user;
    }

    @Override
    public PostDto updatePost(PostDto postDto, MultipartFile mainImage, Principal principal) {
        Check.required(postDto, "Required object not found");
        Optional<Post> optionalPost = this.postRepository.findById(postDto.getId());
        Post post = optionalPost.orElse(null);
        Check.exist(post, postDto.getId(), Post.class);

        ImagesEntry updatedTextPost = fileService.changeContentUrlOnLocal(postDto.getText(), localFileImagePath);

        if (!updatedTextPost.getText().equals(post.getText())) {
            String preview = Jsoup.parse(updatedTextPost.getText()).text();
            if (preview.length() > previewTextLength) {
                preview = preview.substring(0, previewTextLength) + "...";
            }
            post.setPreviewPost(preview);
        }

        if (!postDto.getCategoriesPosts().isEmpty()) {
            post.setCategoriesPosts(postDto.getCategoriesPosts());
        }

        saveMainImage(post, mainImage);
        post.setText(updatedTextPost.getText());
        post.setTitle(postDto.getTitle());
        postRepository.save(post);
        this.savePostImage(updatedTextPost.getImages(), post);

        return new PostDto(post);
    }

    private void savePostImage(List<String> imagesStringList, Post post) {
        List<Images> imagesList = createImagesObj(imagesStringList, post);
        String[] splitMainPostImage = post.getImage().split("/");
        Images mainImageObj = new Images(splitMainPostImage[splitMainPostImage.length - 1], post);
        imagesList.add(mainImageObj);
        imagesRepository.saveAll(imagesList);
    }

    private void saveMainImage(Post post, MultipartFile mainImage) {
        if (mainImage != null) {
            String fileNameImage = fileService.storeFile(mainImage);
            String relativePathForImage = this.localFileImagePath + fileNameImage;
            post.setImage(relativePathForImage);
        }
    }

    private List<Images> createImagesObj(List<String> imagesStringList, Post post) {
        List<Images> imagesList = new LinkedList<>();
        if (imagesStringList != null && !imagesStringList.isEmpty()) {
            for (String nameFile : imagesStringList) {
                Images images = new Images(nameFile, post);
                imagesList.add(images);
            }
        }
        return imagesList;
    }

    @Override
    public Page<Post> getPageNews(int numPage, String keyword, Long categoryId) {
        Page<Post> postsOnPage = null;
        Pageable pageableSort = PageRequest.of(numPage - 1, 10, Sort.Direction.DESC, "createAt");

        // todo: rework on hibernate and generics query for db !!!!!
        if (categoryId != null && categoryId != -1) {
            keyword = stringForQuery(keyword);
            Optional<Category> category = categoryRepository.findById(categoryId);
            if (category.isPresent()) {
                postsOnPage = postRepository.findPostsByCategoriesPostsAndTitleLikeIgnoreCase(category.get(), keyword, pageableSort);
            }
        } else if (!StringUtils.isEmpty(keyword)) {
            keyword = stringForQuery(keyword);
            postsOnPage = postRepository.findPostsByTextLikeOrTitleLikeIgnoreCase(keyword, keyword, pageableSort);
        } else {
            postsOnPage = postRepository.findAll(pageableSort);
        }
        return postsOnPage;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getRecentPost() {
        return postRepository.findTop5ByOrderByCreateAtDesc();
    }

    private String stringForQuery(String word) {
        return "%" + word + "%";
    }

    @Override
    @Transactional(readOnly = true)
    public Post getPostById(Long id) {
        Optional<Post> post = this.postRepository.findById(id);
        return post.orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountPosts() {
        return postRepository.countAllBy();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getCountPostsByMonth() {
        List<Long> countPostByMonth = new LinkedList<>();
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);

        for (int i = 0; i < 7; i++) {
            cal.set(Calendar.MONTH, month);
            if (month < 0) {
                int currentYear = cal.get(Calendar.YEAR);
                cal.set(Calendar.YEAR, currentYear - 1);
                cal.set(Calendar.MONTH, 11);
            }

            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            long startTime = cal.getTimeInMillis();

            int maxDayMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, maxDayMonth);
            cal.set(Calendar.HOUR, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            long endTime = cal.getTimeInMillis();

            long countByMonth = postRepository.countAllByCreateAtBetween(startTime, endTime);
            countPostByMonth.add(countByMonth);
            month--;
        }

        Collections.reverse(countPostByMonth);
        return countPostByMonth;
    }

    @Override
    public void delete(Long postId) {
        List<Images> postImages = imagesRepository.findAllByPostId(postId);
        for (Images image : postImages) {
            fileService.deleteFile(image.getNameFile());
        }
        postRepository.deleteById(postId);
    }

    public void addCommentary(Long postId, String name, String comment) {
        Optional<Post> optionalPost = this.postRepository.findById(postId);
        Post post = optionalPost.orElse(null);
        Check.exist(post, postId, Post.class);

        Date date = new Date();
        Commentary commentary = new Commentary(name, comment, date.getTime(), post);
        commentaryRepository.save(commentary);
    }

}

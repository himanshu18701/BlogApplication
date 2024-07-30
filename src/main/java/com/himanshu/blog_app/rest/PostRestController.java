package com.himanshu.blog_app.rest;


import com.himanshu.blog_app.entity.Post;
import com.himanshu.blog_app.entity.Tag;
import com.himanshu.blog_app.entity.User;
import com.himanshu.blog_app.service.PostService;
import com.himanshu.blog_app.service.TagService;
import com.himanshu.blog_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {
    private final PostService postService;
    private final UserService userService;
    private final TagService tagService;

    @Autowired
    public PostRestController(PostService postService, UserService userService, TagService tagService) {
        this.postService = postService;
        this.userService = userService;
        this.tagService = tagService;
    }

    @GetMapping()
    public List<Post> getAllPosts(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNumber,
                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                  @RequestParam(value = "sortOrder", defaultValue = "asc", required = false) String sortingOrder,
                                  @RequestParam(value = "sortField", defaultValue = "publishedAt", required = false) String sortingField,
                                  @RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "tags", required = false) List<Integer> tagIds,
                                  Model model) {
        Page<Post> posts;
        if (keyword != null && !keyword.isEmpty()) {
            posts = postService.searchPosts(keyword, pageNumber, pageSize, sortingField, sortingOrder);
            model.addAttribute("keyword", keyword);
        } else if (tagIds != null && !tagIds.isEmpty()) {
            List<Tag> selectedTags = tagService.findAllById(tagIds);
            posts = postService.getPostsByTags(selectedTags, pageNumber, pageSize, sortingField, sortingOrder);
            model.addAttribute("selectedTags", selectedTags);
        } else {
            posts = postService.getAllPosts(pageNumber, pageSize, sortingField, sortingOrder);
        }
        return posts.getContent();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable int id) {
        return postService.getPostById(id);
    }

    @PostMapping()
    public String savePost(@RequestBody Post post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.findUserByEmail(userEmail);
        post.setAuthor(user);
        post.setTags(post.getTags());
        postService.savePost(post);
        return "Success";
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable int id,@RequestBody Post post) {
        Post existingPost= postService.getPostById(id);
        existingPost.setTitle(post.getTitle());
        existingPost.setExcerpt(post.getExcerpt());
        existingPost.setContent(post.getContent());
        return postService.getPostById(id);
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable int id) {
        postService.deletePostById(id);
        return "Deleted Successfully";
    }
}
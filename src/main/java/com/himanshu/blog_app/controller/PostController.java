package com.himanshu.blog_app.controller;

import com.himanshu.blog_app.entity.Comment;
import com.himanshu.blog_app.entity.Post;
import com.himanshu.blog_app.entity.Tag;
import com.himanshu.blog_app.entity.User;
import com.himanshu.blog_app.service.PostService;
import com.himanshu.blog_app.service.UserService;
import com.himanshu.blog_app.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/posts")
public class PostController {

    private String role;
    private int userId;
    private final PostService postService;
    private final UserService userService;
    private final TagService tagService;

    @Autowired
    public PostController(PostService postService, UserService userService, TagService tagService) {
        this.postService = postService;
        this.userService = userService;
        this.tagService = tagService;
    }

    @GetMapping()
    public String getAllPosts(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNumber,
                              @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                              @RequestParam(value = "sortOrder", defaultValue = "asc", required = false) String sortingOrder,
                              @RequestParam(value = "sortField", defaultValue = "publishedAt", required = false) String sortingField,
                              @RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "tags", required = false) List<Integer> tagIds,
                              Model model) {
        Page<Post> posts;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        role = auth.getAuthorities().toString();
        List<Tag> allTags = tagService.getAllTags();
        if (role.contains("ROLE_ANONYMOUS")) {
            userId = 0;
        } else {
            userId = userService.findUserByEmail(auth.getName()).getId();
        }
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
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortOrder", sortingOrder);
        model.addAttribute("sortField", sortingField);
        model.addAttribute("role", role);
        model.addAttribute("tags", allTags);
        return "posts";
    }


    @GetMapping("/{id}")
    public String getPostById(@PathVariable int id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        model.addAttribute("comment", new Comment());
        model.addAttribute("role", role);
        model.addAttribute("userId", userId);
        model.addAttribute("authorId", post.getAuthor().getId());
        return "post";
    }


    @GetMapping("/new")
    public String getNewPost(Model model) {
        Post post = new Post();
        post.setId(0);
        model.addAttribute("post", post);
        return "new_blog";
    }

    @PostMapping("/save")
    public String savePost(@ModelAttribute("post") Post post, @RequestParam("tagsList") String tagsString) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.findUserByEmail(userEmail);
        post.setAuthor(user);
        Set<Tag> tags=postService.getListOfTagsFromString(tagsString);
        post.setTags(tags);
        postService.savePost(post);
        return "redirect:/posts";
    }

    @GetMapping("/updatePost")
    public String updatePost(@RequestParam("postId") int postId, Model model) {
        Post post = postService.getPostById(postId);
        String tagString = tagService.tagString(post.getTags());
        model.addAttribute("post", post);
        model.addAttribute("tagString", tagString);
        return "new_blog";
    }

    @GetMapping("/deletePost")
    public String deletePost(@RequestParam("postId") int postId) {
        postService.deletePostById(postId);
        return "redirect:/posts";
    }

}
package com.himanshu.blog_app.controller;

import com.himanshu.blog_app.entity.Comment;
import com.himanshu.blog_app.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public String createComment(@RequestParam("postId") int postId,
                                @ModelAttribute("postComment") Comment comment) {
        commentService.creteComment(postId, comment);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/delete")
    public String deleteComment(@RequestParam("postId") int postId,
                                @RequestParam("commentId") int commentId) {
        commentService.deleteCommentById(commentId);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/update")
    public String updateComment(@RequestParam("postId") int postId,
                                @RequestParam("commentId") int commentId,
                                Model model) {
        Comment comment = commentService.getCommentById(commentId);
        model.addAttribute("postComment", comment);
        model.addAttribute("postId", postId);
        model.addAttribute("commentId", commentId);
        return "edit_comment";
    }

    @PostMapping("/saveUpdate")
    public String updateComment(@RequestParam("postId") int postId,
                                @RequestParam("commentId") int commentId,
                                @ModelAttribute("postComment") Comment comment) {
        commentService.updateComment(commentId, comment);
        return "redirect:/posts/" + postId;
    }
}

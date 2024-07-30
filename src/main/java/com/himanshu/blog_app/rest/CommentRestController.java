package com.himanshu.blog_app.rest;


import com.himanshu.blog_app.entity.Comment;
import com.himanshu.blog_app.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService commentService;

    @Autowired
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("{id}")
    public String createComment(@PathVariable int id, @RequestBody Comment comment) {
        commentService.creteComment(id, comment);
        return "Successfully created comment";
    }

    @DeleteMapping("{commentId}")
    public String deleteComment(@PathVariable("commentId") int commentId) {
        commentService.deleteCommentById(commentId);
        return "Successfully deleted comment";
    }

    @PutMapping("{id}")
    public Comment updateComment(@PathVariable int id, @RequestBody Comment comment) {
        commentService.updateComment(id, comment);
        return commentService.getCommentById(id);
    }
}


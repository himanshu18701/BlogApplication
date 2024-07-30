package com.himanshu.blog_app.service;

import com.himanshu.blog_app.dao.CommentRepository;
import com.himanshu.blog_app.dao.PostRepository;
import com.himanshu.blog_app.entity.Comment;
import com.himanshu.blog_app.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Comment getCommentById(int id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteCommentById(int id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateComment(int commentId, Comment comment) {
        Comment existingComment = commentRepository.findById(commentId).orElse(null);
        if (existingComment != null) {
            existingComment.setName(comment.getName());
            existingComment.setEmail(comment.getEmail());
            existingComment.setComment(comment.getComment());
            existingComment.setUpdatedAt(LocalDateTime.now());
            commentRepository.save(existingComment);
        }
    }

    @Override
    public void creteComment(int postId, Comment comment) {
        Post post = postRepository.findById(postId).orElseThrow(RuntimeException::new);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }
}


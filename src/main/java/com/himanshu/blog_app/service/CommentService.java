package com.himanshu.blog_app.service;

import com.himanshu.blog_app.entity.Comment;

public interface CommentService {
    Comment getCommentById(int id);

    void updateComment(int commentId, Comment comment);

    void deleteCommentById(int id);

    void creteComment(int postId, Comment comment);
}
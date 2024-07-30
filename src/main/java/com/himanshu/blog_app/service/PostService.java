package com.himanshu.blog_app.service;

import com.himanshu.blog_app.entity.Post;
import com.himanshu.blog_app.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;


public interface PostService {
    Page<Post> getAllPosts(int pageNumber, int pageSize, String sortingField, String sortOrder);

    Post getPostById(int id);

    void savePost(Post post);

    void deletePostById(int id);

    Page<Post> searchPosts(String keyword, int pageNumber, int pageSize, String sortingField, String sortOrder);

    Pageable getPageablePosts(int pageNumber, int pageSize, String sortingField, String sortOrder);

    Page<Post> getPostsByTags(List<Tag> tags, int pageNumber, int pageSize, String sortingField, String sortingOrder);

    Set<Tag> getListOfTagsFromString(String tagsString);
}

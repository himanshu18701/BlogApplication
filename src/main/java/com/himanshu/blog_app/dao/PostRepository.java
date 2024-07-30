package com.himanshu.blog_app.dao;

import com.himanshu.blog_app.entity.Post;
import com.himanshu.blog_app.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p WHERE " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.excerpt) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.author.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Post> search(@Param("keyword") String keyword, Pageable pageable);

    Page<Post> findByTagsIn(List<Tag> tags, Pageable pageable);
}

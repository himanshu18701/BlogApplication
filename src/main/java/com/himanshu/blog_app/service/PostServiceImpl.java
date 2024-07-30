package com.himanshu.blog_app.service;

import com.himanshu.blog_app.dao.PostRepository;
import com.himanshu.blog_app.dao.TagRepository;
import com.himanshu.blog_app.entity.Post;
import com.himanshu.blog_app.entity.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public Page<Post> getAllPosts(int pageNumber, int pageSize, String sortingField, String sortOrder) {
        Pageable pageable = getPageablePosts(pageNumber, pageSize, sortingField, sortOrder);
        return postRepository.findAll(pageable);
    }

    @Override
    public Page<Post> searchPosts(String keyword, int pageNumber, int pageSize, String sortingField, String sortOrder) {
        Pageable pageable = getPageablePosts(pageNumber, pageSize, sortingField, sortOrder);
        return postRepository.search(keyword, pageable);
    }

    @Override
    public Pageable getPageablePosts(int pageNumber, int pageSize, String sortingField, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortingField).ascending()
                : Sort.by(sortingField).descending();
        return PageRequest.of(pageNumber, pageSize, sort);
    }

    @Override
    public Page<Post> getPostsByTags(List<Tag> tags, int pageNumber, int pageSize, String sortingField, String sortingOrder) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortingOrder), sortingField));
        if (tags == null || tags.isEmpty()) {
            return postRepository.findAll(pageable);
        } else {
            return postRepository.findByTagsIn(tags, pageable);
        }
    }

    @Override
    public Set<Tag> getListOfTagsFromString(String tagsString) {
        Set<Tag> tags = new HashSet<>();
        if (tagsString != null && !tagsString.isEmpty()) {
            String[] tagNames = tagsString.split(",");
            for (String tagName : tagNames) {
                Tag tag = tagRepository.findByName(tagName.trim());
                if (tag == null) {
                    tag = new Tag();
                    tag.setName(tagName.trim());
                    tagRepository.save(tag);
                }
                tags.add(tag);
            }
        }
        return tags;
    }

    @Override
    public Post getPostById(int id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void savePost(Post post) {
        if (post.getId() != 0) {
            Post existingPost = postRepository.findById(post.getId()).orElse(null);
            if (existingPost != null) {
                post.setCreatedAt(existingPost.getCreatedAt());
                post.setUpdatedAt(LocalDateTime.now());
                if (Boolean.TRUE.equals(post.getPublished()) && existingPost.getPublishedAt() == null) {
                    post.setPublishedAt(LocalDateTime.now());
                } else if (!Boolean.TRUE.equals(post.getPublished())) {
                    post.setPublishedAt(null);
                }
                Set<Tag> existingTags = new HashSet<>(existingPost.getTags());
                for (Tag tag : existingTags) {
                    tag.getPosts().remove(existingPost);
                    if (tag.getPosts().isEmpty()) {
                        tagRepository.delete(tag);
                    }
                }
            }
        } else {
            post.setCreatedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());
            if (Boolean.TRUE.equals(post.getPublished())) {
                post.setPublishedAt(LocalDateTime.now());
            }
        }
        Set<Tag> newTags = new HashSet<>(post.getTags());
        post.setTags(new HashSet<>());
        Post savedPost = postRepository.save(post);
        for (Tag tag : newTags) {
            Tag existingTag = tagRepository.findByName(tag.getName());
            if (existingTag == null) {
                existingTag = tagRepository.save(tag);
            }
            existingTag.getPosts().add(savedPost);
            tagRepository.save(existingTag);
            savedPost.getTags().add(existingTag);
        }
        postRepository.save(savedPost);
    }

    @Override
    @Transactional
    public void deletePostById(int id) {
        Post post = postRepository.findById(id).orElse(null);
        Set<Tag> tags = post.getTags();
        for (Tag tag : tags) {
            tag.getPosts().remove(post);
        }
        postRepository.deleteById(id);
        for (Tag tag : tags) {
            if (tag.getPosts().isEmpty()) {
                tagRepository.delete(tag);
            }
        }
    }
}

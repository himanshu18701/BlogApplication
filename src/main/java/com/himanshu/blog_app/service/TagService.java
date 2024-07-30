package com.himanshu.blog_app.service;


import com.himanshu.blog_app.entity.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {
    Tag findByName(String name);

    void save(Tag tag);

    String tagString(Set<Tag> tags);

    List<Tag> findAllById(List<Integer> ids);

    List<Tag> getAllTags();

}
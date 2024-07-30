package com.himanshu.blog_app.service;

import com.himanshu.blog_app.dao.TagRepository;
import com.himanshu.blog_app.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag findByName(String name) {
        return tagRepository.findByName(name);
    }

    public List<Tag> findAllById(List<Integer> ids) {
        return tagRepository.findAllById(ids);
    }

    @Override
    public void save(Tag tag) {
        tagRepository.save(tag);
    }

    @Override
    public String tagString(Set<Tag> tags) {
        StringBuilder tagStringBuilder = new StringBuilder();
        for (Tag tag : tags) {
            if (!tagStringBuilder.isEmpty()) {
                tagStringBuilder.append(", ");
            }
            tagStringBuilder.append(tag.getName());
        }
        return tagStringBuilder.toString();
    }
}

package com.example.demo.domain.blog.dto;

import com.example.demo.domain.user.User;
import lombok.Getter;

@Getter
public class PostBlogDTO {
    private String title;
    private String text;
    private User author;
    private String category;
}

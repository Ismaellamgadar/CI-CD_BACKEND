package com.example.demo.domain.blog.dto;

import lombok.Getter;

@Getter
public class UpdateBlogDTO {
    private String title;
    private String text;
    private String category;
}

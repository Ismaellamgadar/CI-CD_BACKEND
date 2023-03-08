package com.example.demo.domain.blog.dto;

import com.example.demo.core.generic.AbstractDTO;
import com.example.demo.domain.user.User;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
public class BlogDTO extends AbstractDTO {
    @Valid
    private UUID id;
    @Valid
    @NotNull
    private String title;
    private String text;
    private User author;
    private String category;
}

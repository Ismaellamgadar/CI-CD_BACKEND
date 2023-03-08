package com.example.demo.domain.blog;

import com.example.demo.core.generic.AbstractEntity;
import com.example.demo.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class Blog extends AbstractEntity {

    @NotNull
    private String title;

    @NotNull
    private String text;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;


    @NotNull
    private String category;

    public Blog(UUID id, String title, String text, User author, String category) {
        super(id);
        this.title = title;
        this.text = text;
        this.author = author;
        this.category = category;
    }

    public Blog(String title, String text, User author, String category) {
        this.title = title;
        this.text = text;
        this.author = author;
        this.category = category;
    }

    public Blog() {
    }

}

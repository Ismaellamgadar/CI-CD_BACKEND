package com.example.demo.domain.page;

import com.example.demo.core.generic.AbstractEntity;
import com.example.demo.domain.group.Group;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "custompage")
@Getter
public class CustomPage extends AbstractEntity {
    @Id
    @GeneratedValue
    @NotNull
    private UUID id;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "body")
    @NotNull
    private String body;

    @NotNull
    @ElementCollection
    @CollectionTable(name = "custompage_rules")
    @Column(name = "rules")
    private List<String> rules;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

    public UUID getId() {
        return id;
    }
    public Group getGroup() {
        return this.group;
    }

    public CustomPage setTitle(String title) {
        this.title = title;
        return this;
    }

    public CustomPage setBody(String body) {
        this.body = body;
        return this;
    }

    public CustomPage setRules(List<String> rules) {
        this.rules = rules;
        return this;
    }

    public CustomPage setGroup(Group group) {
        this.group = group;
        return this;
    }
}

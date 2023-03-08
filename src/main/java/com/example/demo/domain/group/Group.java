package com.example.demo.domain.group;

import com.example.demo.core.generic.AbstractEntity;
import com.example.demo.domain.page.CustomPage;
import com.example.demo.domain.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
@Deprecated
@Entity
@Table(name = "groups")
public class Group extends AbstractEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "groupName")
    @NotNull
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "groups_users", joinColumns = @JoinColumn(name = "groups_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"))
    private List<User> usersIds;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomPage> ownedPages;

    public Group setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public Group addUser(User user) {
        this.usersIds.add(user);
        return this;
    }

    public Group deleteUser(User user) {
        this.usersIds.remove(user);
        return this;
    }

    public Group setUsers(List<User> users) {
        this.usersIds = users;
        return this;
    }
    public Group setName(String name) {
        this.name = name;
        return this;
    }
    public void updateGroup(Group group) {
        this.name = group.getName();
        this.usersIds = group.getUsers();
        this.ownedPages = group.getOwnedPages();
    }
    public List<User> getUsers() {
        return this.usersIds;
    }

    public String getName() {
        return name;
    }

    public List<CustomPage> getOwnedPages() {
        return ownedPages;
    }

    public Group setOwnedPages(List<CustomPage> ownedPages) {
        this.ownedPages = ownedPages;
        return this;
    }
    public void build() {}
}

package com.example.demo.domain.blog;

import com.example.demo.core.generic.AbstractRepository;
import com.example.demo.domain.blog.dto.PostBlogDTO;
import com.example.demo.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface BlogRepository extends AbstractRepository<Blog> {
}

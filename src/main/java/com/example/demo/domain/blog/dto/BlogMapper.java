package com.example.demo.domain.blog.dto;

import com.example.demo.core.generic.AbstractMapper;
import com.example.demo.domain.authority.Authority;
import com.example.demo.domain.authority.dto.AuthorityDTO;
import com.example.demo.domain.blog.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface BlogMapper extends AbstractMapper<Blog, BlogDTO> {
}

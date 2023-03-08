package com.example.demo.domain.user.dto;


import com.example.demo.core.generic.AbstractMapper;
import com.example.demo.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRegisterMapper extends AbstractMapper<User, UserRegisterDTO> {
}

package com.example.demo.domain.group.dto;

import com.example.demo.core.generic.AbstractMapper;
import com.example.demo.domain.group.Group;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
@Deprecated
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper extends AbstractMapper<Group, GroupDTO> {
}

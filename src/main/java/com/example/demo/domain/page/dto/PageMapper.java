package com.example.demo.domain.page.dto;

import com.example.demo.core.generic.AbstractMapper;
import com.example.demo.domain.page.CustomPage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface PageMapper extends AbstractMapper<CustomPage, CustomPageDTO> {
}

package com.liviavilaca.investimentsmanager.dto.mapper.company;

import com.liviavilaca.investimentsmanager.dto.model.company.CompanyDTO;
import com.liviavilaca.investimentsmanager.model.company.Company;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    Company toModel(CompanyDTO companyDTO);

    CompanyDTO toDTO(Company company);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCompanyFromDTO(CompanyDTO companyDTO, @MappingTarget Company company);
}

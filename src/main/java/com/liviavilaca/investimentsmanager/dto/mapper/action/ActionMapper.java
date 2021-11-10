package com.liviavilaca.investimentsmanager.dto.mapper.action;

import com.liviavilaca.investimentsmanager.dto.model.action.ActionDTO;
import com.liviavilaca.investimentsmanager.model.action.Action;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActionMapper {

    ActionMapper INSTANCE = Mappers.getMapper(ActionMapper.class);

    @Mapping(source = "companyId", target = "company.id")
    @Mapping(source = "companyPrice", target = "company.price")
    @Mapping(source = "acquisitionId", target = "acquisition.id")
    Action toModel(ActionDTO actionDTO);

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.price", target = "companyPrice")
    @Mapping(source = "acquisition.id", target = "acquisitionId")
    ActionDTO toDTO(Action action);

}

package com.liviavilaca.investimentsmanager.dto.mapper.acquisition;

import com.liviavilaca.investimentsmanager.dto.mapper.action.ActionMapper;
import com.liviavilaca.investimentsmanager.dto.model.action.ActionDTO;
import com.liviavilaca.investimentsmanager.dto.model.acquisition.AcquisitionDTO;
import com.liviavilaca.investimentsmanager.model.action.Action;
import com.liviavilaca.investimentsmanager.model.acquisition.Acquisition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AcquisitionMapper {

    AcquisitionMapper INSTANCE = Mappers.getMapper(AcquisitionMapper.class);

    @Mapping(source = "clientId", target = "client.id")
    Acquisition toModel(AcquisitionDTO acquisitionDTO);

    @Mapping(source = "client.id", target = "clientId")
    AcquisitionDTO toDTO(Acquisition acquisition);

    public default Action actionDTOToAction(ActionDTO actionDTO) { return ActionMapper.INSTANCE.toModel(actionDTO); }

    public default ActionDTO actionToActionDTO(Action action) {
        return ActionMapper.INSTANCE.toDTO(action);
    }
}

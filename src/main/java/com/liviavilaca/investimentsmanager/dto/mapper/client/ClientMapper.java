package com.liviavilaca.investimentsmanager.dto.mapper.client;

import com.liviavilaca.investimentsmanager.dto.model.client.ClientDTO;
import com.liviavilaca.investimentsmanager.model.client.Client;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    ClientDTO toDTO(Client client);

    Client toModel(ClientDTO clientDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateClientFromDTO(ClientDTO clientDTO, @MappingTarget Client client);
}

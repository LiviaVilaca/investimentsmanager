package com.liviavilaca.investimentsmanager.service.acquisition;

import com.liviavilaca.investimentsmanager.dto.mapper.acquisition.AcquisitionMapper;
import com.liviavilaca.investimentsmanager.dto.model.acquisition.AcquisitionDTO;
import com.liviavilaca.investimentsmanager.dto.response.ResponseEntityDTO;
import com.liviavilaca.investimentsmanager.model.acquisition.Acquisition;
import com.liviavilaca.investimentsmanager.repository.acquisition.AcquisitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AcquisitionService {

    private AcquisitionRepository acquisitionRepository;

    private final AcquisitionMapper acquisitionMapper = AcquisitionMapper.INSTANCE;

    @Autowired
    public AcquisitionService(AcquisitionRepository acquisitionRepository) {
        this.acquisitionRepository = acquisitionRepository;
    }

    public ResponseEntityDTO<AcquisitionDTO> create(AcquisitionDTO acquisitionDTO) {
        Acquisition acquisition = acquisitionMapper.toModel(acquisitionDTO);
        acquisition.getActions().stream().forEach(action -> action.setAcquisition(acquisition));
        Acquisition savedAcquisition = this.acquisitionRepository.save(acquisition);
        ResponseEntityDTO<AcquisitionDTO> response = new ResponseEntityDTO<>();
        response.setData(acquisitionMapper.toDTO(savedAcquisition));
        return response;
    }

    public ResponseEntityDTO<List<AcquisitionDTO>> findByClientId (Long clientId, int page, int size){
        Pageable pageable =  PageRequest.of(page, size, Sort.by("totalSpent").descending());
        Page<Acquisition> acquisitions = this.acquisitionRepository.findByClientId(clientId, pageable);

        ResponseEntityDTO<List<AcquisitionDTO>> listEntityResponseDTO = new ResponseEntityDTO<>();
        listEntityResponseDTO.setData(acquisitions.getContent().stream().map(acquisitionMapper::toDTO).collect(Collectors.toList()));
        listEntityResponseDTO.setTotalData(acquisitions.getTotalElements());
        return listEntityResponseDTO;
    }


}

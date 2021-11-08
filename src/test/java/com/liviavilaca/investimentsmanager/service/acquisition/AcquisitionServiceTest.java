package com.liviavilaca.investimentsmanager.service.acquisition;

import com.liviavilaca.investimentsmanager.dto.mapper.acquisition.AcquisitionMapper;
import com.liviavilaca.investimentsmanager.dto.model.acquisition.AcquisitionDTO;
import com.liviavilaca.investimentsmanager.dto.response.ResponseEntityDTO;
import com.liviavilaca.investimentsmanager.model.acquisition.Acquisition;
import com.liviavilaca.investimentsmanager.repository.acquisition.AcquisitionRepository;
import com.liviavilaca.investimentsmanager.util.acquisition.AcquisitionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AcquisitionServiceTest {

    @Mock
    private AcquisitionRepository acquisitionRepository;

    @InjectMocks
    private AcquisitionService acquisitionService;

    private AcquisitionMapper acquisitionMapper = AcquisitionMapper.INSTANCE;

    @Test
    void whenCreateAcquisitionThenReturnThisAcquisition() {
        AcquisitionDTO acquisitionDTO = AcquisitionUtils.createFakeAcquisitionDTO();
        acquisitionDTO.setId(null);
        Acquisition expectedCreatedAcquisition = acquisitionMapper.toModel(acquisitionDTO);
        expectedCreatedAcquisition.getActions().stream().forEach(action -> action.setAcquisition(null));
        when(acquisitionRepository.save(expectedCreatedAcquisition))
                .thenReturn(expectedCreatedAcquisition);

        ResponseEntityDTO<AcquisitionDTO> actualResponse = acquisitionService.create(acquisitionDTO);

        assertEquals(expectedCreatedAcquisition.getTotalSpent().doubleValue(), actualResponse.getData().getTotalSpent());
        assertEquals(expectedCreatedAcquisition.getClient().getId(), actualResponse.getData().getClientId());
        assertEquals(expectedCreatedAcquisition.getActions().get(0).getCompany().getId(), actualResponse.getData().getActions().get(0).getCompanyId());
    }
}

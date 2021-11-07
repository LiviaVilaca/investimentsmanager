package com.liviavilaca.investimentsmanager.service.action;

import com.liviavilaca.investimentsmanager.dto.mapper.action.ActionMapper;
import com.liviavilaca.investimentsmanager.dto.mapper.client.ClientMapper;
import com.liviavilaca.investimentsmanager.dto.mapper.company.CompanyMapper;
import com.liviavilaca.investimentsmanager.dto.model.action.ActionDTO;
import com.liviavilaca.investimentsmanager.dto.model.client.ClientDTO;
import com.liviavilaca.investimentsmanager.dto.model.company.CompanyDTO;
import com.liviavilaca.investimentsmanager.dto.response.ActionsAcquiredResponseDTO;
import com.liviavilaca.investimentsmanager.dto.response.ResponseEntityDTO;
import com.liviavilaca.investimentsmanager.model.action.Action;
import com.liviavilaca.investimentsmanager.model.company.Company;
import com.liviavilaca.investimentsmanager.repository.action.ActionRepository;
import com.liviavilaca.investimentsmanager.util.action.ActionUtils;
import com.liviavilaca.investimentsmanager.util.client.ClientUtils;
import com.liviavilaca.investimentsmanager.util.company.CompanyUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActionServiceTest {

    @Mock
    private ActionRepository actionRepository;

    @InjectMocks
    private ActionService actionService;

    private ActionMapper actionMapper = ActionMapper.INSTANCE;

    @Test
    void whenGivenExistingClientIdThenReturnActionsList() {
        ActionDTO actionDTO = ActionUtils.createFakeActionDTO();
        ResponseEntityDTO<List<ActionDTO>> expectedActionsList = new ResponseEntityDTO<>();
        expectedActionsList.setData(Arrays.asList(new ActionDTO[] { actionDTO }));

        Page<Action> pagedResponse = new PageImpl(Arrays.asList(new Action[] { actionMapper.toModel(actionDTO) }));

        when(actionRepository.findByClientId(actionDTO.getClientId(), PageRequest.of(0, 10, Sort.by("quantity").descending())))
                .thenReturn(pagedResponse);

        ResponseEntityDTO<List<ActionDTO>> actualActionsList = actionService.findByClientId(actionDTO.getClientId(), 0, 10);

        assertEquals(expectedActionsList.getData(), actualActionsList.getData());
    }

    @Test
    void whenAcquireActionsThenReturnActionsList(){
        ActionDTO actionDTO = ActionUtils.createFakeActionDTO();
        CompanyDTO companyDTO = CompanyUtils.createFakeCompanyDTO();
        ClientDTO clientDTO = ClientUtils.createFakeClientDTO();

        actionDTO.setId(null);
        actionDTO.setClientId(clientDTO.getId());
        actionDTO.setCompanyId(companyDTO.getId());
        actionDTO.setCompanyPrice(companyDTO.getPrice());
        Action action = actionMapper.toModel(actionDTO);
        when(actionRepository.save(action)).thenReturn(action);

        Double expectedExchange = 0.5;
        Double expectedTotalSpent = companyDTO.getPrice().doubleValue()*action.getQuantity();
        Double totalAmount = expectedTotalSpent + expectedExchange;

        List<CompanyDTO> companies = Arrays.asList(new CompanyDTO[]{companyDTO});
        ActionsAcquiredResponseDTO actualResponse = actionService.acquiredActions(totalAmount,clientDTO, companies);

        assertEquals(expectedExchange, actualResponse.getExchange());
        assertEquals(expectedTotalSpent, actualResponse.getTotalSpent());
    }
}

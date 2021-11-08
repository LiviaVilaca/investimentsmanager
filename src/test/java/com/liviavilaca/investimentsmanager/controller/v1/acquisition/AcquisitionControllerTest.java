package com.liviavilaca.investimentsmanager.controller.v1.acquisition;

import com.liviavilaca.investimentsmanager.dto.model.acquisition.AcquisitionDTO;
import com.liviavilaca.investimentsmanager.dto.model.client.ClientDTO;
import com.liviavilaca.investimentsmanager.dto.response.ResponseEntityDTO;
import com.liviavilaca.investimentsmanager.exception.EntityNotFoundException;
import com.liviavilaca.investimentsmanager.service.acquisition.AcquisitionService;
import com.liviavilaca.investimentsmanager.service.client.ClientService;
import com.liviavilaca.investimentsmanager.service.company.CompanyService;
import com.liviavilaca.investimentsmanager.util.acquisition.AcquisitionUtils;
import com.liviavilaca.investimentsmanager.util.client.ClientUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class AcquisitionControllerTest {

    private static final String URL = "/api/v1/clients";

    private MockMvc mockMvc;

    @Mock
    private AcquisitionService acquisitionService;

    @Mock
    private ClientService clientService;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private AcquisitionController acquisitionController;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(acquisitionController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers(((viewName, locale) -> new MappingJackson2JsonView()))
                .build();
    }


    @Test
    void testWhenGETActionsIsCalledForAbsentClientThenBadRequestShouldBeReturned() throws Exception {
        var clientId = 1L;
        when(clientService.findById(clientId)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get(URL+"/"+clientId+"/actions"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testWhenGETActionsIsCalledThenAListOfClientActionsIsReturned() throws Exception {
        AcquisitionDTO acquisitionDTO = AcquisitionUtils.createFakeAcquisitionDTO();
        var clientId = acquisitionDTO.getClientId();

        ClientDTO clientDTO = ClientUtils.createFakeClientDTO();
        clientDTO.setId(clientId);
        clientDTO.setPassword(null);

        ResponseEntityDTO<ClientDTO> clientResponse = new ResponseEntityDTO<>();
        clientResponse.setData(clientDTO);
        when(clientService.findById(clientId)).thenReturn(clientResponse);

        ResponseEntityDTO<List<AcquisitionDTO>> expectedResponse = new ResponseEntityDTO<List<AcquisitionDTO>>();
        expectedResponse.setData(Arrays.asList(new AcquisitionDTO[]{acquisitionDTO}));
        when(acquisitionService.findByClientId(clientId,0,10)).thenReturn(expectedResponse);

        mockMvc.perform(get(URL+"/"+clientId+"/actions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(expectedResponse.getData().get(0).getId()));

    }

/*    @Test
    void testWhenPOSTActionsIsCalledThenAListOfActionsIsAcquiredAndReturned() throws Exception {
        AcquisitionDTO acquisitionDTO = AcquisitionUtils.createFakeAcquisitionDTO();
        var clientId = acquisitionDTO.getClientId();

        ClientDTO clientDTO = ClientUtils.createFakeClientDTO();
        clientDTO.setId(clientId);
        clientDTO.setPassword(null);
        ResponseEntityDTO<ClientDTO> clientResponse = new ResponseEntityDTO<>();
        clientResponse.setData(clientDTO);
        when(clientService.findById(clientId)).thenReturn(clientResponse);

        ActionDTO actionDTO = ActionUtils.createFakeActionDTO();
        actionDTO.setAcquisitionId(acquisitionDTO.getId());
        actionDTO.setTotalSpent(8.0);
        actionDTO.setCompanyPrice(4.0);
        actionDTO.setQuantity(2L);
        actionDTO.setId(null);

        CompanyDTO companyDTO = CompanyUtils.createFakeCompanyDTO();
        companyDTO.setId(actionDTO.getCompanyId());
        companyDTO.setPrice(4.0);
        List<CompanyDTO> companies = Arrays.asList(new CompanyDTO[]{companyDTO});
        ResponseEntityDTO<List<CompanyDTO>> companiesResponse = new ResponseEntityDTO<>();
        companiesResponse.setData(companies);
        when(companyService.findByStatusActiveOrderByPriceAsc()).thenReturn(companiesResponse);

        var totalAmount = 10.0D;
        acquisitionDTO.setActions(Arrays.asList(new ActionDTO[]{actionDTO}));
        acquisitionDTO.setTotalSpent(companyDTO.getPrice()*actionDTO.getQuantity());
        acquisitionDTO.setExchange(totalAmount - acquisitionDTO.getTotalSpent());
        acquisitionDTO.setId(null);
        ResponseEntityDTO<AcquisitionDTO> acquisitionResponse = new ResponseEntityDTO<>();
        acquisitionResponse.setData(acquisitionDTO);
        when(acquisitionService.create(acquisitionDTO)).thenReturn(acquisitionResponse);


        ResponseEntityDTO<List<AcquisitionDTO>> expectedResponse = new ResponseEntityDTO<List<AcquisitionDTO>>();
        expectedResponse.setData(Arrays.asList(new AcquisitionDTO[]{acquisitionDTO}));

        mockMvc.perform(post(URL+"/"+clientId+"/actions?totalAmount="+totalAmount))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]").value(expectedResponse.getData().get(0)));
    }*/
}

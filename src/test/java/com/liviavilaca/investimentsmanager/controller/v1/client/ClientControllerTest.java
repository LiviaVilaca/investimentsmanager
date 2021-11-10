package com.liviavilaca.investimentsmanager.controller.v1.client;

import com.liviavilaca.investimentsmanager.dto.model.client.ClientDTO;
import com.liviavilaca.investimentsmanager.dto.response.ResponseEntityDTO;
import com.liviavilaca.investimentsmanager.exception.EntityNotFoundException;
import com.liviavilaca.investimentsmanager.service.client.ClientService;
import com.liviavilaca.investimentsmanager.util.client.ClientUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Arrays;
import java.util.List;

import static com.liviavilaca.investimentsmanager.util.JsonConversionUtils.asJsonString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    private static final String URL = "/api/v1/clients";

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(clientController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers(((viewName, locale) -> new MappingJackson2JsonView()))
                .build();
    }

    @Test
    void testWhenPOSTisCalledThenAClientShouldBeCreated() throws Exception {
        ClientDTO clientDTO = ClientUtils.createFakeClientDTO();
        ResponseEntityDTO<ClientDTO> expectedMessageResponse = new ResponseEntityDTO<ClientDTO>();
        expectedMessageResponse.setData(clientDTO);
        System.out.println(expectedMessageResponse);
        when(clientService.create(clientDTO)).thenReturn(expectedMessageResponse);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(clientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value(expectedMessageResponse.getData().getName()))
                .andExpect(jsonPath("$.data.email").value(expectedMessageResponse.getData().getEmail()))
                .andExpect(jsonPath("$.data.cpf").value(expectedMessageResponse.getData().getCpf()));
    }

    @Test
    void testWhenPOSTWithInvalidEmailThenBadRequestShouldBeReturned() throws Exception {
        ClientDTO clientDTO = ClientUtils.createFakeClientDTO();
        clientDTO.setEmail(null);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(clientDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWhenGETisCalledThenAListOfCompaniesIsReturned() throws Exception {
        ClientDTO clientDTO = ClientUtils.createFakeClientDTO();
        clientDTO.setPassword(null);
        ResponseEntityDTO<List<ClientDTO>> expectedResponse = new ResponseEntityDTO<List<ClientDTO>>();
        expectedResponse.setData(Arrays.asList(new ClientDTO[]{clientDTO}));

        when(clientService.findAll(0, 10)).thenReturn(expectedResponse);

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]").value(expectedResponse.getData().get(0)));
    }


}

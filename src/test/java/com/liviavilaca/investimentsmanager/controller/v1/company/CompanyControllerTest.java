package com.liviavilaca.investimentsmanager.controller.v1.company;

import com.liviavilaca.investimentsmanager.dto.model.company.CompanyDTO;
import com.liviavilaca.investimentsmanager.dto.response.ResponseEntityDTO;
import com.liviavilaca.investimentsmanager.service.company.CompanyService;
import com.liviavilaca.investimentsmanager.util.company.CompanyUtils;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.liviavilaca.investimentsmanager.util.JsonConversionUtils.asJsonString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerTest {

    private static final String URL = "/api/v1/companies";

    private MockMvc mockMvc;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(companyController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers(((viewName, locale) -> new MappingJackson2JsonView()))
                .build();
    }

    @Test
    void testWhenPOSTisCalledThenACompanyShouldBeCreated() throws Exception {
        CompanyDTO companyDTO = CompanyUtils.createFakeCompanyDTO();
        ResponseEntityDTO<CompanyDTO> expectedMessageResponse = new ResponseEntityDTO<CompanyDTO>();
        expectedMessageResponse.setData(companyDTO);

        when(companyService.create(companyDTO)).thenReturn(expectedMessageResponse);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(companyDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(expectedMessageResponse.getData()));
    }

    @Test
    void testWhenPOSTWithInvalidPriceThenBadRequestShouldBeReturned() throws Exception {
        CompanyDTO companyDTO = CompanyUtils.createFakeCompanyDTO();
        companyDTO.setPrice(-19.9D);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(companyDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWhenPUTisCalledThenTheCompanyIsUpdated() throws Exception {
        CompanyDTO companyDTO = CompanyUtils.createFakeCompanyDTO();
        ResponseEntityDTO<CompanyDTO> expectedResponse = new ResponseEntityDTO<CompanyDTO>();
        expectedResponse.setData(companyDTO);

        when(companyService.update(companyDTO.getId(), companyDTO)).thenReturn(expectedResponse);

        mockMvc.perform(put(URL+"/"+companyDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(companyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(expectedResponse.getData()));
    }

    @Test
    void testWhenPUTisCalledWithInvalidCompanyThenThrowsAnException() throws Exception {
        CompanyDTO companyDTO = CompanyUtils.createFakeCompanyDTO();
        companyDTO.setName(null);

        mockMvc.perform(put(URL+"/"+companyDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(companyDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWhenGETisCalledThenAListOfCompaniesIsReturned() throws Exception {
        CompanyDTO companyDTO = CompanyUtils.createFakeCompanyDTO();
        ResponseEntityDTO<List<CompanyDTO>> expectedResponse = new ResponseEntityDTO<List<CompanyDTO>>();
        expectedResponse.setData(Arrays.asList(new CompanyDTO[]{companyDTO}));

        when(companyService.findAll(null, 0, 10)).thenReturn(expectedResponse);

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]").value(expectedResponse.getData().get(0)));
    }

}

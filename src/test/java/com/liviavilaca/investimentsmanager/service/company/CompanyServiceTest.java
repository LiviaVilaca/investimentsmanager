package com.liviavilaca.investimentsmanager.service.company;

import com.liviavilaca.investimentsmanager.dto.mapper.company.CompanyMapper;
import com.liviavilaca.investimentsmanager.dto.model.company.CompanyDTO;
import com.liviavilaca.investimentsmanager.dto.response.MessageResponseDTO;
import com.liviavilaca.investimentsmanager.dto.response.ResponseEntityDTO;
import com.liviavilaca.investimentsmanager.exception.EntityNotFoundException;
import com.liviavilaca.investimentsmanager.model.company.Company;
import com.liviavilaca.investimentsmanager.repository.company.CompanyRepository;
import com.liviavilaca.investimentsmanager.util.company.CompanyUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    private CompanyMapper companyMapper = CompanyMapper.INSTANCE;

    @Test
    void whenGivenExistingIdThenReturnThisCompany() throws EntityNotFoundException {
        Company expectedFoundCompany = CompanyUtils.createFakeCompany();

        when(companyRepository.findById(expectedFoundCompany.getId()))
                .thenReturn(Optional.of(expectedFoundCompany));

        ResponseEntityDTO<CompanyDTO> companyDTO = companyService.findById(expectedFoundCompany.getId());

        assertEquals(expectedFoundCompany.getName(), companyDTO.getData().getName());
        assertEquals(expectedFoundCompany.getTicker(), companyDTO.getData().getTicker());
        assertEquals(expectedFoundCompany.getPrice().doubleValue(), companyDTO.getData().getPrice());
    }

    @Test
    void whenGivenAbsentIdThenNotFindThrowAnException() {
        var invalidId = 123456L;
        when(companyRepository.findById(invalidId)).thenReturn(Optional.ofNullable(any(Company.class)));

        assertThrows(EntityNotFoundException.class, () -> companyService.findById(invalidId));
    }

    @Test
    void whenCreateCompanyThenReturnThisCompany() {
        Company expectedCreatedCompany = CompanyUtils.createFakeCompany();
        when(companyRepository.save(expectedCreatedCompany))
                .thenReturn(expectedCreatedCompany);

        ResponseEntityDTO<CompanyDTO> companyDTO = companyService.create(companyMapper.toDTO(expectedCreatedCompany));

        assertEquals(expectedCreatedCompany.getName(), companyDTO.getData().getName());
        assertEquals(expectedCreatedCompany.getTicker(), companyDTO.getData().getTicker());
        assertEquals(expectedCreatedCompany.getPrice().doubleValue(), companyDTO.getData().getPrice());
    }

    @Test
    void whenDeleteCompanyReturnSuccessfulMessage() throws EntityNotFoundException {
        var validId = 1L;

        doNothing().when(companyRepository).deleteById(validId);
        MessageResponseDTO response = companyService.deleteById(validId);
        assertEquals("Company with id "+validId+" deleted!", response.getMessage());
    }

}

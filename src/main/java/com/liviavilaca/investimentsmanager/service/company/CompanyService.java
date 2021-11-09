package com.liviavilaca.investimentsmanager.service.company;

import com.liviavilaca.investimentsmanager.dto.mapper.company.CompanyMapper;
import com.liviavilaca.investimentsmanager.dto.model.company.CompanyDTO;
import com.liviavilaca.investimentsmanager.dto.response.MessageResponseDTO;
import com.liviavilaca.investimentsmanager.dto.response.ResponseEntityDTO;
import com.liviavilaca.investimentsmanager.exception.EntityNotFoundException;
import com.liviavilaca.investimentsmanager.model.company.Company;
import com.liviavilaca.investimentsmanager.repository.company.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private CompanyRepository companyRepository;

    private final CompanyMapper companyMapper = CompanyMapper.INSTANCE;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @CacheEvict(value = "companies", allEntries = true)
    public ResponseEntityDTO<CompanyDTO> create (CompanyDTO companyDTO){
        Company savedCompany = this.companyRepository.save(companyMapper.toModel(companyDTO));
        ResponseEntityDTO<CompanyDTO> response = new ResponseEntityDTO<>();
        response.setData(companyMapper.toDTO(savedCompany));
        return response;
    }

    public ResponseEntityDTO<CompanyDTO> findById(Long id) throws EntityNotFoundException {
        Company company = companyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Company.class, id));
        ResponseEntityDTO<CompanyDTO> response = new ResponseEntityDTO<>();
        response.setData(companyMapper.toDTO(company));
        return response;
    }

    @Cacheable("companies")
    public ResponseEntityDTO findByStatusActiveOrderByPriceAsc(){
        /**
         * TODO Pegar todas as páginas
         */
        Pageable pageable =  PageRequest.of(0, 100, Sort.by("price").ascending());
        Page<Company> companies = companyRepository.findByStatus(true, pageable);
        ResponseEntityDTO listEntityResponseDTO = ResponseEntityDTO.builder()
                .totalData(companies.getTotalElements())
                .data(companies.getContent().stream().map(companyMapper::toDTO).collect(Collectors.toList()))
                .build();
        return listEntityResponseDTO;
    }

    @Cacheable("companies")
    public ResponseEntityDTO<List<CompanyDTO>> findAll(Boolean status, int page, int size){
        Pageable pageable =  PageRequest.of(page, size, Sort.by("price").descending());
        Page<Company> companies = status == null ? companyRepository.findAll(pageable) : companyRepository.findByStatus(status, pageable);
        ResponseEntityDTO<List<CompanyDTO>> listEntityResponseDTO = new ResponseEntityDTO<>();
        listEntityResponseDTO.setData(companies.getContent().stream().map(companyMapper::toDTO).collect(Collectors.toList()));
        listEntityResponseDTO.setTotalData(companies.getTotalElements());
        return listEntityResponseDTO;
    }

    @CacheEvict(value = "companies", allEntries = true)
    public MessageResponseDTO deleteById (Long id) throws EntityNotFoundException {
        try{
            this.companyRepository.deleteById(id);
            return MessageResponseDTO.builder().message("Company with id "+id+" deleted!").build();
        } catch (EmptyResultDataAccessException e){
            throw new EntityNotFoundException(Company.class, id);
        }
    }

    @CacheEvict(value = "companies", allEntries = true)
    public ResponseEntityDTO<CompanyDTO> update (Long id, CompanyDTO companyDTO) throws EntityNotFoundException {
        Company company = companyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Company.class, id));
        companyMapper.updateCompanyFromDTO(companyDTO, company);
        Company updatedCompany = companyRepository.save(company);

        ResponseEntityDTO<CompanyDTO> response = new ResponseEntityDTO<>();
        response.setData(companyMapper.toDTO(updatedCompany));
        return response;
    }

}

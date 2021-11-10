package com.liviavilaca.investimentsmanager.controller.v1.company;

import com.liviavilaca.investimentsmanager.dto.model.company.CompanyDTO;
import com.liviavilaca.investimentsmanager.dto.response.MessageResponseDTO;
import com.liviavilaca.investimentsmanager.dto.response.ResponseEntityDTO;
import com.liviavilaca.investimentsmanager.exception.EntityNotFoundException;
import com.liviavilaca.investimentsmanager.service.company.CompanyService;
import com.liviavilaca.investimentsmanager.util.validation.ValidateOnInsert;
import com.liviavilaca.investimentsmanager.util.validation.ValidateOnUpdate;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
@Validated
public class CompanyController {

    private CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Validated(ValidateOnInsert.class)
    @PostMapping
    @ApiOperation(value = "Route to create a Company in the API", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityDTO<CompanyDTO>> create (@Valid @RequestBody CompanyDTO company){
        return new ResponseEntity<ResponseEntityDTO<CompanyDTO>>(this.companyService.create(company), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Route to find a Company by Id in the API", produces = "application/json")
    public ResponseEntity<ResponseEntityDTO<CompanyDTO>> findById (@PathVariable  Long id) throws EntityNotFoundException {
        return new ResponseEntity<ResponseEntityDTO<CompanyDTO>>(this.companyService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "Route to find all Companies in the API. It can filter by Company Status", produces = "application/json")
    public ResponseEntity<ResponseEntityDTO<List<CompanyDTO>>> findAll(@RequestParam(required = false) Boolean status, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        ResponseEntityDTO<List<CompanyDTO>> response = this.companyService.findAll(status, page, size);
        return new ResponseEntity<ResponseEntityDTO<List<CompanyDTO>>>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Route to delete a Company by Id in the API", produces = "application/json")
    public ResponseEntity<MessageResponseDTO> deleteById(@PathVariable Long id) throws EntityNotFoundException {
        return new ResponseEntity<MessageResponseDTO>(this.companyService.deleteById(id), HttpStatus.OK);
    }

    @Validated(ValidateOnUpdate.class)
    @PutMapping("/{id}")
    @ApiOperation(value = "Route to update a Company by Id in the API", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityDTO<CompanyDTO>> update (@PathVariable Long id, @RequestBody @Valid CompanyDTO company) throws EntityNotFoundException {
        return new ResponseEntity<ResponseEntityDTO<CompanyDTO>>(this.companyService.update(id, company), HttpStatus.OK);
    }
}

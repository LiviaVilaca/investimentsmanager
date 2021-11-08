package com.liviavilaca.investimentsmanager.controller.v1.acquisition;

import com.liviavilaca.investimentsmanager.dto.model.action.ActionDTO;
import com.liviavilaca.investimentsmanager.dto.model.acquisition.AcquisitionDTO;
import com.liviavilaca.investimentsmanager.dto.model.client.ClientDTO;
import com.liviavilaca.investimentsmanager.dto.model.company.CompanyDTO;
import com.liviavilaca.investimentsmanager.dto.response.ResponseEntityDTO;
import com.liviavilaca.investimentsmanager.exception.EntityNotFoundException;
import com.liviavilaca.investimentsmanager.service.action.ActionService;
import com.liviavilaca.investimentsmanager.service.acquisition.AcquisitionService;
import com.liviavilaca.investimentsmanager.service.client.ClientService;
import com.liviavilaca.investimentsmanager.service.company.CompanyService;
import com.liviavilaca.investimentsmanager.util.AcquireActionUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clients/{id}/actions")
public class AcquisitionController {

    private AcquisitionService acquisitionService;
    private ClientService clientService;
    private CompanyService companyService;
    private ActionService actionService;

    @Autowired
    public AcquisitionController(AcquisitionService acquisitionService, ClientService clientService, CompanyService companyService,  ActionService actionService) {
        this.acquisitionService = acquisitionService;
        this.clientService = clientService;
        this.companyService = companyService;
        this.actionService = actionService;
    }

    @PostMapping
    @ApiOperation(value = "Route to acquire Actions for a Client in the API", produces = "application/json")
    public ResponseEntity<ResponseEntityDTO<AcquisitionDTO>> acquiredActions (@PathVariable Long id,
                                                                              @RequestParam @NotNull @ApiParam(value = "The total amount to acquire actions") Double totalAmount) throws EntityNotFoundException {

        ResponseEntityDTO<ClientDTO> clientResponse = this.clientService.findById(id);
        /**
         * TODO Carregar companies em memória
         */
        List<CompanyDTO> activeCompanies = (List<CompanyDTO>) this.companyService.findByStatusActiveOrderByPriceAsc().getData();

        List<ActionDTO> actions = AcquireActionUtil.distributeActionsByAllCompanies(totalAmount, activeCompanies);

        Double totalSpent = actions.stream().mapToDouble(action -> action.getTotalSpent()).sum();

        AcquisitionDTO acquisitionDTO = AcquisitionDTO.builder()
                .clientId(id)
                .exchange(totalAmount - totalSpent)
                .totalSpent(totalSpent)
                .actions(actions)
                .build();

        ResponseEntityDTO<AcquisitionDTO> acquisitionResponse = acquisitionService.create(acquisitionDTO);

        return new ResponseEntity<ResponseEntityDTO<AcquisitionDTO>>(acquisitionResponse, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "Route to list all Client Actions in the API", produces = "application/json")
    public ResponseEntity<ResponseEntityDTO<List<AcquisitionDTO>>> findAllAcquisitions(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws EntityNotFoundException {
        ResponseEntityDTO<ClientDTO> clientResponse = this.clientService.findById(id);
        ResponseEntityDTO<List<AcquisitionDTO>> response = this.acquisitionService.findByClientId(clientResponse.getData().getId(), page, size);
        return new ResponseEntity<ResponseEntityDTO<List<AcquisitionDTO>>>(response, HttpStatus.OK);
    }


}

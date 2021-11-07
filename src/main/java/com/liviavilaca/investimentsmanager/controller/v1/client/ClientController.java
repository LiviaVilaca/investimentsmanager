package com.liviavilaca.investimentsmanager.controller.v1.client;

import com.liviavilaca.investimentsmanager.dto.model.action.ActionDTO;
import com.liviavilaca.investimentsmanager.dto.model.client.ClientDTO;
import com.liviavilaca.investimentsmanager.dto.model.company.CompanyDTO;
import com.liviavilaca.investimentsmanager.dto.response.ActionsAcquiredResponseDTO;
import com.liviavilaca.investimentsmanager.dto.response.MessageResponseDTO;
import com.liviavilaca.investimentsmanager.dto.response.ResponseEntityDTO;
import com.liviavilaca.investimentsmanager.exception.EntityNotFoundException;
import com.liviavilaca.investimentsmanager.service.action.ActionService;
import com.liviavilaca.investimentsmanager.service.client.ClientService;
import com.liviavilaca.investimentsmanager.service.company.CompanyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private ClientService clientService;
    private ActionService actionService;
    private CompanyService companyService;

    @Autowired
    public ClientController(ClientService clientService, ActionService actionService, CompanyService companyService) {
        this.clientService = clientService;
        this.actionService = actionService;
        this.companyService = companyService;
    }

    @PostMapping
    @ApiOperation(value = "Route to create a Client in the API", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityDTO<ClientDTO>> create (@RequestBody @Valid ClientDTO client){
        System.out.println("ENTROU");
        return new ResponseEntity<ResponseEntityDTO<ClientDTO>>(this.clientService.create(client), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Route to find a Client by Id in the API", produces = "application/json")
    public ResponseEntity<ResponseEntityDTO<ClientDTO>> findById(@PathVariable Long id) throws EntityNotFoundException {
        return new ResponseEntity<ResponseEntityDTO<ClientDTO>>(this.clientService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "Route to find all Clients in the API", produces = "application/json")
    public ResponseEntity<ResponseEntityDTO<List<ClientDTO>>> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<ResponseEntityDTO<List<ClientDTO>>>(this.clientService.findAll(page, size), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Route to delete a Client by Id in the API", produces = "application/json")
    public ResponseEntity<MessageResponseDTO> deleteById(@PathVariable Long id) throws EntityNotFoundException {
        return new ResponseEntity<MessageResponseDTO>(this.clientService.deleteById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/actions")
    @ApiOperation(value = "Route to list all Client Actions in the API", produces = "application/json")
    public ResponseEntity<ResponseEntityDTO<List<ActionDTO>>> findAllClientActions(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws EntityNotFoundException {
        ResponseEntityDTO<ClientDTO> clientResponse = this.clientService.findById(id);
        ResponseEntityDTO<List<ActionDTO>> response = this.actionService.findByClientId(clientResponse.getData().getId(), page, size);
        return new ResponseEntity<ResponseEntityDTO<List<ActionDTO>>>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/actions")
    @ApiOperation(value = "Route to acquire Actions for a Client in the API", produces = "application/json")
    public ResponseEntity<ActionsAcquiredResponseDTO> acquiredActions (@PathVariable Long id,
                   @RequestParam @NotNull @ApiParam(value = "The total amount to acquire actions") Double totalAmount) throws EntityNotFoundException {
        ResponseEntityDTO<ClientDTO> clientResponse = this.clientService.findById(id);
        /**
         * TODO Carregar companies em memória
         */
        List<CompanyDTO> activeCompanies = (List<CompanyDTO>) this.companyService.findByStatusActiveOrderByPriceAsc().getData();
        ActionsAcquiredResponseDTO response = this.actionService.acquiredActions(totalAmount, clientResponse.getData(), activeCompanies);
        return new ResponseEntity<ActionsAcquiredResponseDTO>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Route to update a Client by Id in the API", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityDTO<ClientDTO>> update (@PathVariable Long id, @RequestBody @Valid ClientDTO client) throws EntityNotFoundException {
        return new ResponseEntity<ResponseEntityDTO<ClientDTO>>(this.clientService.update(id, client), HttpStatus.OK);
    }

}

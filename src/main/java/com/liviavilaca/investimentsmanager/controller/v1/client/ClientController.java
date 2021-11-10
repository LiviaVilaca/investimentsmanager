package com.liviavilaca.investimentsmanager.controller.v1.client;

import com.liviavilaca.investimentsmanager.dto.model.client.ClientDTO;
import com.liviavilaca.investimentsmanager.dto.response.MessageResponseDTO;
import com.liviavilaca.investimentsmanager.dto.response.ResponseEntityDTO;
import com.liviavilaca.investimentsmanager.exception.EntityNotFoundException;
import com.liviavilaca.investimentsmanager.service.client.ClientService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @ApiOperation(value = "Route to create a Client in the API", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityDTO<ClientDTO>> create (@RequestBody @Valid ClientDTO client){
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

    @PutMapping("/{id}")
    @ApiOperation(value = "Route to update a Client by Id in the API", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityDTO<ClientDTO>> update (@PathVariable Long id, @RequestBody @Valid ClientDTO client) throws EntityNotFoundException {
        return new ResponseEntity<ResponseEntityDTO<ClientDTO>>(this.clientService.update(id, client), HttpStatus.OK);
    }
}

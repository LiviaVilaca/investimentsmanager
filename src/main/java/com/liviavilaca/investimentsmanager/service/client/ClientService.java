package com.liviavilaca.investimentsmanager.service.client;

import com.liviavilaca.investimentsmanager.dto.mapper.client.ClientMapper;
import com.liviavilaca.investimentsmanager.dto.model.client.ClientDTO;
import com.liviavilaca.investimentsmanager.dto.response.MessageResponseDTO;
import com.liviavilaca.investimentsmanager.dto.response.ResponseEntityDTO;
import com.liviavilaca.investimentsmanager.exception.EntityNotFoundException;
import com.liviavilaca.investimentsmanager.model.client.Client;
import com.liviavilaca.investimentsmanager.repository.client.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    private final ClientMapper clientMapper = ClientMapper.INSTANCE;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ResponseEntityDTO<ClientDTO> create(ClientDTO clientDTO) {
        Client savedClient = this.clientRepository.save(clientMapper.toModel(clientDTO));
        ResponseEntityDTO<ClientDTO> response = new ResponseEntityDTO<>();
        response.setData(clientMapper.toDTO(savedClient));
        return response;
    }

    @Cacheable(value = "clients", key="#id")
    public ResponseEntityDTO<ClientDTO> findById(Long id) throws EntityNotFoundException {
        Client client = this.clientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Client.class, id));
        ResponseEntityDTO<ClientDTO> response = new ResponseEntityDTO<>();
        response.setData(clientMapper.toDTO(client));
        return response;
    }

    @Cacheable(value = "authentication", key="#email")
    public ResponseEntityDTO<ClientDTO> findByEmail(String email) throws EntityNotFoundException {
        Client client = this.clientRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(Client.class));
        ResponseEntityDTO<ClientDTO> response = new ResponseEntityDTO<>();
        response.setData(clientMapper.toDTO(client));
        return response;
    }

    public ResponseEntityDTO findAll(int page, int size) {
        Pageable pageable =  PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Client> clients = this.clientRepository.findAll(pageable) ;
        ResponseEntityDTO listEntityResponseDTO = ResponseEntityDTO.builder()
                .totalData(clients.getTotalElements())
                .data(clients.getContent().stream().map(clientMapper::toDTO).collect(Collectors.toList()))
                .build();
        return listEntityResponseDTO;
    }

    @Caching(evict = {
            @CacheEvict(value = "authentication", allEntries = true),
            @CacheEvict(value="clients", key="#id") })
    public MessageResponseDTO deleteById(Long id) throws EntityNotFoundException {
        try{
            this.clientRepository.deleteById(id);
            return MessageResponseDTO.builder().message("Client with id "+id+" deleted!").build();
        } catch (EmptyResultDataAccessException e){
            throw new EntityNotFoundException(Client.class, id);
        }
    }


    @Caching(evict = {
            @CacheEvict(value = "authentication", allEntries = true)},
            put = {
            @CachePut(value = "clients", key="#id")
            })
    public ResponseEntityDTO<ClientDTO> update (Long id, ClientDTO companyDTO) throws EntityNotFoundException {
        Client client = clientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Client.class, id));
        clientMapper.updateClientFromDTO(companyDTO, client);
        Client updatedClient = clientRepository.save(client);

        ResponseEntityDTO<ClientDTO> response = new ResponseEntityDTO<>();
        response.setData(clientMapper.toDTO(updatedClient));
        return response;
    }

}

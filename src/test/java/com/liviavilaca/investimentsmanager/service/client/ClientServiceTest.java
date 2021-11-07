package com.liviavilaca.investimentsmanager.service.client;

import com.liviavilaca.investimentsmanager.dto.mapper.client.ClientMapper;
import com.liviavilaca.investimentsmanager.dto.model.client.ClientDTO;
import com.liviavilaca.investimentsmanager.dto.response.MessageResponseDTO;
import com.liviavilaca.investimentsmanager.dto.response.ResponseEntityDTO;
import com.liviavilaca.investimentsmanager.exception.EntityNotFoundException;
import com.liviavilaca.investimentsmanager.model.client.Client;
import com.liviavilaca.investimentsmanager.repository.client.ClientRepository;
import com.liviavilaca.investimentsmanager.util.client.ClientUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private ClientMapper clientMapper = ClientMapper.INSTANCE;

    @Test
    void whenGivenExistingIdThenReturnThisClient() throws EntityNotFoundException {
        Client expectedFoundClient = ClientUtils.createFakeClient();

        when(clientRepository.findById(expectedFoundClient.getId()))
                .thenReturn(Optional.of(expectedFoundClient));

        ResponseEntityDTO<ClientDTO> clientDTO = clientService.findById(expectedFoundClient.getId());

        assertEquals(expectedFoundClient.getName(), clientDTO.getData().getName());
        assertEquals(expectedFoundClient.getCpf(), clientDTO.getData().getCpf());
        assertEquals(expectedFoundClient.getEmail(), clientDTO.getData().getEmail());
    }

    @Test
    void whenGivenAbsentIdThenNotFindThrowAnException() {
        var invalidId = 123456L;
        when(clientRepository.findById(invalidId)).thenReturn(Optional.ofNullable(any(Client.class)));

        assertThrows(EntityNotFoundException.class, () -> clientService.findById(invalidId));
    }

    @Test
    void whenCreateClientThenReturnThisClient() {
        ClientDTO clientDTO = ClientUtils.createFakeClientDTO();
        Client expectedCreatedClient = clientMapper.toModel(clientDTO);
        when(clientRepository.save(expectedCreatedClient))
                .thenReturn(expectedCreatedClient);

        ResponseEntityDTO<ClientDTO> actualResponse = clientService.create(clientDTO);

        assertEquals(expectedCreatedClient.getName(), actualResponse.getData().getName());
        assertEquals(expectedCreatedClient.getCpf(), actualResponse.getData().getCpf());
        assertEquals(expectedCreatedClient.getEmail(), actualResponse.getData().getEmail());
    }

    @Test
    void whenDeleteClientReturnSuccessfulMessage() throws EntityNotFoundException {
        var validId = 1L;

        doNothing().when(clientRepository).deleteById(validId);
        MessageResponseDTO response = clientService.deleteById(validId);
        assertEquals("Client with id "+validId+" deleted!", response.getMessage());
    }
}

package com.liviavilaca.investimentsmanager.service.security;

import com.liviavilaca.investimentsmanager.dto.model.client.ClientDTO;
import com.liviavilaca.investimentsmanager.exception.EntityNotFoundException;
import com.liviavilaca.investimentsmanager.model.security.JwtUser;
import com.liviavilaca.investimentsmanager.service.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private ClientService clientService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            ClientDTO client = clientService.findByEmail(email).getData();
            return JwtUser.builder().id(client.getId()).username(client.getEmail()).password(client.getPassword()).build();
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException("User/Email not found.");
        }
    }
}

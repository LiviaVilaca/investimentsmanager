package com.liviavilaca.investimentsmanager.util.client;

import com.github.javafaker.Faker;
import com.liviavilaca.investimentsmanager.dto.model.client.ClientDTO;
import com.liviavilaca.investimentsmanager.model.client.Client;
import com.liviavilaca.investimentsmanager.util.security.BcryptUtil;

public class ClientUtils {

    private static final Faker faker = Faker.instance();

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = BcryptUtil.getHash("123456");

    public static ClientDTO createFakeClientDTO(){
        return ClientDTO.builder()
                .id(faker.number().randomNumber())
                .name(faker.name().fullName())
                .password(PASSWORD)
                .cpf(String.valueOf(faker.number().randomNumber(11, true)))
                .email(EMAIL).build();
    }

    public static Client createFakeClient(){
        return Client.builder()
                .id(faker.number().randomNumber())
                .name(faker.name().fullName())
                .password(PASSWORD)
                .cpf(String.valueOf(faker.number().randomNumber(11, true)))
                .email(EMAIL).build();
    }
}

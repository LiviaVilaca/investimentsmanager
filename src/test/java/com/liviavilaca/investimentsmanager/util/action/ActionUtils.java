package com.liviavilaca.investimentsmanager.util.action;

import com.github.javafaker.Faker;
import com.liviavilaca.investimentsmanager.dto.model.action.ActionDTO;
import com.liviavilaca.investimentsmanager.dto.model.company.CompanyDTO;
import com.liviavilaca.investimentsmanager.model.action.Action;
import com.liviavilaca.investimentsmanager.util.client.ClientUtils;
import com.liviavilaca.investimentsmanager.util.company.CompanyUtils;

public class ActionUtils {

    private static final Faker faker = Faker.instance();

    public static ActionDTO createFakeActionDTO(){
        return ActionDTO.builder()
                .id(faker.number().randomNumber())
                .companyId(faker.number().randomNumber())
                .companyPrice(faker.number().randomDouble(3, 0, 100))
                .clientId(faker.number().randomNumber())
                .quantity(faker.number().randomNumber())
                .build();
    }

    public static Action createFakeAction(){
        return Action.builder()
                .id(faker.number().randomNumber())
                .company(CompanyUtils.createFakeCompany())
                .client(ClientUtils.createFakeClient())
                .quantity(faker.number().randomNumber())
                .build();
    }
}

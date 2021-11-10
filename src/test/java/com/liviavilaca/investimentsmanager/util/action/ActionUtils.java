package com.liviavilaca.investimentsmanager.util.action;

import com.github.javafaker.Faker;
import com.liviavilaca.investimentsmanager.dto.model.action.ActionDTO;
import com.liviavilaca.investimentsmanager.model.action.Action;
import com.liviavilaca.investimentsmanager.model.company.Company;
import com.liviavilaca.investimentsmanager.util.company.CompanyUtils;

import java.math.BigDecimal;

public class ActionUtils {

    private static final Faker faker = Faker.instance();

    public static ActionDTO createFakeActionDTO(){
        long quantity = faker.number().randomNumber();
        double companyPrice = faker.number().randomDouble(3, 0, 100);
        return ActionDTO.builder()
                .id(faker.number().randomNumber())
                .quantity(quantity)
                .companyId(faker.number().randomNumber())
                .companyPrice(companyPrice)
                //.acquisitionId(faker.number().randomNumber())
                .totalSpent(companyPrice*quantity)
                .build();
    }

    public static Action createFakeAction(){
        long quantity = faker.number().randomNumber();
        Company company = CompanyUtils.createFakeCompany();
        return Action.builder()
                .id(faker.number().randomNumber())
                .quantity(quantity)
                .company(company)
                .totalSpent(BigDecimal.valueOf(company.getPrice().doubleValue()*quantity))
                //.acquisition(AcquisitionUtils.createFakeAcquisition())
                .build();
    }
}

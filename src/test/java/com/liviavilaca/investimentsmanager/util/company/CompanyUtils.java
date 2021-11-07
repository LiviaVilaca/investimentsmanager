package com.liviavilaca.investimentsmanager.util.company;

import com.github.javafaker.Faker;
import com.liviavilaca.investimentsmanager.dto.model.company.CompanyDTO;
import com.liviavilaca.investimentsmanager.model.company.Company;

import java.math.BigDecimal;

public class CompanyUtils {

    private static final Faker faker = Faker.instance();

    private static final String TICKER = "CMPN123";

    public static CompanyDTO createFakeCompanyDTO(){
        return CompanyDTO.builder()
                .id(faker.number().randomNumber())
                .name(faker.company().name())
                .ticker(TICKER)
                .price(faker.number().randomDouble(3, 0, 100))
                .status(true)
                .build();
    }

    public static Company createFakeCompany(){
        return Company.builder()
                .id(faker.number().randomNumber())
                .name(faker.company().name())
                .ticker(TICKER)
                .price(BigDecimal.valueOf(faker.number().randomDouble(3, 0, 100)))
                .status(true)
                .build();
    }

}

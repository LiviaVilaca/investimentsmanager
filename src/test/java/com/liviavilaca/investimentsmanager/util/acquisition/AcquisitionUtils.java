package com.liviavilaca.investimentsmanager.util.acquisition;

import com.github.javafaker.Faker;
import com.liviavilaca.investimentsmanager.dto.model.action.ActionDTO;
import com.liviavilaca.investimentsmanager.dto.model.acquisition.AcquisitionDTO;
import com.liviavilaca.investimentsmanager.model.action.Action;
import com.liviavilaca.investimentsmanager.model.acquisition.Acquisition;
import com.liviavilaca.investimentsmanager.util.action.ActionUtils;
import com.liviavilaca.investimentsmanager.util.client.ClientUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

public class AcquisitionUtils {

    private static final Faker faker = Faker.instance();

    public static AcquisitionDTO createFakeAcquisitionDTO(){
        ActionDTO action = ActionUtils.createFakeActionDTO();
        action.setId(null);
        action.setAcquisitionId(null);
        return AcquisitionDTO.builder()
                .id(faker.number().randomNumber())
                .actions(Arrays.asList(new ActionDTO[]{action}))
                .totalSpent(action.getTotalSpent())
                .exchange(0.5)
                .clientId(faker.number().randomNumber())
                .build();
    }

    public static Acquisition createFakeAcquisition(){
        Action action = ActionUtils.createFakeAction();
        action.setAcquisition(null);
        action.setId(null);
        return Acquisition.builder()
                .id(faker.number().randomNumber())
                .totalSpent(action.getTotalSpent())
                .exchange(BigDecimal.valueOf(0.5))
                .client(ClientUtils.createFakeClient())
                .actions(Arrays.asList(new Action[]{action}))
                .build();
    }

}

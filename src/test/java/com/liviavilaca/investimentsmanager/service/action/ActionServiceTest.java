package com.liviavilaca.investimentsmanager.service.action;

import com.liviavilaca.investimentsmanager.dto.mapper.action.ActionMapper;
import com.liviavilaca.investimentsmanager.dto.model.action.ActionDTO;
import com.liviavilaca.investimentsmanager.dto.response.ResponseEntityDTO;
import com.liviavilaca.investimentsmanager.model.action.Action;
import com.liviavilaca.investimentsmanager.repository.action.ActionRepository;
import com.liviavilaca.investimentsmanager.util.action.ActionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActionServiceTest {

    @Mock
    private ActionRepository actionRepository;

    @InjectMocks
    private ActionService actionService;

    private ActionMapper actionMapper = ActionMapper.INSTANCE;

    @Test
    void whenCreateActionThenReturnThisAction() {
        ActionDTO actionDTO = ActionUtils.createFakeActionDTO();
        Action expectedCreatedAction = actionMapper.toModel(actionDTO);
        when(actionRepository.save(expectedCreatedAction))
                .thenReturn(expectedCreatedAction);

        ResponseEntityDTO<ActionDTO> actualResponse = actionService.create(actionDTO);

        assertEquals(expectedCreatedAction.getCompany().getId(), actualResponse.getData().getCompanyId());
        assertEquals(expectedCreatedAction.getTotalSpent().doubleValue(), actualResponse.getData().getTotalSpent());
        assertEquals(expectedCreatedAction.getQuantity(), actualResponse.getData().getQuantity());
    }

}

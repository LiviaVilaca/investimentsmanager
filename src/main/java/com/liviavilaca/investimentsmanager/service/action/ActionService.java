package com.liviavilaca.investimentsmanager.service.action;

import com.liviavilaca.investimentsmanager.dto.model.action.ActionDTO;
import com.liviavilaca.investimentsmanager.dto.response.ResponseEntityDTO;
import com.liviavilaca.investimentsmanager.model.action.Action;
import com.liviavilaca.investimentsmanager.dto.mapper.action.ActionMapper;
import com.liviavilaca.investimentsmanager.repository.action.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.stream.Collectors;

@Service
public class ActionService {

    private ActionRepository actionRepository;

    private final ActionMapper actionMapper = ActionMapper.INSTANCE;

    @Autowired
    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public ResponseEntityDTO<ActionDTO> create (@NotNull ActionDTO actionDTO){
        Action savedAction = this.actionRepository.save(actionMapper.toModel(actionDTO));
        ResponseEntityDTO<ActionDTO> response = new ResponseEntityDTO<>();
        response.setData(actionMapper.toDTO(savedAction));
        return response;
    }

    public ResponseEntityDTO findAll(int page, int size) {
        Pageable pageable =  PageRequest.of(page, size, Sort.by("quantity").descending());
        Page<Action> actions = this.actionRepository.findAll(pageable) ;
        ResponseEntityDTO listEntityResponseDTO = ResponseEntityDTO.builder()
                .totalData(actions.getTotalElements())
                .data(actions.getContent().stream().map(actionMapper::toDTO).collect(Collectors.toList()))
                .build();
        return listEntityResponseDTO;
    }

}

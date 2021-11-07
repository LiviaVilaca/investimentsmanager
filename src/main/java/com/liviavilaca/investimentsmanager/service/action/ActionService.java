package com.liviavilaca.investimentsmanager.service.action;

import com.liviavilaca.investimentsmanager.dto.model.action.ActionDTO;
import com.liviavilaca.investimentsmanager.dto.model.client.ClientDTO;
import com.liviavilaca.investimentsmanager.dto.model.company.CompanyDTO;
import com.liviavilaca.investimentsmanager.dto.response.ActionsAcquiredResponseDTO;
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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@Service
public class ActionService {

    private ActionRepository actionRepository;

    private final ActionMapper actionMapper = ActionMapper.INSTANCE;

    @Autowired
    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public ResponseEntityDTO<List<ActionDTO>> findByClientId (Long clientId, int page, int size){
        Pageable pageable =  PageRequest.of(page, size, Sort.by("quantity").descending());
        Page<Action> actions = this.actionRepository.findByClientId(clientId, pageable);

        ResponseEntityDTO<List<ActionDTO>> listEntityResponseDTO = new ResponseEntityDTO<>();
        listEntityResponseDTO.setData(actions.getContent().stream().map(actionMapper::toDTO).collect(Collectors.toList()));
        listEntityResponseDTO.setTotalData(actions.getTotalElements());
        return listEntityResponseDTO;
    }

    public ActionsAcquiredResponseDTO acquiredActions(Double totalAmount, ClientDTO clientDTO, List<CompanyDTO> activeCompanies){
        HashMap<Integer, ActionDTO> actionDTOHashMap = new HashMap<Integer, ActionDTO>();
        Double exchange = distributeActionsByCompanies(totalAmount, clientDTO, activeCompanies, actionDTOHashMap);

        for(ActionDTO actionDTO : actionDTOHashMap.values()){
            Action savedAction = this.actionRepository.save(actionMapper.toModel(actionDTO));
            actionDTO.setId(savedAction.getId());
            actionDTO.setTotalSpent(actionDTO.getQuantity()*actionDTO.getCompanyPrice().doubleValue());
        }
        return  ActionsAcquiredResponseDTO.builder()
                .actionsAcquired(actionDTOHashMap.values().stream().collect(Collectors.toList()))
                .exchange(exchange)
                .totalSpent(totalAmount-exchange)
                .build();
    }

    private Double distributeActionsByCompanies (Double totalAmount, ClientDTO clientDTO, List<CompanyDTO> activeCompanies, @NotNull HashMap<Integer, ActionDTO> actionDTOHashMap){

        if(activeCompanies==null || activeCompanies.isEmpty() || clientDTO == null)
            return totalAmount;

        Double totalPriceCompanies = activeCompanies.stream().mapToDouble(companyDTO -> companyDTO.getPrice().doubleValue()).sum();
        Long quantityActionsByCompany = 0L;
        if(totalAmount > totalPriceCompanies){
            quantityActionsByCompany = Long.valueOf(((Double)(totalAmount/totalPriceCompanies)).intValue());
            totalAmount = totalAmount - (totalPriceCompanies*quantityActionsByCompany);
        }

        ActionDTO actionDTO;
        if(quantityActionsByCompany>0){
            for(int i = 0; i < activeCompanies.size(); i++){
                actionDTO = ActionDTO.builder()
                        .companyId(activeCompanies.get(i).getId())
                        .companyPrice(activeCompanies.get(i).getPrice())
                        .clientId(clientDTO.getId())
                        .quantity(quantityActionsByCompany).build();
                actionDTOHashMap.put(i, actionDTO);
            }
        }

        Double exchange = totalAmount;

        Stack<Integer> path = new Stack<Integer>();
        CompanyDTO currentCompany;
        CompanyDTO lastCompany;
        for(int i = 0; i < activeCompanies.size();) {
            currentCompany = activeCompanies.get(i);
            if (exchange >= currentCompany.getPrice().doubleValue()) {
                exchange -= currentCompany.getPrice().doubleValue();
                path.push(i);
            } else {
                if (i == 0 && exchange < currentCompany.getPrice().doubleValue()) {
                    break;
                }

                Integer lastIndex = path.peek();
                if (lastIndex != null) {
                    lastCompany = activeCompanies.get(path.peek());
                    if (lastCompany.getPrice().doubleValue() + exchange >= currentCompany.getPrice().doubleValue()) {
                        path.pop();
                        exchange += lastCompany.getPrice().doubleValue();
                        continue;
                    }
                }
                i = 0;
                continue;
            }
            if (exchange == 0) {
                break;
            }
            i++;
        }

        path.stream().forEach(companyIndex ->  {
            actionDTOHashMap.compute(companyIndex, (i, action) -> {
                if (action != null)
                    action.setQuantity(action.getQuantity() + 1);
                else
                    action = ActionDTO.builder()
                            .companyId(activeCompanies.get(i).getId())
                            .companyPrice(activeCompanies.get(i).getPrice())
                            .clientId(clientDTO.getId())
                            .quantity(1L).build();
                return action;
            });
        });
        return exchange;
    }

}

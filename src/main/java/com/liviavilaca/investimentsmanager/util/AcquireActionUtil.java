package com.liviavilaca.investimentsmanager.util;

import com.liviavilaca.investimentsmanager.dto.model.action.ActionDTO;
import com.liviavilaca.investimentsmanager.dto.model.client.ClientDTO;
import com.liviavilaca.investimentsmanager.dto.model.company.CompanyDTO;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class AcquireActionUtil {

    public static List<ActionDTO> distributeActionsByAllCompanies (Double totalAmount, List<CompanyDTO> activeCompanies){

        if(activeCompanies==null || activeCompanies.isEmpty())
            return null;

        Double totalPriceCompanies = activeCompanies.stream().mapToDouble(companyDTO -> companyDTO.getPrice().doubleValue()).sum();
        Long quantityActionsByCompany = 0L;
        if(totalAmount > totalPriceCompanies){
            quantityActionsByCompany = ((Double)(totalAmount/totalPriceCompanies)).longValue();
            totalAmount = totalAmount - (totalPriceCompanies*quantityActionsByCompany);
        }

        ActionDTO actionDTO;
        HashMap<Integer, ActionDTO> actionDTOHashMap = new HashMap<>();
        if(quantityActionsByCompany>0){
            for(int i = 0; i < activeCompanies.size(); i++){
                actionDTO = ActionDTO.builder()
                        .companyId(activeCompanies.get(i).getId())
                        .companyPrice(activeCompanies.get(i).getPrice())
                        .quantity(quantityActionsByCompany)
                        .totalSpent(quantityActionsByCompany*activeCompanies.get(i).getPrice())
                        .build();
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
                if (action != null){
                    action.setQuantity(action.getQuantity() + 1);
                    action.setTotalSpent(action.getTotalSpent()+action.getCompanyPrice());
                } else
                    action = ActionDTO.builder()
                            .companyId(activeCompanies.get(i).getId())
                            .companyPrice(activeCompanies.get(i).getPrice())
                            .quantity(1L)
                            .totalSpent(activeCompanies.get(i).getPrice())
                            .build();
                return action;
            });
        });
        return actionDTOHashMap.values().stream().toList();
    }
}

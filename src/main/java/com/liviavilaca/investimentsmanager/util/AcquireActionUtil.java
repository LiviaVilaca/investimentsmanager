package com.liviavilaca.investimentsmanager.util;

import com.liviavilaca.investimentsmanager.dto.model.action.ActionDTO;
import com.liviavilaca.investimentsmanager.dto.model.company.CompanyDTO;
import com.liviavilaca.investimentsmanager.model.company.Company;
import org.paukov.combinatorics3.Generator;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class AcquireActionUtil {

    public static List<ActionDTO> distributeActionsByCompanies(Double totalAmount, Integer totalCompaniesToInvest, List<CompanyDTO> activeCompanies){
        if(activeCompanies==null || activeCompanies.isEmpty())
            return null;

        Integer totalCompanies = activeCompanies.size();
        if (totalCompaniesToInvest > totalCompanies) {
            totalCompaniesToInvest = totalCompanies;
        }

        List<List<Long>> companiesCombinations = Generator.combination(activeCompanies.stream().map(companyDTO -> companyDTO.getId()).collect(Collectors.toList()))
                .simple(totalCompaniesToInvest).stream().collect(Collectors.toList());

        HashMap<Long, Long> bestActionsQuantity = pickBestCombination(totalAmount, companiesCombinations, activeCompanies);

        return createActions(bestActionsQuantity, activeCompanies);
    }

    public static List<ActionDTO> distributeActionsByAllCompanies(Double totalAmount, List<CompanyDTO> activeCompanies){
        if(activeCompanies==null || activeCompanies.isEmpty())
            return null;
        HashMap<Long, Long> actionsQuantity = new HashMap<>();
        computeExchange(totalAmount, activeCompanies, actionsQuantity);
        return createActions(actionsQuantity, activeCompanies);
    }

    private static HashMap<Long, Long> pickBestCombination(Double totalAmount, List<List<Long>> companiesCombinations, List<CompanyDTO> activeCompanies){
        HashMap<Long, Long> bestActionsQuantity = new HashMap<>();
        Double minorExchange = totalAmount;
        Integer bestTotalActions = 0;

        HashMap<Long, Long> currentActionsQuantity;
        Double currentExchange;
        List<CompanyDTO> currentCompanies;
        Integer currentTotalActions;

        for(List<Long> companiesIds : companiesCombinations){
            currentCompanies = activeCompanies.stream().filter(company -> { return companiesIds.contains(company.getId()); } ).collect(Collectors.toList());
            if(totalAmount < currentCompanies.get(0).getPrice())
                continue;

            currentActionsQuantity = new HashMap<>();
            currentExchange = computeExchange(totalAmount, currentCompanies, currentActionsQuantity);
            if(currentExchange <= minorExchange){
                currentTotalActions = currentActionsQuantity.values().stream().mapToInt(quantity -> quantity.intValue()).sum();
                if(currentExchange < minorExchange || currentTotalActions > bestTotalActions){
                    bestActionsQuantity = currentActionsQuantity;
                    bestTotalActions = currentTotalActions;
                }
                minorExchange = currentExchange;
            }
        }

        return bestActionsQuantity;
    }

    private static Double computeExchange (Double totalAmount, List<CompanyDTO> activeCompanies, @NotNull HashMap<Long, Long> actionsQuantity){
        Double totalPriceCompanies = activeCompanies.stream().mapToDouble(companyDTO -> companyDTO.getPrice().doubleValue()).sum();

        Long quantityActionsByCompany = 0L;
        if(totalAmount > totalPriceCompanies){
            quantityActionsByCompany = ((Double)(totalAmount/totalPriceCompanies)).longValue();
            totalAmount = totalAmount - (totalPriceCompanies*quantityActionsByCompany);
        }

        if(quantityActionsByCompany>0){
            for(CompanyDTO company : activeCompanies){
                actionsQuantity.put(company.getId(), quantityActionsByCompany);
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
            Long companyId = activeCompanies.get(companyIndex).getId();
            actionsQuantity.computeIfPresent(companyId, (i, quantity) -> quantity+=1);
            actionsQuantity.putIfAbsent(companyId, 1L);
        });

        return exchange;
    }

    private static List<ActionDTO> createActions(HashMap<Long, Long> actionsQuantity, List<CompanyDTO> activeCompanies){
        List<ActionDTO> actions = new ArrayList<>();
        Long quantity;
        ActionDTO action;
        CompanyDTO company;
        HashMap<Long, CompanyDTO> companiesById = new HashMap<>();
        activeCompanies.stream().forEach(c -> { companiesById.put(c.getId(), c); });

        for(Long companyId : actionsQuantity.keySet()){
            quantity = actionsQuantity.get(companyId);
            company = companiesById.get(companyId);
            action = ActionDTO.builder()
                    .companyId(company.getId())
                    .companyPrice(company.getPrice())
                    .quantity(quantity)
                    .totalSpent(company.getPrice()*quantity)
                    .build();
            actions.add(action);
        }
        return actions;
    }

}

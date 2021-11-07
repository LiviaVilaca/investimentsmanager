package com.liviavilaca.investimentsmanager.dto.response;

import com.liviavilaca.investimentsmanager.dto.model.action.ActionDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Response")
public class ActionsAcquiredResponseDTO {
    private List<ActionDTO> actionsAcquired;
    private Double exchange;
    private Double totalSpent;
}

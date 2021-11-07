package com.liviavilaca.investimentsmanager.dto.model.action;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Action")
public class ActionDTO {

    private Long id;

    @NotNull(message = "Actions quantity cannot be null")
    private Long quantity;

    @NotNull(message="Company Id cannot be null")
    private Long companyId;

    @NotNull(message="Company Price cannot be null")
    private Double companyPrice;

    @NotNull(message="Client Id cannot be null")
    private Long clientId;

    private Double totalSpent;

}

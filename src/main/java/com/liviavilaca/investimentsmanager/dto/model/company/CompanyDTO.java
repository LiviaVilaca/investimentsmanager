package com.liviavilaca.investimentsmanager.dto.model.company;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Company")
public class CompanyDTO {

    private Long id;

    @NotBlank(message = "Ticker cannot be blank")
    @Size(max= 200, message = "Ticker is too long. Only 10 characters are allowed")
    private String ticker;

    @NotBlank(message = "Name cannot be blank")
    @Size(max= 200, message = "Name is too long. Only 200 characters are allowed")
    private String name;

    @NotNull(message = "Price cannot be null")
    @PositiveOrZero(message = "Price must be a positive value")
    private Double price;

    private Boolean status;
}

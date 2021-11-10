package com.liviavilaca.investimentsmanager.dto.model.company;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.liviavilaca.investimentsmanager.util.validation.ValidateOnInsert;
import com.liviavilaca.investimentsmanager.util.validation.ValidateOnUpdate;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Company")
public class CompanyDTO {

    private Long id;

    @NotBlank(message = "Ticker cannot be blank", groups = {ValidateOnInsert.class})
    @Size(max= 200, min=1, message = "Ticker is too long. Only 10 characters are allowed", groups = {ValidateOnInsert.class, ValidateOnUpdate.class})
    @Pattern(regexp = "^.*[a-zA-Z].*$",
            message = "Ticker invalid.", groups = {ValidateOnUpdate.class})
    private String ticker;

    @NotBlank(message = "Name cannot be blank", groups = {ValidateOnInsert.class})
    @Size(max= 200, message = "Name is too long. Only 200 characters are allowed", groups = {ValidateOnInsert.class, ValidateOnUpdate.class})
    @Pattern(regexp = "^.*[a-zA-Z].*$",
            message = "Name invalid", groups = {ValidateOnUpdate.class})
    private String name;

    @NotNull(message = "Price cannot be null", groups = {ValidateOnInsert.class})
    @PositiveOrZero(message = "Price must be a positive value", groups = {ValidateOnInsert.class, ValidateOnUpdate.class})
    private Double price;

    private Boolean status;
}

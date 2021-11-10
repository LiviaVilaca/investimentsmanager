package com.liviavilaca.investimentsmanager.dto.model.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "User")
public class JwtUserDTO {

    @NotNull
    @NotEmpty(message = "Enter an email")
    private String email;

    @NotNull
    @NotEmpty(message = "Enter a password")
    private String password;
}

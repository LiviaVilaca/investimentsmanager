package com.liviavilaca.investimentsmanager.dto.model.security;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Token")
public class JwtTokenDTO {

    @Getter
    String token;
}

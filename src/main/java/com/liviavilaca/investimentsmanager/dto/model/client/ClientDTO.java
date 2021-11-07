package com.liviavilaca.investimentsmanager.dto.model.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liviavilaca.investimentsmanager.util.security.BcryptUtil;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Client")
public class ClientDTO {

    private Long id;

    @NotNull(message = "CPF cannot be null")
    @Pattern(regexp = "^\\d{11}$",
            message = "CPF invalid format. Must contains 11 numbers")
    private String cpf;

    @NotBlank(message = "Email cannot be blank")
    @Size(max= 100)
    @Pattern(regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$",
                message = "Email invalid format")
    private String email;

    @NotNull(message = "Password cannot be null.")
    @Length(min=6, message="Password must contain at least 6 characters.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "Name cannot be blank")
    @Size(max= 200, message = "Name is too long. Only 200 characters are allowed")
    private String name;

    private Integer age;

    public String getPassword() {
        return BcryptUtil.getHash(this.password);
    }

}

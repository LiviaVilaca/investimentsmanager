package com.liviavilaca.investimentsmanager.dto.model.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liviavilaca.investimentsmanager.enumeration.RoleEnum;
import com.liviavilaca.investimentsmanager.util.security.BcryptUtil;
import com.liviavilaca.investimentsmanager.util.validation.ValidateOnInsert;
import com.liviavilaca.investimentsmanager.util.validation.ValidateOnUpdate;
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

    @NotNull(message = "CPF cannot be null", groups = {ValidateOnInsert.class})
    @Pattern(regexp = "^\\d{11}$",
            message = "CPF invalid format. Must contains 11 numbers", groups = {ValidateOnInsert.class, ValidateOnUpdate.class})
    private String cpf;

    @NotBlank(message = "Email cannot be blank", groups = {ValidateOnInsert.class})
    @Size(max= 100, groups = {ValidateOnInsert.class, ValidateOnUpdate.class})
    @Pattern(regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$",
                message = "Email invalid format", groups = {ValidateOnInsert.class, ValidateOnUpdate.class})
    private String email;

    @NotNull(message = "Password cannot be null.", groups = {ValidateOnInsert.class})
    @Length(min=6, message="Password must contain at least 6 characters.", groups = {ValidateOnInsert.class, ValidateOnUpdate.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "Name cannot be blank", groups = {ValidateOnInsert.class})
    @Size(max= 200, message = "Name is too long. Only 200 characters are allowed", groups = {ValidateOnInsert.class, ValidateOnUpdate.class})
    @Pattern(regexp = "^.*[a-zA-Z].*$",
            message = "Name invalid", groups = {ValidateOnUpdate.class})
    private String name;

    private Integer age;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private RoleEnum role;

    public String getPassword() {
        return BcryptUtil.getHash(this.password);
    }

}

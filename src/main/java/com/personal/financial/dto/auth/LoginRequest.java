package com.personal.financial.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "El usuario o email es requerido")
    @Size(max = 100, message = "El usuario o email no puede exceder los 100 caracteres")
    private String identifier;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, max = 255, message = "La contraseña debe tener mas de 6 caracteres")
    private String password;

}

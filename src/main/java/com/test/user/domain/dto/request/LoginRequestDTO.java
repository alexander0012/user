package com.test.user.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "El correo no puede estar vacio")
        @Email(message = "El formato del correo no es valido")
        String email,

        @NotBlank(message = "La contraseña no puede estar vacia")
        String password
) {}
package com.test.user.domain.dto.request;

import com.test.user.validation.ValidEmailFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UsuarioRequestDTO(
        @NotBlank(message = "El nombre no puede estar vacio")
        String name,

        @NotBlank(message = "El correo no puede estar vacio")
        @ValidEmailFormat
        String email,

        @NotBlank(message = "La contraseña no puede estar vacia")
        String password,

        @NotEmpty(message = "La lista de telefonos no puede estar vacia")
        List<PhoneRequestDTO> phones
)
{}

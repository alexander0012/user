package com.test.user.domain.dto.request;

import com.test.user.validation.ValidEmailFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UsuarioRequestDTO(
        @NotBlank(message = "El nombre no puede estar vacio")
        String name,

        @NotNull(message = "La edad no puede estar vacia")
        @Min(value = 15, message = "La edad debe ser un número positivo")
        Integer edad,

        @NotBlank(message = "El correo no puede estar vacio")
        @ValidEmailFormat
        String email,

        @NotBlank(message = "La contraseña no puede estar vacia")
        String password,

        @NotEmpty(message = "La lista de telefonos no puede estar vacia")
        List<PhoneRequestDTO> phones
)
{}

package com.test.user.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PhoneRequestDTO(
        @NotBlank(message = "El numero no puede estar vacio")
        String number,

        @NotBlank(message = "El codigo de ciudad no puede estar vacio")
        String cityCode,

        @NotBlank(message = "El codigo de pais no puede estar vacio")
        String contryCode
) {
}

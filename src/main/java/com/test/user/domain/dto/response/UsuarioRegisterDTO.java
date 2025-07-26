package com.test.user.domain.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UsuarioRegisterDTO(
        UUID id,
        Instant created,
        Instant modified,
        Instant lastLogin,
        String token,
        Boolean isactive
)
{}

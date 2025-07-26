package com.test.user.domain.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UsuarioDetailsResponseDTO (
        UUID id,
        String name,
        Instant created,
        Instant modified,
        Instant lastLogin,
        String token,
        Boolean isactive,
        String email,
        List<PhoneResponseDTO> phones
)
{}

package com.test.user.domain.dto.response;

import com.test.user.repository.entity.Usuario;

import java.time.Instant;
import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String name,
        String email,
        Instant created,
        Instant modified,
        Instant lastLogin,
        String token,
        boolean active
) {

    public UsuarioResponseDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getName(),
                usuario.getEmail(),
                usuario.getCreated(),
                usuario.getModified(),
                usuario.getLastLogin(),
                usuario.getToken(),
                usuario.isActive()
        );
    }
}

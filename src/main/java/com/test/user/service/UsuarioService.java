package com.test.user.service;

import com.test.user.domain.dto.request.LoginRequestDTO;
import com.test.user.domain.dto.request.PhoneRequestDTO;
import com.test.user.domain.dto.request.UsuarioRequestDTO;
import com.test.user.domain.dto.response.PhoneResponseDTO;
import com.test.user.domain.dto.response.UsuarioDetailsResponseDTO;
import com.test.user.domain.dto.response.UsuarioRegisterDTO;
import com.test.user.domain.dto.response.UsuarioResponseDTO;
import com.test.user.repository.UsuarioRepository;
import com.test.user.repository.entity.Phone;
import com.test.user.repository.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private  final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioRegisterDTO register(UsuarioRequestDTO request) {
        usuarioRepository.findByEmail(request.email()).ifPresent(u -> {
            throw new IllegalStateException("El correo ya registrado");
        });

        var user = Usuario.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .active(true)
                .lastLogin(Instant.now())
                .build();

        var phones = request.phones().stream()
                .map(phoneRequestDto -> mapPhoneDtoToEntity(phoneRequestDto, user))
                .collect(Collectors.toList());
        user.setPhones(phones);

        var jwtToken = jwtService.generateToken(user);
        user.setToken(jwtToken);

        var savedUser = usuarioRepository.save(user);

        return new UsuarioRegisterDTO(
                savedUser.getId(),
                savedUser.getCreated(),
                savedUser.getModified(),
                savedUser.getLastLogin(),
                savedUser.getToken(),
                savedUser.isActive());
    }

    public UsuarioRegisterDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalStateException("Error inesperado: usuario no encontrado después de la autenticación."));

        var newToken = jwtService.generateToken(user);

        user.setLastLogin(Instant.now());
        user.setToken(newToken);
        var savedUser =usuarioRepository.save(user);

        return new UsuarioRegisterDTO(
                savedUser.getId(),
                savedUser.getCreated(),
                savedUser.getModified(),
                savedUser.getLastLogin(),
                savedUser.getToken(),
                savedUser.isActive());
    }

    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll().stream().map(UsuarioResponseDTO::new).toList();
    }

    public List<UsuarioDetailsResponseDTO> findAllDetails() {
        return usuarioRepository.findAll().stream().map(
                user -> new UsuarioDetailsResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getCreated(),
                        user.getModified(),
                        user.getLastLogin(),
                        user.getToken(),
                        user.isActive(),
                        user.getEmail(),
                        user.getPhones().stream().map(PhoneResponseDTO::new).toList()
        )).toList();
    }

    private Phone mapPhoneDtoToEntity(PhoneRequestDTO dto, Usuario user) {
        return Phone.builder()
                .number(dto.number())
                .cityCode(dto.cityCode())
                .contryCode(dto.contryCode())
                .usuario(user)
                .build();
    }
}

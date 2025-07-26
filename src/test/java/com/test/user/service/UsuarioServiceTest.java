package com.test.user.service;

import com.test.user.domain.dto.request.LoginRequestDTO;
import com.test.user.domain.dto.request.PhoneRequestDTO;
import com.test.user.domain.dto.request.UsuarioRequestDTO;
import com.test.user.domain.dto.response.UsuarioRegisterDTO;
import com.test.user.domain.dto.response.UsuarioResponseDTO;
import com.test.user.repository.UsuarioRepository;
import com.test.user.repository.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioRequestDTO usuarioRequestDTO;
    private Usuario usuario;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    void setUp() {
        PhoneRequestDTO phoneDTO = new PhoneRequestDTO("1234567", "1", "57");
        usuarioRequestDTO = new UsuarioRequestDTO(
                "Test User",
                "aaaaaaaa@example.cl",
                "password123",
                List.of(phoneDTO)
        );

        usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("aaaaaaaa@example.cl")
                .password("encodedPassword")
                .active(true)
                .created(Instant.now())
                .modified(Instant.now())
                .lastLogin(Instant.now())
                .token("some-jwt-token")
                .build();

        loginRequestDTO = new LoginRequestDTO("aaaaaaaa@example.cl", "password123");
    }

    @Test
    void register_whenEmailIsNotRegistered_shouldCreateUser() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(Usuario.class))).thenReturn("new-jwt-token");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioRegisterDTO result = usuarioService.register(usuarioRequestDTO);
        assertNotNull(result);
        assertEquals(usuario.getId(), result.id());
        assertEquals(usuario.getToken(), result.token());
        assertTrue(result.isactive());

        verify(usuarioRepository).findByEmail("aaaaaaaa@example.cl");
        verify(passwordEncoder).encode("password123");
        verify(jwtService).generateToken(any(Usuario.class));
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void register_whenEmailIsRegistered_shouldThrowException() {
        when(usuarioRepository.findByEmail("aaaaaaaa@example.cl")).thenReturn(Optional.of(usuario));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            usuarioService.register(usuarioRequestDTO);
        });

        assertEquals("El correo ya registrado", exception.getMessage());
        verify(usuarioRepository).findByEmail("aaaaaaaa@example.cl");
        verify(passwordEncoder, never()).encode(anyString());
        verify(jwtService, never()).generateToken(any(Usuario.class));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void login_withValidCredentials_shouldReturnUserDTO() {
        when(usuarioRepository.findByEmail("aaaaaaaa@example.cl")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(any(Usuario.class))).thenReturn("new-refreshed-token");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioRegisterDTO result = usuarioService.login(loginRequestDTO);

        assertNotNull(result);
        assertEquals(usuario.getId(), result.id());
        assertEquals("new-refreshed-token", result.token());

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password())
        );
        verify(usuarioRepository).findByEmail("aaaaaaaa@example.cl");
        verify(jwtService).generateToken(usuario);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void login_whenUserNotFoundAfterAuth_shouldThrowException() {
        when(usuarioRepository.findByEmail("aaaaaaaa@example.cl")).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            usuarioService.login(loginRequestDTO);
        });

        assertEquals("Error inesperado: usuario no encontrado después de la autenticación.", exception.getMessage());
        verify(authenticationManager).authenticate(any());
        verify(usuarioRepository).findByEmail("aaaaaaaa@example.cl");
        verify(jwtService, never()).generateToken(any());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void findAll_whenUsersExist_shouldReturnUserList() {
        Usuario anotherUser = Usuario.builder().name("Another User").email("aaaaaaaa@example.cl").build();
        List<Usuario> userList = List.of(usuario, anotherUser);
        when(usuarioRepository.findAll()).thenReturn(userList);

        List<UsuarioResponseDTO> result = usuarioService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test User", result.get(0).name());
        assertEquals("aaaaaaaa@example.cl", result.get(1).email());

        verify(usuarioRepository).findAll();
    }

    @Test
    void findAll_whenNoUsersExist_shouldReturnEmptyList() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        List<UsuarioResponseDTO> result = usuarioService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(usuarioRepository).findAll();
    }
}

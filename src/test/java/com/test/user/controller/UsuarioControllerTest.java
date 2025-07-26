package com.test.user.controller;

import com.test.user.domain.dto.request.LoginRequestDTO;
import com.test.user.domain.dto.request.PhoneRequestDTO;
import com.test.user.domain.dto.request.UsuarioRequestDTO;
import com.test.user.domain.dto.response.UsuarioRegisterDTO;
import com.test.user.domain.dto.response.UsuarioResponseDTO;
import com.test.user.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @MockBean
    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private UsuarioResponseDTO usuarioResponseDTO;
    private UsuarioRequestDTO usuarioRequestDTO;
    private UsuarioRegisterDTO usuarioRegisterDTO;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    void setUp() {
        usuarioRequestDTO = new UsuarioRequestDTO(
                "Test User",
                "aaaaaaaa@example.cl",
                "Password123",
                List.of(new PhoneRequestDTO("1234567", "1", "57"))
        );

        usuarioRegisterDTO = new UsuarioRegisterDTO(
                UUID.randomUUID(),
                Instant.now(),
                Instant.now(),
                Instant.now(),
                "some.jwt.token",
                true
        );

        loginRequestDTO = new LoginRequestDTO("aaaaaaaa@example.cl", "Password123");
    }

    @Test
    void register_shouldCallServiceAndReturnOk() {
        when(usuarioService.register(usuarioRequestDTO)).thenReturn(usuarioRegisterDTO);

        ResponseEntity<?> response = usuarioController.register(usuarioRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioRegisterDTO, response.getBody());

        verify(usuarioService).register(usuarioRequestDTO);
    }

    @Test
    void login_shouldCallServiceAndReturnOk() {
        when(usuarioService.login(loginRequestDTO)).thenReturn(usuarioRegisterDTO);

        ResponseEntity<?> response = usuarioController.login(loginRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioRegisterDTO, response.getBody());

        verify(usuarioService).login(loginRequestDTO);
    }

    @Test
    void getUsuarios_shouldCallServiceAndReturnUserList() {
        UsuarioResponseDTO userResponse = new UsuarioResponseDTO(
                UUID.randomUUID(), "Test User", "aaaaaaaa@example.cl",
                Instant.now(), Instant.now(), Instant.now(), "token", true
        );
        List<UsuarioResponseDTO> userList = List.of(userResponse);
        when(usuarioService.findAll()).thenReturn(userList);

        ResponseEntity<?> response = usuarioController.getUsuarios();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody());

        verify(usuarioService).findAll();
    }

    @Test
    void getUsuarios_whenNoUsers_shouldReturnEmptyList() {
        List<UsuarioResponseDTO> emptyList = Collections.emptyList();
        when(usuarioService.findAll()).thenReturn(emptyList);

        ResponseEntity<?> response = usuarioController.getUsuarios();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyList, response.getBody());

        verify(usuarioService).findAll();
    }
}
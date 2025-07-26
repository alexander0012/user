package com.test.user.service.impl;

import com.test.user.repository.UsuarioRepository;
import com.test.user.repository.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private Usuario testUsuario;
    private final String userEmail = "aaaaaaaa@example.cl";

    @BeforeEach
    void setUp() {
        // Initialize a sample user for our tests
        testUsuario = Usuario.builder()
                .email(userEmail)
                .password("securePassword123")
                .name("Test User")
                .build();
    }

    @Test
    void loadUserByUsername_whenUserExists_shouldReturnUserDetails() {
        when(usuarioRepository.findByEmail(userEmail)).thenReturn(Optional.of(testUsuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        assertNotNull(userDetails);
        assertEquals(userEmail, userDetails.getUsername());
        assertEquals(testUsuario, userDetails);

        verify(usuarioRepository, times(1)).findByEmail(userEmail);
    }

    @Test
    void loadUserByUsername_whenUserDoesNotExist_shouldThrowException() {
        String nonExistentEmail = "aaaaaaaa@example.cl";
        when(usuarioRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(nonExistentEmail)
        );

        String expectedMessage = "Usuario no encontrado con el email: " + nonExistentEmail;
        assertEquals(expectedMessage, exception.getMessage());

        verify(usuarioRepository, times(1)).findByEmail(nonExistentEmail);
    }
}
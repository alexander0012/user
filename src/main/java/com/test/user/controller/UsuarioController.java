package com.test.user.controller;

import com.test.user.domain.dto.request.LoginRequestDTO;
import com.test.user.domain.dto.request.UsuarioRequestDTO;
import com.test.user.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.ok(usuarioService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(usuarioService.login(request));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getUsuarios() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/allDetails")
    public ResponseEntity<?> getUsuariosDetails() {
        return ResponseEntity.ok(usuarioService.findAllDetails());
    }

    @GetMapping("/search")
    public ResponseEntity<?> getUsuariosDetails(@RequestParam(name = "edad") int edad) {
        return ResponseEntity.ok(usuarioService.searchUsers(edad));
    }
}

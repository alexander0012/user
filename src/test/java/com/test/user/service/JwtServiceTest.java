package com.test.user.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private final String testUsername = "testuser";
    private final String secretKey = "dGhpcy1pcy1hLXNlY3JldC1rZXktZm9yLXRlc3RpbmctcHVycG9zZXMtYW5kLWl0LWlzLWxvbmc=";
    private final long jwtExpiration = TimeUnit.HOURS.toMillis(1);

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", jwtExpiration);
        lenient().when(userDetails.getUsername()).thenReturn(testUsername);
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_shouldReturnCorrectUsername() {
        String token = jwtService.generateToken(userDetails);
        String extractedUsername = jwtService.extractUsername(token);

        assertEquals(testUsername, extractedUsername);
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(userDetails);
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_shouldReturnFalseForUsernameMismatch() {
        String token = jwtService.generateToken(userDetails);
        when(userDetails.getUsername()).thenReturn("anotheruser");

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertFalse(isValid);
    }

    @Test
    void isTokenValid_shouldReturnFalseForExpiredToken() throws InterruptedException {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1L);
        String token = jwtService.generateToken(userDetails);

        Thread.sleep(5);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertFalse(isValid);
    }

    @Test
    void isTokenValid_shouldReturnFalseForMalformedToken() {
        String malformedToken = "this.is.not.a.valid.jwt";
        boolean isValid = jwtService.isTokenValid(malformedToken, userDetails);
        assertFalse(isValid);
    }

    @Test
    void isTokenValid_shouldReturnFalseForInvalidSignature() {
        JwtService otherJwtService = new JwtService();
        ReflectionTestUtils.setField(otherJwtService, "secretKey", "ZGlmZmVyZW50LXNlY3JldC1rZXktZm9yLXRlc3RpbmctcHVycG9zZXMtYW5kLWl0LWlzLWxvbmc=");
        ReflectionTestUtils.setField(otherJwtService, "jwtExpiration", jwtExpiration);
        String tokenWithWrongSignature = otherJwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(tokenWithWrongSignature, userDetails);
        assertFalse(isValid);
    }

    @Test
    void extractClaim_shouldReturnCorrectClaim() {
        String token = jwtService.generateToken(userDetails);

        String subject = jwtService.extractClaim(token, Claims::getSubject);
        assertEquals(testUsername, subject);

        Date expirationDate = jwtService.extractClaim(token, Claims::getExpiration);
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }
}
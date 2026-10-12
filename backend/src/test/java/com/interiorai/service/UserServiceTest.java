package com.interiorai.service;

import com.interiorai.config.JwtTokenProvider;
import com.interiorai.dto.AuthResponse;
import com.interiorai.dto.RegistrationRequest;
import com.interiorai.entity.User;
import com.interiorai.exception.BadRequestException;
import com.interiorai.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    private JwtTokenProvider tokenProvider;
    private UserService userService;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();
        // inject default test secret
        org.springframework.test.util.ReflectionTestUtils.setField(tokenProvider, "jwtSecret", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        org.springframework.test.util.ReflectionTestUtils.setField(tokenProvider, "jwtExpirationMs", 3600000L);
        userService = new UserService(userRepository, passwordEncoder, tokenProvider, authenticationManager);
    }

    @Test
    void testRegister_Success() {
        RegistrationRequest request = new RegistrationRequest();
        request.setFullName("Aarav Sharma");
        request.setEmail("aarav@example.com");
        request.setPassword("securePass123");
        request.setConfirmPassword("securePass123");
        request.setMobile("+91 9876543210");

        when(userRepository.existsByEmail("aarav@example.com")).thenReturn(false);
        when(passwordEncoder.encode("securePass123")).thenReturn("encodedPassword");

        User savedUser = new User("Aarav Sharma", "aarav@example.com", "encodedPassword", "+91 9876543210");
        savedUser.setUserId(1L);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        AuthResponse response = userService.register(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertFalse(response.getToken().isBlank());
        assertEquals("Aarav Sharma", response.getUser().getFullName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_ThrowsWhenPasswordsMismatch() {
        RegistrationRequest request = new RegistrationRequest();
        request.setFullName("Aarav Sharma");
        request.setEmail("aarav@example.com");
        request.setPassword("passwordA");
        request.setConfirmPassword("passwordB");

        BadRequestException ex = assertThrows(BadRequestException.class, () -> userService.register(request));
        assertTrue(ex.getMessage().contains("match"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_ThrowsWhenEmailAlreadyExists() {
        RegistrationRequest request = new RegistrationRequest();
        request.setFullName("Aarav Sharma");
        request.setEmail("existing@example.com");
        request.setPassword("password123");
        request.setConfirmPassword("password123");

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> userService.register(request));
        assertTrue(ex.getMessage().contains("already exists"));
        verify(userRepository, never()).save(any(User.class));
    }
}

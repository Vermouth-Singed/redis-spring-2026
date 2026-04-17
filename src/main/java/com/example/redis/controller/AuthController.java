package com.example.redis.controller;

import com.example.redis.constants.RedisConstants;
import com.example.redis.dto.UserRequestDto;
import com.example.redis.dto.UserResponseDto;
import com.example.redis.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody UserRequestDto request, HttpServletResponse response) {
        if (
            request.getUserId() == null || request.getPassword() == null ||
            request.getUserId().trim().isEmpty() || request.getPassword().trim().isEmpty())
        {
            return ResponseEntity.ok(null);
        }

        String sessionId = UUID.randomUUID().toString();

        UserResponseDto userResponseDto = authService.login(request.getUserId(), request.getPassword(), sessionId);

        if (userResponseDto == null) {
            return ResponseEntity.ok(null);
        }

        Cookie cookie = new Cookie(RedisConstants.SESSION_ID, sessionId);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60);
        cookie.setPath("/");

        response.addCookie(cookie);

        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(RedisConstants.SESSION_ID) String sessionId, HttpServletResponse response) {
        authService.logout(sessionId);

        Cookie cookie = new Cookie(RedisConstants.SESSION_ID, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);

        return ResponseEntity.ok("로그아웃 성공");
    }
}
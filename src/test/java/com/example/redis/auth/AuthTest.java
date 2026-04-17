package com.example.redis.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthTest {
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("로그인 성공")
    void 비밀번호_암호화() {
        //$2a$10$lQ2d6FMcusuhJvdSwDNu6e.2Ixnq.rosGZliTLBpjWeCKOd2zHe9u
        System.out.println(encoder.encode("user01"));
    }
}

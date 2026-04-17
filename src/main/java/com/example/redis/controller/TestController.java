package com.example.redis.controller;

import com.example.redis.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class TestController {
    @GetMapping
    public ResponseEntity<UserResponseDto> test() {
        return ResponseEntity.ok(new UserResponseDto());
    }
}

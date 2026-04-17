package com.example.redis.service;

import com.example.redis.dto.UserResponseDto;
import com.example.redis.entity.UserEntity;
import com.example.redis.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String SESSION_PREFIX = "session:";
    private static final Duration SESSION_TTL = Duration.ofMinutes(1);
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private static final ObjectMapper mapper = new ObjectMapper();

    public UserResponseDto login(String userId, String password, String sessionId) {
        UserResponseDto userResponseDto = new UserResponseDto();

        UserEntity userEntity = findByUserId(userId);

        if (userEntity == null || !encoder.matches(password, userEntity.getPassword())) {
            return null;
        }

        UserVo userVo = new UserVo(userEntity.getUserId());

        redisTemplate.opsForValue().set(
            SESSION_PREFIX + sessionId,
            userVo,
            SESSION_TTL
        );

        userResponseDto.setUserId(userEntity.getUserId());
        userResponseDto.setName(userEntity.getName());

        return userResponseDto;
    }

    private UserEntity findByUserId(String userId) {
        if (userId == null || userId.trim().isEmpty() || !"user01".equals(userId)) {
            return null;
        }

        return new UserEntity("user01", encoder.encode("user01"), "유저01");
    }

    public UserVo getSession(String sessionId) {
        Object userVo = redisTemplate.opsForValue().get(SESSION_PREFIX + sessionId);

        if (userVo == null) {
            return null;
        }

        return mapper.convertValue(userVo, UserVo.class);
    }

    public void extendSession(String sessionId) {
        redisTemplate.expire(SESSION_PREFIX + sessionId, SESSION_TTL);
    }

    public void logout(String sessionId) {
        redisTemplate.delete(SESSION_PREFIX + sessionId);
    }
}

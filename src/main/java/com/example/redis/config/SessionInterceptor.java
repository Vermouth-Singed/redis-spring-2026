package com.example.redis.config;

import com.example.redis.constants.RedisConstants;
import com.example.redis.service.AuthService;
import com.example.redis.vo.UserVo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 필요");

            return false;
        }

        String sessionId = Arrays.stream(cookies)
            .filter(c -> RedisConstants.SESSION_ID.equals(c.getName()))
            .map(Cookie::getValue)
            .findFirst()
            .orElse(null);

        if (sessionId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 필요");

            return false;
        }

        UserVo loginUser = authService.getSession(sessionId);

        if (loginUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 필요");

            return false;
        }

        authService.extendSession(sessionId);

        request.setAttribute("loginUser", loginUser);

        return true;
    }
}

package com.listywave.auth.presentation;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.listywave.auth.application.dto.LoginResponse;
import com.listywave.auth.application.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/kakao")
    ResponseEntity<Void> redirectAuthCodeRequestUrl(HttpServletResponse response) throws IOException {
        String requestUrl = authService.provideRedirectUri();
        response.sendRedirect(requestUrl);
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

    @GetMapping("/auth/redirect/kakao")
    ResponseEntity<LoginResponse> login(@RequestParam(name = "code") String authCode) {
        LoginResponse loginResponse = authService.login(authCode);
        return ResponseEntity.ok(loginResponse);
    }

    @PatchMapping("/auth/kakao")
    ResponseEntity<Void> logout(@RequestHeader(value = AUTHORIZATION) String accessToken) {
        authService.logout(accessToken);
        return ResponseEntity.noContent().build();
    }
}
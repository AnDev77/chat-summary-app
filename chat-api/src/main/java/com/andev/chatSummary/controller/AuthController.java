package com.andev.chatSummary.controller;

import com.andev.chatSummary.common.ApiResponse;
import com.andev.chatSummary.dto.auth.EmailRequest;
import com.andev.chatSummary.dto.auth.LoginRequest;
import com.andev.chatSummary.dto.auth.SignUpRequest;
import com.andev.chatSummary.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor// 경로 지정은 여기서!
public class AuthController {
    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Map<String, String>>> signup(@RequestBody SignUpRequest request){
        return ResponseEntity.ok(
                ApiResponse.ok("회원가입 테스트 성공!", null)
        );
    }

    @PostMapping("/email/send")
    public ResponseEntity<ApiResponse<String>> emailSend(@RequestBody EmailRequest request){
        return ResponseEntity.ok(
                ApiResponse.ok("이메일 컨트롤러 오류 없음", null)
        );
    }

    @PostMapping("/email/verify")
    public ResponseEntity<ApiResponse<String>> emailVerify(@RequestBody EmailRequest request){
        return ResponseEntity.ok(
                ApiResponse.ok("이메일 컨트롤러 오류 없음", null)
        );
    }



    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody LoginRequest loginRequest){
        System.out.println("로그인 시도 아이디: " + loginRequest.getEmail());

        // 2. 가상의 응답 데이터 (나중에 여기에 JWT 토큰이 들어갈 거예요)
        Map<String, String> mockData = new HashMap<>();
        mockData.put("accessToken", "fake-jwt-token-for-test");

        // 3. 우리가 만든 공통 규격으로 반환
        return ResponseEntity.ok(
                ApiResponse.ok("로그인 테스트 성공!", mockData)
        );
    }
}

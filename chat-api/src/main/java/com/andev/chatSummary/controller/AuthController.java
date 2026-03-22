package com.andev.chatSummary.controller;

import com.andev.chatSummary.common.ApiResponse;
import com.andev.chatSummary.dto.auth.EmailRequest;
import com.andev.chatSummary.dto.auth.LoginRequest;
import com.andev.chatSummary.dto.auth.LoginResponse;
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
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody SignUpRequest request){
        authService.signup(request);
        return ResponseEntity.ok(
                ApiResponse.ok("회원가입 테스트 성공!")
        );
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request){
        LoginResponse user = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.ok("로그인이 완료됐습니다.", user)
        );
    }

    @PostMapping("/email-duplication")
    public ResponseEntity<ApiResponse<String>> checkDuplication(@RequestBody EmailRequest request){
        authService.checkEmailDuplication(request);
        return ResponseEntity.ok(
                ApiResponse.ok("중복확인이 완료 됐습니다.")
        );
    }
    @PostMapping("/email-send")
    public ResponseEntity<ApiResponse<String>> emailSend(@RequestBody EmailRequest request){
        authService.sendVerificationCode(request);
        return ResponseEntity.ok(
                ApiResponse.ok("인증 번호가 발송되었습니다. 메일함을 확인해주세요.")
        );
    }

    @PostMapping("/email-verify")
    public ResponseEntity<ApiResponse<Boolean>> emailVerify(@RequestBody EmailRequest request){
        authService.verifyCode(request);

        return ResponseEntity.ok(
                    ApiResponse.ok("이메일 인증에 성공했습니다.")
            );


    }

}

package com.andev.chatSummary.service;


import com.andev.chatSummary.dto.auth.EmailRequest;
import com.andev.chatSummary.dto.auth.LoginRequest;
import com.andev.chatSummary.dto.auth.LoginResponse;
import com.andev.chatSummary.dto.auth.SignUpRequest;
import com.andev.chatSummary.execption.BusinessException;
import com.andev.chatSummary.implement.EmailRepository;
import com.andev.chatSummary.implement.UserRepository;
import com.andev.chatSummary.util.JwtProvider;
import com.project.chatsummary.jooq.tables.Users;
import com.project.chatsummary.jooq.tables.records.UsersRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor

public class AuthService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwt;
    @Transactional
    public void signup(SignUpRequest request){
        // 2. 인증 여부 체크
        if (!emailRepository.isEmailVerified(request.getEmail())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,"EMAIL_NOT_VERIFIED","이메일 인증이 완료되지 않았습니다.");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);
        userRepository.save(request);

    }
    @Transactional
    public void checkEmailDuplication(EmailRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new BusinessException(HttpStatus.BAD_REQUEST, "DUPLICATE", "이미 갑입 된 이메일 입니다.");
        }
    }
    public void sendVerificationCode(EmailRequest request) {
        String email = request.getEmail();
        String code = String.format("%06d", new Random().nextInt(1000000));

        System.out.println("2. Redis 저장 시도...");
        emailRepository.saveVerificationCode(email, code);

        sendEmail(email, code);

    }

    /**
     * 2. 인증 번호 확인
     */
    @Transactional
    public void verifyCode(EmailRequest request) {
        String savedCode = emailRepository.getVerificationCode(request.getEmail());

        // 1. 만료 체크
        if (savedCode == null) {
            System.out.println("Redis에서 번호를 못 찾음! 입력된 이메일: " + request.getEmail());
            throw new BusinessException(HttpStatus.BAD_REQUEST, "EXPIRED_CODE", "인증 시간이 만료되었습니다. 다시 시도해 주세요.");
        }
        // 2. 일치 체크
        if (!savedCode.equals(request.getCode())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "INVALID_CODE", "인증 번호가 일치하지 않습니다.");
        }

        // 3. 성공 시 상태 저장
        emailRepository.setVerifiedStatus(request.getEmail());
    }

    /**
     * 실제 이메일 전송 로직 (Private)
     */
    private void sendEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[Chat Summary] 이메일 인증 번호입니다.");
        message.setText("인증 번호는 [" + code + "] 입니다.\n5분 이내에 입력해 주세요.");

        mailSender.send(message);
    }


    public LoginResponse login(LoginRequest request) {
        UsersRecord record = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "LOGIN_FAIL", "아이디 또는 비밀번호가 일치하지 않습니다."));

        //비밀번호 일치 확인
        if (!passwordEncoder.matches(request.getPassword(), record.getPasswordHash())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "LOGIN_FAIL", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        String token = jwt.createToken(request.getEmail());
        return LoginResponse.builder()
                .email(record.getEmail())
                .nickname(record.getNickname())
                .token(token)
                .build();
    }

}

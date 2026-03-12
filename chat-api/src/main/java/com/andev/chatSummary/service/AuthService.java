package com.andev.chatSummary.service;


import com.andev.chatSummary.dto.auth.EmailRequest;
import com.andev.chatSummary.dto.auth.VerificationRequest;
import com.andev.chatSummary.implement.EmailRepository;
import com.andev.chatSummary.implement.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor

public class AuthService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Transactional
    public void sendVerificationCode(EmailRequest request) {
        String email = request.getEmail();

        // 6자리 난수 생성 (000000 ~ 999999)
        String code = String.format("%06d", new Random().nextInt(1000000));
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5); // 5분 유효 시간

        // DB에 저장 (이미 있으면 갱신 - Jooq 구현체에서 처리함)
        emailRepository.saveOrUpdate(email, code, expiredAt);

        // 메일 발송
        sendEmail(email, code);
    }

    /**
     * 2. 인증 번호 확인
     */
    @Transactional
    public boolean verifyCode(VerificationRequest request) {
        String email = request.getEmail();
        String code = request.getVerification_code();

        return emailRepository.findByEmail(email)
                .filter(record -> record.getVerificationCode().equals(code)) // 번호 일치 여부
                .filter(record -> record.getExpiredAt().isAfter(LocalDateTime.now())) // 만료 여부
                .map(record -> {
                    // 확인 성공 시 상태를 VERIFIED로 변경
                    emailRepository.updateStatusToVerified(email);
                    return true;
                })
                .orElse(false);
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



}

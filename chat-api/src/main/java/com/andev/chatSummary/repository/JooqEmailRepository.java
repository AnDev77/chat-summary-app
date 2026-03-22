package com.andev.chatSummary.repository;

import com.andev.chatSummary.implement.EmailRepository;
import com.project.chatsummary.jooq.tables.records.EmailVerificationsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import static com.project.chatsummary.jooq.Tables.EMAIL_VERIFICATIONS;
import static com.project.chatsummary.jooq.enums.EmailVerificationsStatus.PENDING;
import static com.project.chatsummary.jooq.enums.EmailVerificationsStatus.VERIFIED;

@Repository
@RequiredArgsConstructor
public class JooqEmailRepository implements EmailRepository {
    private final StringRedisTemplate redisTemplate;

    // 접두사(Prefix)를 붙여서 관리하면 나중에 redis-cli에서 찾기 편합니다.
    private static final String PREFIX_CODE = "email:code:";
    private static final String PREFIX_VERIFIED = "email:verified:";

    // 1. 인증번호 저장 (5분 제한)
    public void saveVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(
                PREFIX_CODE + email,
                code,
                Duration.ofMinutes(5) // 5분 뒤 자동 삭제
        );
    }

    // 2. 인증번호 조회
    public String getVerificationCode(String email) {
        return redisTemplate.opsForValue().get(PREFIX_CODE + email);
    }

    // 3. 인증 완료 마킹 (성공 시 'true' 저장, 10분 내 가입 완료 필요)
    public void setVerifiedStatus(String email) {
        redisTemplate.opsForValue().set(
                PREFIX_VERIFIED + email,
                "true",
                Duration.ofMinutes(10)
        );
        // 인증 성공했으니 기존 번호는 지워도 됩니다.
        redisTemplate.delete(PREFIX_CODE + email);
    }

    // 4. 최종 인증 여부 확인 (회원가입 버튼 눌렀을 때 체크)
    public boolean isEmailVerified(String email) {
        String status = redisTemplate.opsForValue().get(PREFIX_VERIFIED + email);
        return "true".equals(status);
    }
}

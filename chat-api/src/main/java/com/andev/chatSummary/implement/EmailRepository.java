package com.andev.chatSummary.implement;

import com.andev.chatSummary.jooq.enums.EmailVerificationsStatus;
import com.andev.chatSummary.jooq.tables.records.EmailVerificationsRecord;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailRepository {
    void saveOrUpdate(String email, String code, LocalDateTime expiredAt);

    // 2. 인증 번호와 만료 시간 확인
    Optional<EmailVerificationsRecord> findByEmail(String email);

    // 3. 인증 완료 시 상태 업데이트 (PENDING -> VERIFIED)
    void updateStatus(String email, EmailVerificationsStatus status);

    // 4. 회원가입 완료 후 데이터 삭제
    void deleteByEmail(String email);
}

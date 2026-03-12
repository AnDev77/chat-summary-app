package com.andev.chatSummary.implement;


import com.project.chatsummary.jooq.tables.records.EmailVerificationsRecord;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailRepository {
    void saveOrUpdate(String email, String code, LocalDateTime expiredAt);

    // 이메일로 인증 정보 찾기
    Optional<EmailVerificationsRecord> findByEmail(String email);

    // 인증 상태를 VERIFIED로 변경
    void updateStatusToVerified(String email);

    // 인증 여부 확인 (회원가입 직전 체크용)
    boolean isVerified(String email);

    // 데이터 삭제 (회원가입 완료 후 정리)
    void deleteByEmail(String email);
}

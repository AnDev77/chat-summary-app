package com.andev.chatSummary.implement;


import com.project.chatsummary.jooq.tables.records.EmailVerificationsRecord;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailRepository {
    public void saveVerificationCode(String email, String code);
    // 이메일로 인증 정보 찾기
    public String getVerificationCode(String email);

    // 인증 상태를 VERIFIED로 변경
    public void setVerifiedStatus(String email);

    // 인증 여부 확인 (회원가입 직전 체크용)
    public boolean isEmailVerified(String email);
}

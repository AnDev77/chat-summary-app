package com.andev.chatSummary.repository;

import com.andev.chatSummary.implement.EmailRepository;
import com.project.chatsummary.jooq.tables.records.EmailVerificationsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import static com.project.chatsummary.jooq.Tables.EMAIL_VERIFICATIONS;
import static com.project.chatsummary.jooq.enums.EmailVerificationsStatus.PENDING;
import static com.project.chatsummary.jooq.enums.EmailVerificationsStatus.VERIFIED;

@Repository
@RequiredArgsConstructor
public class JooqEmailRepository implements EmailRepository {
    private final DSLContext dsl;

    /**
     * 1. 인증번호 저장 또는 갱신
     * 이미 이메일이 존재하면 번호와 만료시간을 업데이트하고 상태를 PENDING으로 리셋합니다.
     */
    @Override
    public void saveOrUpdate(String email, String code, LocalDateTime expiredAt) {
        dsl.insertInto(EMAIL_VERIFICATIONS)
                .set(EMAIL_VERIFICATIONS.EMAIL, email)
                .set(EMAIL_VERIFICATIONS.VERIFICATION_CODE, code)
                .set(EMAIL_VERIFICATIONS.STATUS, PENDING)
                .set(EMAIL_VERIFICATIONS.EXPIRED_AT, expiredAt)
                .onDuplicateKeyUpdate()
                .set(EMAIL_VERIFICATIONS.VERIFICATION_CODE, code)
                .set(EMAIL_VERIFICATIONS.STATUS, PENDING)
                .set(EMAIL_VERIFICATIONS.EXPIRED_AT, expiredAt)
                .execute();
    }

    /**
     * 2. 인증 정보 조회
     */
    @Override
    public Optional<EmailVerificationsRecord> findByEmail(String email) {
        return dsl.selectFrom(EMAIL_VERIFICATIONS)
                .where(EMAIL_VERIFICATIONS.EMAIL.eq(email))
                .fetchOptional();
    }
    /**
     * 3. 인증 성공 시 상태 변경 (PENDING -> VERIFIED)
     */
    @Override
    public void updateStatusToVerified(String email) {
        dsl.update(EMAIL_VERIFICATIONS)
                .set(EMAIL_VERIFICATIONS.STATUS, VERIFIED)
                .where(EMAIL_VERIFICATIONS.EMAIL.eq(email))
                .execute();
    }

    /**
     * 4. 최종 회원가입 완료 후 데이터 삭제 (정리)
     */
    @Override
    public void deleteByEmail(String email) {
        dsl.deleteFrom(EMAIL_VERIFICATIONS)
                .where(EMAIL_VERIFICATIONS.EMAIL.eq(email))
                .execute();
    }

    @Override
    public boolean isVerified(String email) {
        return dsl.fetchExists(
                dsl.selectFrom(EMAIL_VERIFICATIONS)
                        .where(EMAIL_VERIFICATIONS.EMAIL.eq(email))
                        .and(EMAIL_VERIFICATIONS.STATUS.eq(VERIFIED))
        );
    }
}

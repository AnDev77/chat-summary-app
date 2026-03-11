package com.andev.chatSummary.repository;

import com.andev.chatSummary.implement.EmailRepository;
import com.andev.chatSummary.jooq.enums.EmailVerificationsStatus;
import com.andev.chatSummary.jooq.tables.records.EmailVerificationsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.andev.chatSummary.jooq.Tables.EMAIL_VERIFICATIONS;

@Repository
@RequiredArgsConstructor
public class JooqEmailRepository implements EmailRepository {
    private final DSLContext dsl;

    @Override
    public void saveOrUpdate(String email, String code, LocalDateTime expiredAt) {
        // 있으면 업데이트, 없으면 삽입 (MySQL의 ON DUPLICATE KEY UPDATE 활용)
        dsl.insertInto(EMAIL_VERIFICATIONS)
                .set(EMAIL_VERIFICATIONS.EMAIL, email)
                .set(EMAIL_VERIFICATIONS.VERIFICATION_CODE, code)
                .set(EMAIL_VERIFICATIONS.STATUS, EmailVerificationsStatus.PENDING)
                .set(EMAIL_VERIFICATIONS.EXPIRED_AT, expiredAt)
                .onDuplicateKeyUpdate()
                .set(EMAIL_VERIFICATIONS.VERIFICATION_CODE, code)
                .set(EMAIL_VERIFICATIONS.STATUS, EmailVerificationsStatus.PENDING)
                .set(EMAIL_VERIFICATIONS.EXPIRED_AT, expiredAt)
                .execute();
    }

    @Override
    public Optional<EmailVerificationsRecord> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void updateStatus(String email, EmailVerificationsStatus status) {

    }

    @Override
    public void deleteByEmail(String email) {

    }

    public boolean isVerified(String email) {
        return dsl.fetchExists(
                dsl.selectFrom(EMAIL_VERIFICATIONS)
                        .where(EMAIL_VERIFICATIONS.EMAIL.eq(email))
                        .and(EMAIL_VERIFICATIONS.STATUS.eq(EmailVerificationsStatus.VERIFIED))
        );
    }
}

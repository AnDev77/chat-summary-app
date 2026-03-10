package com.andev.chatSummary.repository;

import com.andev.chatSummary.dto.SignUpRequest;
import com.andev.chatSummary.implement.UserRepository;
import com.andev.chatSummary.jooq.Tables;
import com.andev.chatSummary.jooq.enums.EmailVerificationsStatus;
import com.andev.chatSummary.jooq.tables.EmailVerifications;
import com.andev.chatSummary.jooq.tables.Users;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JooqUserRepository implements UserRepository {
    private final DSLContext dsl;

    @Override
    public void save(Users user) {

    }

    @Override
    public boolean findByEmail(String email) {
        return false;
    }

    @Override
    public boolean isVerified(String email) {
        return dsl.fetchExists(
                dsl.selectFrom(Tables.EMAIL_VERIFICATIONS)
                        .where(Tables.EMAIL_VERIFICATIONS.EMAIL.eq(email))
                        .and(Tables.EMAIL_VERIFICATIONS.STATUS.eq(EmailVerificationsStatus.VERIFIED))
        );
    }
    @Override
    public void deleteVerification(String email) {

    }
}
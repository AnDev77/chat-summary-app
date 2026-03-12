package com.andev.chatSummary.repository;

import com.andev.chatSummary.implement.UserRepository;
import com.project.chatsummary.jooq.enums.EmailVerificationsStatus;
import com.project.chatsummary.jooq.tables.Users;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.project.chatsummary.jooq.Tables.EMAIL_VERIFICATIONS;

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
                dsl.selectFrom(EMAIL_VERIFICATIONS)
                        .where(EMAIL_VERIFICATIONS.EMAIL.eq(email))
                        .and(EMAIL_VERIFICATIONS.STATUS.eq(EmailVerificationsStatus.VERIFIED))
        );
    }
    @Override
    public void deleteVerification(String email) {

    }
}
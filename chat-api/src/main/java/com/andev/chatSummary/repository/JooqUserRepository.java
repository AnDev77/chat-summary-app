package com.andev.chatSummary.repository;

import com.andev.chatSummary.dto.auth.EmailRequest;
import com.andev.chatSummary.dto.auth.LoginRequest;
import com.andev.chatSummary.dto.auth.LoginResponse;
import com.andev.chatSummary.dto.auth.SignUpRequest;
import com.andev.chatSummary.implement.EmailRepository;
import com.andev.chatSummary.implement.UserRepository;
import com.project.chatsummary.jooq.enums.EmailVerificationsStatus;
import com.project.chatsummary.jooq.tables.EmailVerifications;
import com.project.chatsummary.jooq.tables.Users;
import com.project.chatsummary.jooq.tables.records.UsersRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.project.chatsummary.jooq.Tables.EMAIL_VERIFICATIONS;
import static com.project.chatsummary.jooq.Tables.USERS;

@Repository
@RequiredArgsConstructor
public class JooqUserRepository implements UserRepository {
    private final DSLContext dsl;

    @Override
    public void save(SignUpRequest request) {
        dsl.insertInto(USERS).
                set(USERS.EMAIL, request.getEmail())
                .set(USERS.PASSWORD_HASH, request.getPassword())
                .set(USERS.NICKNAME, request.getNickname())
                .execute();
    }

    @Override
    public Optional<UsersRecord> findByEmail(String email) {
        return dsl.selectFrom(USERS)
                .where(USERS.EMAIL.eq(email))
                .fetchOptional();
    }

}
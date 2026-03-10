package com.andev.chatSummary.implement;

import com.andev.chatSummary.jooq.tables.Users;
import com.andev.chatSummary.jooq.tables.records.UsersRecord;

import java.util.Optional;

public interface UserRepository {
    void save(Users user);
    boolean findByEmail(String email);
    boolean isVerified(String email); // 우리가 추가한 status 체크용
    void deleteVerification(String email);
}
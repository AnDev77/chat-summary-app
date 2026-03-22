package com.andev.chatSummary.implement;;
import com.andev.chatSummary.dto.auth.LoginRequest;
import com.andev.chatSummary.dto.auth.LoginResponse;
import com.andev.chatSummary.dto.auth.SignUpRequest;
import com.project.chatsummary.jooq.tables.Users;
import com.project.chatsummary.jooq.tables.records.UsersRecord;

import java.util.Optional;

public interface UserRepository {
    void save(SignUpRequest request);
    Optional<UsersRecord> findByEmail(String email);
}
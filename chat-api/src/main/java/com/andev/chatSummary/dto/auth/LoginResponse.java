package com.andev.chatSummary.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    String nickname;
    String email;
    String token;
}

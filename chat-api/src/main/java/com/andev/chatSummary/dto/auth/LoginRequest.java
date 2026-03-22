package com.andev.chatSummary.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    String password;
    String email;
}

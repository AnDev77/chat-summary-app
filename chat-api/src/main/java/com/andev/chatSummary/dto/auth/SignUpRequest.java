package com.andev.chatSummary.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    String nickname;
    String password;
    String email;
}

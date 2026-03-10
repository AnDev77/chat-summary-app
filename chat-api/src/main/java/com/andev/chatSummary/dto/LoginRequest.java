package com.andev.chatSummary.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    String password;
    String email;
}

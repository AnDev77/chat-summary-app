package com.andev.chatSummary.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationRequest {
    String email;
    String verification_code;
}

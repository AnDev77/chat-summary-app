package com.andev.chatSummary.dto.auth;

import lombok.Getter;

@Getter
public class EmailRequest {
    String email;
    String code;
}

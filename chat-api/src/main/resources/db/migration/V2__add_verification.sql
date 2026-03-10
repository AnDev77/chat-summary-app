CREATE TABLE email_verifications (
    email VARCHAR(100) PRIMARY KEY,
    verification_code VARCHAR(10) NOT NULL, -- 숫자 6자리나 짧은 코드
    expired_at TIMESTAMP NOT NULL,          -- 유효 시간 (예: 5분)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

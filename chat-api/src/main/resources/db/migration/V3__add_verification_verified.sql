ALTER TABLE email_verifications
ADD COLUMN status ENUM('PENDING', 'VERIFIED') DEFAULT 'PENDING' AFTER verification_code;

ALTER TABLE email_verifications
ADD COLUMN attempt_count INT DEFAULT 0 AFTER status;
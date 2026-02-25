-- 앱 서비스 전용 (CRUD 권한만)
CREATE USER 'app_user'@'%' IDENTIFIED BY 'app_pass123';
GRANT SELECT, INSERT, UPDATE, DELETE ON chat_db.* TO 'app_user'@'%';

-- 요약 서비스 전용 (읽기 및 요약본 작성만)
CREATE USER 'summary_user'@'%' IDENTIFIED BY 'sum_pass123';
GRANT SELECT ON chat_db.messages TO 'summary_user'@'%';

FLUSH PRIVILEGES;
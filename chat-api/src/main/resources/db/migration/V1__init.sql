CREATE DATABASE IF NOT EXISTS chat_db;
USE chat_db;

-- 1. 유저
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. 채팅방
CREATE TABLE chat_rooms (
    room_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. 방 멤버 (메시지보다 먼저 생성 가능)
CREATE TABLE room_members (
    room_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    user_role ENUM('OWNER', 'ADMIN', 'MEMBER') DEFAULT 'MEMBER',
    PRIMARY KEY (room_id, user_id),
    FOREIGN KEY (room_id) REFERENCES chat_rooms(room_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 4. 메시지 (last_read_messages가 참조해야 하므로 위로 이동)
CREATE TABLE messages (
    message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    is_summarized BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES chat_rooms(room_id),
    FOREIGN KEY (sender_id) REFERENCES users(user_id)
);

-- 5. 읽음 확인 (messages 테이블이 존재해야 생성 가능)
CREATE TABLE last_read_messages (
    room_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    last_read_message_id BIGINT NOT NULL,
    PRIMARY KEY (room_id, user_id),
    FOREIGN KEY (room_id) REFERENCES chat_rooms(room_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (last_read_message_id) REFERENCES messages(message_id)
);

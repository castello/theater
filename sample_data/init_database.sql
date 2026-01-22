-- =============================================
-- 극장 예약 시스템 데이터베이스 초기화 스크립트
-- =============================================

-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS theater
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE theater;

-- =============================================
-- 테이블 삭제 (의존성 역순)
-- =============================================
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS reserved_seats;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS showtimes;
DROP TABLE IF EXISTS seats;
DROP TABLE IF EXISTS screens;
DROP TABLE IF EXISTS theaters;
DROP TABLE IF EXISTS movies;
DROP TABLE IF EXISTS users;

-- =============================================
-- 테이블 생성
-- =============================================

-- 1. 회원
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. 영화
CREATE TABLE movies (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    duration_minutes INT NOT NULL,
    rating VARCHAR(10),
    poster_url VARCHAR(500),
    release_date DATE,
    status ENUM('showing', 'upcoming', 'ended') DEFAULT 'upcoming'
) ENGINE=InnoDB;

-- 3. 극장
CREATE TABLE theaters (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(255),
    address TEXT
) ENGINE=InnoDB;

-- 4. 상영관
CREATE TABLE screens (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    theater_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    total_seats INT NOT NULL,
    FOREIGN KEY (theater_id) REFERENCES theaters(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5. 좌석
CREATE TABLE seats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    screen_id BIGINT NOT NULL,
    row_label VARCHAR(5) NOT NULL,
    seat_number INT NOT NULL,
    seat_type ENUM('standard', 'premium', 'wheelchair') DEFAULT 'standard',
    FOREIGN KEY (screen_id) REFERENCES screens(id) ON DELETE CASCADE,
    UNIQUE KEY uk_seat (screen_id, row_label, seat_number)
) ENGINE=InnoDB;

-- 6. 상영 스케줄
CREATE TABLE showtimes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    movie_id BIGINT NOT NULL,
    screen_id BIGINT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
    FOREIGN KEY (screen_id) REFERENCES screens(id) ON DELETE CASCADE,
    INDEX idx_showtime (screen_id, start_time)
) ENGINE=InnoDB;

-- 7. 예약
CREATE TABLE reservations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    showtime_id BIGINT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    status ENUM('pending', 'confirmed', 'cancelled') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (showtime_id) REFERENCES showtimes(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 8. 예약 좌석
CREATE TABLE reserved_seats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    reservation_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE,
    FOREIGN KEY (seat_id) REFERENCES seats(id) ON DELETE CASCADE,
    UNIQUE KEY uk_reserved_seat (reservation_id, seat_id)
) ENGINE=InnoDB;

-- 9. 결제
CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    reservation_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method ENUM('card', 'kakao', 'naver') NOT NULL,
    status ENUM('pending', 'completed', 'refunded') DEFAULT 'pending',
    paid_at TIMESTAMP NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =============================================
-- 샘플 데이터 삽입
-- =============================================

-- 회원 데이터
INSERT INTO users (id, email, password_hash, name, phone, created_at) VALUES
(1, 'kim@example.com', '$2b$10$abcdefghijklmnop', '김민수', '010-1234-5678', '2024-01-15 10:30:00'),
(2, 'lee@example.com', '$2b$10$qrstuvwxyzabcdef', '이지영', '010-2345-6789', '2024-01-20 14:20:00'),
(3, 'park@example.com', '$2b$10$ghijklmnopqrstuv', '박준호', '010-3456-7890', '2024-02-01 09:15:00'),
(4, 'choi@example.com', '$2b$10$wxyzabcdefghijkl', '최서연', '010-4567-8901', '2024-02-10 16:45:00'),
(5, 'jung@example.com', '$2b$10$mnopqrstuvwxyzab', '정다은', '010-5678-9012', '2024-02-15 11:00:00');

-- 영화 데이터
INSERT INTO movies (id, title, description, duration_minutes, rating, poster_url, release_date, status) VALUES
(1, '어벤져스: 엔드게임', '마블 히어로들의 최후의 전투', 181, '12세', '/posters/avengers.jpg', '2024-01-10', 'showing'),
(2, '기생충', '봉준호 감독의 블랙 코미디 스릴러', 132, '15세', '/posters/parasite.jpg', '2024-01-15', 'showing'),
(3, '겨울왕국 3', '엘사와 안나의 새로운 모험', 110, '전체', '/posters/frozen3.jpg', '2024-02-01', 'showing'),
(4, '범죄도시 4', '마동석 주연 액션 영화', 109, '15세', '/posters/crime4.jpg', '2024-03-01', 'upcoming'),
(5, '듄: 파트 3', '사막 행성 아라키스의 서사시', 165, '12세', '/posters/dune3.jpg', '2024-03-15', 'upcoming');

-- 극장 데이터
INSERT INTO theaters (id, name, location, address) VALUES
(1, 'CGV 강남', '강남', '서울시 강남구 강남대로 438'),
(2, 'CGV 용산', '용산', '서울시 용산구 한강대로 23길 55'),
(3, '롯데시네마 월드타워', '잠실', '서울시 송파구 올림픽로 300'),
(4, '메가박스 코엑스', '삼성', '서울시 강남구 영동대로 513');

-- 상영관 데이터
INSERT INTO screens (id, theater_id, name, total_seats) VALUES
(1, 1, '1관', 120),
(2, 1, '2관', 100),
(3, 1, 'IMAX관', 200),
(4, 2, '1관', 150),
(5, 2, '2관', 120),
(6, 3, '1관', 180),
(7, 3, '프리미엄관', 80),
(8, 4, '1관', 140),
(9, 4, '2관', 140),
(10, 4, '돌비시네마', 160);

-- 좌석 데이터
INSERT INTO seats (id, screen_id, row_label, seat_number, seat_type) VALUES
-- 1관 좌석
(1, 1, 'A', 1, 'standard'),
(2, 1, 'A', 2, 'standard'),
(3, 1, 'A', 3, 'standard'),
(4, 1, 'A', 4, 'standard'),
(5, 1, 'A', 5, 'standard'),
(6, 1, 'B', 1, 'standard'),
(7, 1, 'B', 2, 'standard'),
(8, 1, 'B', 3, 'standard'),
(9, 1, 'B', 4, 'standard'),
(10, 1, 'B', 5, 'standard'),
(11, 1, 'C', 1, 'premium'),
(12, 1, 'C', 2, 'premium'),
(13, 1, 'C', 3, 'premium'),
(14, 1, 'C', 4, 'premium'),
(15, 1, 'C', 5, 'premium'),
(16, 1, 'D', 1, 'premium'),
(17, 1, 'D', 2, 'premium'),
(18, 1, 'D', 3, 'premium'),
(19, 1, 'D', 4, 'premium'),
(20, 1, 'D', 5, 'premium'),
(21, 1, 'E', 1, 'standard'),
(22, 1, 'E', 2, 'wheelchair'),
(23, 1, 'E', 3, 'standard'),
(24, 1, 'E', 4, 'wheelchair'),
(25, 1, 'E', 5, 'standard'),
-- 2관 좌석
(26, 2, 'A', 1, 'standard'),
(27, 2, 'A', 2, 'standard'),
(28, 2, 'A', 3, 'standard'),
(29, 2, 'A', 4, 'standard'),
(30, 2, 'B', 1, 'standard'),
(31, 2, 'B', 2, 'standard'),
(32, 2, 'B', 3, 'standard'),
(33, 2, 'B', 4, 'standard'),
(34, 2, 'C', 1, 'premium'),
(35, 2, 'C', 2, 'premium'),
(36, 2, 'C', 3, 'premium'),
(37, 2, 'C', 4, 'premium'),
-- IMAX관 좌석
(38, 3, 'A', 1, 'premium'),
(39, 3, 'A', 2, 'premium'),
(40, 3, 'A', 3, 'premium'),
(41, 3, 'A', 4, 'premium'),
(42, 3, 'A', 5, 'premium'),
(43, 3, 'B', 1, 'premium'),
(44, 3, 'B', 2, 'premium'),
(45, 3, 'B', 3, 'premium'),
(46, 3, 'B', 4, 'premium'),
(47, 3, 'B', 5, 'premium'),
(48, 3, 'C', 1, 'standard'),
(49, 3, 'C', 2, 'standard'),
(50, 3, 'C', 3, 'standard');

-- 상영 스케줄 데이터
INSERT INTO showtimes (id, movie_id, screen_id, start_time, end_time, price) VALUES
(1, 1, 1, '2024-02-20 10:00:00', '2024-02-20 13:01:00', 15000),
(2, 1, 1, '2024-02-20 14:00:00', '2024-02-20 17:01:00', 15000),
(3, 1, 3, '2024-02-20 19:00:00', '2024-02-20 22:01:00', 20000),
(4, 2, 2, '2024-02-20 11:00:00', '2024-02-20 13:12:00', 14000),
(5, 2, 2, '2024-02-20 15:00:00', '2024-02-20 17:12:00', 14000),
(6, 2, 4, '2024-02-20 18:00:00', '2024-02-20 20:12:00', 14000),
(7, 3, 1, '2024-02-20 09:00:00', '2024-02-20 10:50:00', 12000),
(8, 3, 5, '2024-02-20 13:00:00', '2024-02-20 14:50:00', 12000),
(9, 3, 6, '2024-02-20 16:00:00', '2024-02-20 17:50:00', 13000),
(10, 1, 7, '2024-02-20 20:00:00', '2024-02-20 23:01:00', 25000),
(11, 2, 8, '2024-02-21 10:00:00', '2024-02-21 12:12:00', 14000),
(12, 2, 8, '2024-02-21 14:00:00', '2024-02-21 16:12:00', 14000),
(13, 3, 9, '2024-02-21 11:00:00', '2024-02-21 12:50:00', 12000),
(14, 1, 10, '2024-02-21 19:00:00', '2024-02-21 22:01:00', 22000),
(15, 2, 10, '2024-02-21 14:00:00', '2024-02-21 16:12:00', 18000);

-- 예약 데이터
INSERT INTO reservations (id, user_id, showtime_id, total_price, status, created_at) VALUES
(1, 1, 1, 30000, 'confirmed', '2024-02-18 15:30:00'),
(2, 2, 3, 60000, 'confirmed', '2024-02-18 16:45:00'),
(3, 3, 4, 28000, 'confirmed', '2024-02-19 09:20:00'),
(4, 4, 7, 36000, 'confirmed', '2024-02-19 10:00:00'),
(5, 5, 10, 50000, 'confirmed', '2024-02-19 11:30:00'),
(6, 1, 5, 42000, 'pending', '2024-02-19 14:00:00'),
(7, 2, 9, 26000, 'confirmed', '2024-02-19 15:20:00'),
(8, 3, 14, 66000, 'confirmed', '2024-02-20 08:00:00'),
(9, 4, 15, 36000, 'cancelled', '2024-02-20 09:30:00'),
(10, 5, 11, 28000, 'confirmed', '2024-02-20 10:15:00');

-- 예약 좌석 데이터
INSERT INTO reserved_seats (id, reservation_id, seat_id) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 38),
(4, 2, 39),
(5, 2, 40),
(6, 3, 26),
(7, 3, 27),
(8, 4, 1),
(9, 4, 2),
(10, 4, 3),
(11, 5, 34),
(12, 5, 35),
(13, 6, 6),
(14, 6, 7),
(15, 6, 8),
(16, 7, 48),
(17, 7, 49),
(18, 8, 38),
(19, 8, 39),
(20, 8, 40),
(21, 9, 34),
(22, 9, 35),
(23, 10, 26),
(24, 10, 27);

-- 결제 데이터
INSERT INTO payments (id, reservation_id, amount, payment_method, status, paid_at) VALUES
(1, 1, 30000, 'card', 'completed', '2024-02-18 15:31:00'),
(2, 2, 60000, 'kakao', 'completed', '2024-02-18 16:46:00'),
(3, 3, 28000, 'naver', 'completed', '2024-02-19 09:21:00'),
(4, 4, 36000, 'card', 'completed', '2024-02-19 10:01:00'),
(5, 5, 50000, 'kakao', 'completed', '2024-02-19 11:31:00'),
(6, 6, 42000, 'card', 'pending', NULL),
(7, 7, 26000, 'naver', 'completed', '2024-02-19 15:21:00'),
(8, 8, 66000, 'card', 'completed', '2024-02-20 08:01:00'),
(9, 9, 36000, 'kakao', 'refunded', '2024-02-20 10:00:00'),
(10, 10, 28000, 'card', 'completed', '2024-02-20 10:16:00');

-- =============================================
-- 데이터 확인
-- =============================================
SELECT '테이블 생성 및 데이터 삽입 완료!' AS message;

SELECT 'users' AS table_name, COUNT(*) AS row_count FROM users
UNION ALL SELECT 'movies', COUNT(*) FROM movies
UNION ALL SELECT 'theaters', COUNT(*) FROM theaters
UNION ALL SELECT 'screens', COUNT(*) FROM screens
UNION ALL SELECT 'seats', COUNT(*) FROM seats
UNION ALL SELECT 'showtimes', COUNT(*) FROM showtimes
UNION ALL SELECT 'reservations', COUNT(*) FROM reservations
UNION ALL SELECT 'reserved_seats', COUNT(*) FROM reserved_seats
UNION ALL SELECT 'payments', COUNT(*) FROM payments;


-- Term 테이블 생성 ---
CREATE TABLE IF NOT EXISTS terms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date DATETIME(6) NOT NULL,
    modified_date DATETIME(6) NOT NULL,
    description TEXT NOT NULL,
    initial VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,

    -- ngram 기반 FULLTEXT 인덱스
    FULLTEXT INDEX idx_title_description (title, description) WITH PARSER ngram,

    -- 일반 B-Tree 인덱스
    INDEX idx_initial (initial)
) ENGINE=InnoDB;


-- TermScrap 테이블 생성 ---
CREATE TABLE IF NOT EXISTS term_scraps (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date DATETIME(6) NOT NULL,
    modified_date DATETIME(6) NOT NULL,
    term_id BIGINT NULL,
    user_id BIGINT NOT NULL
);

-- News 테이블 생성 ---
CREATE TABLE IF NOT EXISTS news (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date DATETIME(6) NOT NULL,
    modified_date DATETIME(6) NOT NULL,
    category ENUM(
        'ECONOMIC_ANALYSIS', 'ECONOMIC_POLICY', 'FINANCE', 'GLOBAL',
        'INDUSTRY', 'INVESTMENT', 'NORMAL', 'OTHER', 'REAL_ESTATE'
    ) NOT NULL,
    content VARCHAR(255) NULL,
    pub_date DATETIME(6) NULL,
    publisher VARCHAR(255) NULL,
    title VARCHAR(255) NOT NULL,
    url VARCHAR(255) NULL,
    views BIGINT NOT NULL,

    -- ngram 기반 FULLTEXT 인덱스
    FULLTEXT INDEX idx_title_content (title, content) WITH PARSER ngram
) ENGINE=InnoDB;

-- NewsScrap 테이블 생성 ---
CREATE TABLE IF NOT EXISTS news_scraps (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date DATETIME(6) NOT NULL,
    modified_date DATETIME(6) NOT NULL,
    news_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL
);

-- Notification 테이블 생성 ---
CREATE TABLE IF NOT EXISTS notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date DATETIME(6) NOT NULL,
    modified_date DATETIME(6) NOT NULL,
    content VARCHAR(255) NOT NULL,
    is_read BIT NOT NULL,
    post_id BIGINT NULL,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    type ENUM('COMMENT', 'POPULAR', 'REPLY') NOT NULL
);
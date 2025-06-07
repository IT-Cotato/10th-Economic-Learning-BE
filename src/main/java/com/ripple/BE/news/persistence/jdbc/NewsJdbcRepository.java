package com.ripple.BE.news.persistence.jdbc;

import com.ripple.BE.news.persistence.jpa.entity.NewsJpaEntity;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class NewsJdbcRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final int BATCH_SIZE = 1000;

    @Transactional
    public void saveAllNewsByJdbcTemplate(List<NewsJpaEntity> newsJpaEntityList) {

        String insertQuery =
                "INSERT INTO news (title, content, publisher, url, category, views, pub_date, created_date, modified_date) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

        jdbcTemplate.batchUpdate(
                insertQuery,
                newsJpaEntityList,
                BATCH_SIZE,
                (ps, news) -> {
                    ps.setString(1, news.getTitle());
                    ps.setString(2, news.getContent());
                    ps.setString(3, news.getPublisher());
                    ps.setString(4, news.getUrl());
                    ps.setString(5, news.getCategory().toString());
                    ps.setLong(6, 0L);

                    if (news.getPubDate() != null) {
                        ps.setTimestamp(7, Timestamp.valueOf(news.getPubDate().withNano(0)));
                    } else {
                        ps.setTimestamp(7, null);
                    }
                });
    }

    public List<String> findExistingUrls(List<String> urls) {
        if (urls.isEmpty()) {
            return List.of();
        }

        String query = "SELECT url FROM news WHERE url IN (:urls)";
        MapSqlParameterSource params = new MapSqlParameterSource("urls", urls);

        return namedParameterJdbcTemplate.query(query, params, (rs, rowNum) -> rs.getString("url"));
    }
}

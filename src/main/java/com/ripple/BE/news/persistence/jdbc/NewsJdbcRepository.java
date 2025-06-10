package com.ripple.BE.news.persistence.jdbc;

import static com.ripple.BE.news.persistence.jpa.entity.QNewsJpaEntity.*;
import static com.ripple.BE.news.persistence.jpa.entity.QNewsScrapJpaEntity.*;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.ripple.BE.news.domain.type.NewsCategory;
import com.ripple.BE.news.domain.type.NewsSort;
import com.ripple.BE.news.persistence.dto.NewsWithScrapDTO;
import com.ripple.BE.news.persistence.jpa.entity.NewsJpaEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
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

	@Transactional(readOnly = true)
	public Page<NewsWithScrapDTO> searchNews(String keyword, Pageable pageable, long userId) {
		if (keyword == null || keyword.trim().isEmpty()) {
			return Page.empty(pageable);
		}

		String searchSql = """
			    SELECT n.id, n.title, n.content, n.publisher, n.url, n.category,
			           n.views, n.pub_date,
			           IF(ns.id IS NOT NULL, true, false) AS is_scrapped
			    FROM news n
			    LEFT JOIN news_scraps ns ON n.id = ns.news_id AND ns.user_id = ?
			    WHERE MATCH(n.title, n.content) AGAINST (? IN NATURAL LANGUAGE MODE)
			    ORDER BY n.pub_date DESC
			    LIMIT ? OFFSET ?
			""";

		String countSql = """
			    SELECT COUNT(*)
			    FROM news n
			    WHERE MATCH(n.title, n.content) AGAINST (? IN NATURAL LANGUAGE MODE)
			""";

		List<NewsWithScrapDTO> content = jdbcTemplate.query(
			searchSql,
			(rs, rowNum) -> new NewsWithScrapDTO(
				rs.getLong("id"),
				rs.getString("title"),
				rs.getString("content"),
				rs.getString("publisher"),
				rs.getString("url"),
				NewsCategory.valueOf(rs.getString("category")),
				rs.getLong("views"),
				rs.getTimestamp("pub_date").toLocalDateTime(),
				rs.getBoolean("is_scrapped")
			),
			userId,
			keyword,
			pageable.getPageSize(),
			pageable.getOffset()
		);

		Integer total = jdbcTemplate.queryForObject(countSql, Integer.class, keyword);
		return PageableExecutionUtils.getPage(content, pageable, () -> total != null ? total : 0);
	}
}

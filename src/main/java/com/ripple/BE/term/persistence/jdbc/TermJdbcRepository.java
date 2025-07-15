package com.ripple.BE.term.persistence.jdbc;

import com.ripple.BE.term.persistence.dto.TermWithScrapDTO;
import com.ripple.BE.term.persistence.jpa.entity.TermJpaEntity;

import java.util.List;
import java.util.Locale;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class TermJdbcRepository {

	private final JdbcTemplate jdbcTemplate;

	private static final int BATCH_SIZE = 1000;

	@Transactional
	public void saveAllTermsByJdbcTemplate(List<TermJpaEntity> termList) {

		String insertQuery =
			"INSERT INTO terms (title, description, initial, created_date, modified_date) "
				+ "VALUES (?, ?, ?, NOW(), NOW())";

		jdbcTemplate.batchUpdate(
			insertQuery,
			termList,
			BATCH_SIZE,
			(ps, term) -> {
				ps.setString(1, term.getTitle());
				ps.setString(2, term.getDescription());
				ps.setString(3, term.getInitial());
			});
	}

	@Transactional(readOnly = true)
	public Page<TermWithScrapDTO> searchTerms(String keyword, Pageable pageable, long userId) {
		if (keyword == null || keyword.trim().isEmpty()) {
			return Page.empty(pageable);
		}

		String searchSql = """
			SELECT t.id, t.title, t.description, t.initial,
				IF(ts.id IS NOT NULL, true, false) AS is_scrapped,
			    (
			    	1.5 * MATCH(t.title) AGAINST (? IN NATURAL LANGUAGE MODE) +
			    	0.5 * MATCH(t.description) AGAINST (? IN NATURAL LANGUAGE MODE)
			    ) AS score
			FROM terms t
			LEFT JOIN term_scraps ts ON t.id = ts.term_id AND ts.user_id = ?
			WHERE MATCH(t.title, t.description) AGAINST (? IN BOOLEAN MODE)
			ORDER BY score DESC
			LIMIT ? OFFSET ?
			""";

		String countSql = """
			SELECT COUNT(*)
			FROM terms t
			WHERE MATCH(t.title, t.description) AGAINST (? IN NATURAL LANGUAGE MODE)
			""";

		return getTermWithScrapDTOS(keyword, buildBooleanKeyword(keyword), pageable, userId, searchSql, countSql);
	}

	private Page<TermWithScrapDTO> getTermWithScrapDTOS(String keyword, String booleanKeyword, Pageable pageable, long userId,
		String searchSql, String countSql) {
		List<TermWithScrapDTO> content = jdbcTemplate.query(
			searchSql,
			(rs, rowNum) -> new TermWithScrapDTO(
				rs.getLong("id"),
				rs.getString("title"),
				rs.getString("description"),
				rs.getString("initial"),
				rs.getBoolean("is_scrapped")
			),
			keyword,
			keyword,
			userId,
			booleanKeyword,
			pageable.getPageSize(),
			pageable.getOffset()
		);

		Integer total = jdbcTemplate.queryForObject(countSql, Integer.class, keyword);

		return PageableExecutionUtils.getPage(content, pageable, () -> total != null ? total : 0);
	}

	private static String buildBooleanKeyword(String keyword) {
		return '"' + keyword.trim().toLowerCase(Locale.ROOT) + "*\"";
	}

}

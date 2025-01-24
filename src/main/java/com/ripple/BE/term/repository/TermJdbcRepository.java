package com.ripple.BE.term.repository;

import com.ripple.BE.term.domain.Term;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class TermJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final int BATCH_SIZE = 1000;

    @Transactional
    public void saveAllTermsByJdbcTemplate(List<Term> termList) {

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
}

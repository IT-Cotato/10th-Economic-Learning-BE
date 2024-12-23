package com.ripple.BE.learning.repository;

import com.ripple.BE.learning.domain.Quiz;
import com.ripple.BE.learning.domain.type.Purpose;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findAllByPurpose(Purpose purpose);
}

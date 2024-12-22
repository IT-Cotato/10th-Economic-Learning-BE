package com.ripple.BE.learning.repository;

import com.ripple.BE.learning.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {}

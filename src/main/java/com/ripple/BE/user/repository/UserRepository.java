package com.ripple.BE.user.repository;

import com.ripple.BE.user.domain.User;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByKeyCode(String keyCode);

	Optional<User> findByAccountEmail(String accountEmail);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select u from User u")
	List<User> findAllWithLock();

	Boolean existsByNickname(String nickname);

	@Modifying
	@Query("""
		    UPDATE User u
		    SET u.quizCount = u.quizCount + :quiz,
		        u.correctCount = u.correctCount + :correct
		    WHERE u.id = :userId
		""")
	void addQuizStats(@Param("userId") Long userId,
		@Param("quiz") int quiz,
		@Param("correct") int correct);

	@Modifying
	@Query("""
		    UPDATE User u
		    SET u.beginnerCompletedCount = u.beginnerCompletedCount + CASE WHEN :level = 'BEGINNER' THEN 1 ELSE 0 END,
		        u.intermediateCompletedCount = u.intermediateCompletedCount + CASE WHEN :level = 'INTERMEDIATE' THEN 1 ELSE 0 END,
		        u.advancedCompletedCount = u.advancedCompletedCount + CASE WHEN :level = 'ADVANCED' THEN 1 ELSE 0 END
		    WHERE u.id = :userId
		""")
	void incrementCompletedCount(@Param("userId") Long userId, @Param("level") String level);

	@Modifying
	@Query("UPDATE User u SET u.currentLevel = :newLevel WHERE u.id = :userId")
	void updateLevel(@Param("userId") Long userId, @Param("newLevel") String newLevel);
}

package com.ripple.BE.user.repository;

import com.ripple.BE.user.domain.AttendanceLog;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {

    Optional<AttendanceLog> findByAttendanceIdAndDate(Long attendanceId, LocalDate date);

    List<AttendanceLog> findByAttendanceId(Long attendanceId);

    void deleteAllByUserId(Long userId);
}

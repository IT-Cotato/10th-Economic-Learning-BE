package com.ripple.BE.user.repository;

import com.ripple.BE.user.domain.AttendanceLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
    void deleteAll();

    Optional<AttendanceLog> findByAttendanceId(Long attendanceId);
}

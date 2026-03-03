package com.exam.repository;

import com.exam.model.Exam;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    // Custom query to find exams matching a student's department and semester
    @Query("SELECT e FROM Exam e JOIN e.department d WHERE d = :dept AND e.semester = :sem")
    List<Exam> findByDepartmentAndSemester(@Param("dept") String dept, @Param("sem") Integer sem);
}
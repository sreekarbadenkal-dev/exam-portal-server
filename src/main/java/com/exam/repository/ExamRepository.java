package com.exam.repository;

import com.exam.model.Exam;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    // Correct way to check if a string exists inside an ElementCollection Set
    @Query("SELECT e FROM Exam e WHERE :dept MEMBER OF e.department AND e.semester = :sem")
    List<Exam> findByDepartmentAndSemester(@Param("dept") String dept, @Param("sem") Integer sem);
    
    @Query("SELECT e FROM Exam e WHERE :dept MEMBER OF e.department AND e.semester = :sem " +
    	       "AND e.id NOT IN (SELECT r.exam.id FROM Result r WHERE r.student.id = :studentId)")
    	List<Exam> findPendingExams(@Param("dept") String dept, 
    	                            @Param("sem") Integer sem, 
    	                            @Param("studentId") Long studentId);
    
//    @Query("SELECT e FROM Exam e WHERE :dept MEMBER OF e.department AND e.semester = :sem " +
// 	       "AND e.id IN (SELECT r.exam.id FROM Result r WHERE r.student.id = :studentId)")
// 	List<Exam> findCompletedExams(@Param("dept") String dept, 
// 	                            @Param("sem") Integer sem, 
// 	                            @Param("studentId") Long studentId);
    
}
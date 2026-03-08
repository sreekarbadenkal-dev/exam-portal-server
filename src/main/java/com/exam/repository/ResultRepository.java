package com.exam.repository;

import com.exam.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    // You can add this to find results for a specific student later
    List<Result> findByStudentId(Long studentId);
    
}
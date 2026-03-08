package com.exam.controller;

import com.exam.dto.AnswerSubmissionDTO;
import com.exam.dto.ResultDTO;
import com.exam.model.*;
import com.exam.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = {"http://localhost:5173", "https://exam-portal-client.onrender.com"})
public class ResultController {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/submit")
    @Transactional
    public ResponseEntity<?> submitExam(@RequestBody AnswerSubmissionDTO submission) {
        
        System.out.println("\n========== PROCESSING SUBMISSION ==========");
        
        // 1. Fetch Exam and Student
        Exam exam = examRepository.findById(submission.getExamid())
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        Student student = studentRepository.findById(submission.getStudentid())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Set<Question> questions = exam.getQuestions();
        int totalQuestions = (questions != null) ? questions.size() : 0;
        int score = 0;

        if (questions != null) {
            for (Question q : questions) {
                // 2. Direct ID Extraction (No more searching text!)
                String studentSelectedId = submission.getSelectedOptions().get(q.getId());
                String correctIdInDB = q.getCorrectoption();

                System.out.println("[Q ID: " + q.getId() + "] Student picked ID: [" + studentSelectedId + "] | Correct ID: [" + correctIdInDB + "]");

                // 3. Simple ID-to-ID Comparison
                if (studentSelectedId != null && studentSelectedId.equals(correctIdInDB)) {
                    score++;
                    System.out.println("-> Result: CORRECT");
                } else {
                    System.out.println("-> Result: WRONG");
                }
            }
        }

        // 4. Construct Result Object
        Result result = new Result();
        result.setExam(exam);
        result.setStudent(student);
        result.setExamTitle(exam.getTitle());
        result.setCorrectAnswers(totalQuestions); 
        result.setMarksGot(score);
        
        // Use your preferred date format
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        result.setSubmittedAt(currentTime);

        // 5. Finalize
        Result savedResult = resultRepository.save(result);
        
        System.out.println("========== GRADING COMPLETE ==========");
        System.out.println("STUDENT: " + student.getName() + " | SCORE: " + score + "/" + totalQuestions);
        System.out.println("======================================\n");
        
        return ResponseEntity.ok(savedResult);
    }
    
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getResultById(@PathVariable Long id) {
        System.out.println("Fetching details for Result ID: " + id);
        
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Result not found"));

        // FIX: Convert to DTO before returning to React
        return ResponseEntity.ok(convertToDTO(result));
    }

    // Ensure you have the convertToDTO method in this controller too
    private ResultDTO convertToDTO(Result result) {
        ResultDTO dto = new ResultDTO();
        dto.setId(result.getId());
        dto.setExamTitle(result.getExamTitle());
        dto.setMarksGot(result.getMarksGot());
        dto.setCorrectAnswers(result.getCorrectAnswers());
        dto.setSubmittedAt(result.getSubmittedAt());
        return dto;
    }
}
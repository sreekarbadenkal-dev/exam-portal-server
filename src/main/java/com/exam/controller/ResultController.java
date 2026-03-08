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
import java.util.ArrayList;
import java.util.List;
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

        // Initialize Result and the list to hold student choices
        Result result = new Result();
        List<StudentAnswer> studentAnswerList = new ArrayList<>();

        if (questions != null) {
            for (Question q : questions) {
                // 2. Extract IDs for grading
                String studentSelectedId = submission.getSelectedOptions().get(q.getId());
                String correctIdInDB = q.getCorrectoption();

                // 3. Create StudentAnswer record to track history
                StudentAnswer sa = new StudentAnswer();
                sa.setQuestionId(q.getId());
                // Convert String ID from JSON to Long for Database
                sa.setSelectedOptionId(studentSelectedId != null ? Long.parseLong(studentSelectedId) : null);
                sa.setResult(result); // Set bidirectional relationship
                studentAnswerList.add(sa);

                System.out.println("[Q ID: " + q.getId() + "] Student picked: [" + studentSelectedId + "]");

                // 4. Comparison Logic
                if (studentSelectedId != null && studentSelectedId.equals(correctIdInDB)) {
                    score++;
                }
            }
        }

        // 5. Finalize Result Object
        result.setExam(exam);
        result.setStudent(student);
        result.setExamTitle(exam.getTitle());
        result.setCorrectAnswers(totalQuestions); 
        result.setMarksGot(score);
        result.setStudentAnswers(studentAnswerList); // Link the list to the result
        
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        result.setSubmittedAt(currentTime);

        // 6. Save (CascadeType.ALL in Result.java handles student_answer inserts)
        Result savedResult = resultRepository.save(result);
        
        System.out.println("========== GRADING COMPLETE ==========");
        System.out.println("STUDENT: " + student.getName() + " | SCORE: " + score + "/" + totalQuestions);
        
        return ResponseEntity.ok(savedResult);
    }
    
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getResultById(@PathVariable Long id) {
        System.out.println("Fetching details for Result ID: " + id);
        
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Result not found"));

        return ResponseEntity.ok(convertToDTO(result));
    }

    private ResultDTO convertToDTO(Result result) {
        ResultDTO dto = new ResultDTO();
        dto.setId(result.getId());
        dto.setExamTitle(result.getExamTitle());
        dto.setMarksGot(result.getMarksGot());
        dto.setCorrectAnswers(result.getCorrectAnswers());
        dto.setSubmittedAt(result.getSubmittedAt());
        dto.setExam(result.getExam());
        // Crucial: Pass the list back so React can show Red/Blue highlights
        dto.setStudentAnswers(result.getStudentAnswers()); 
        return dto;
    }
}
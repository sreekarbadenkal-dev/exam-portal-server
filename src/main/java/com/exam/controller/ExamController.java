package com.exam.controller;

import com.exam.model.*;
import com.exam.dto.*;
import com.exam.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = {"http://localhost:5173", "https://exam-portal-client.onrender.com"})
public class ExamController {

    @Autowired
    private ExamRepository examRepository;

    /**
     * 1. Admin: Create a new Exam (POST)
     * Implements the UUID Handshake to map correct options reliably.
     */
    @PostMapping("/create")
    @Transactional // CRITICAL: Keeps the session alive for the ID swap
    public ResponseEntity<Exam> createExam(@RequestBody Exam exam) {
        // A. Setup Relationships
        if (exam.getQuestions() != null) {
            for (Question q : exam.getQuestions()) {
                q.setExam(exam);
                if (q.getOptions() != null) {
                    for (Option opt : q.getOptions()) {
                        opt.setQuestion(q);
                    }
                }
            }
        }

        // B. First Pass: Save the Exam to generate real Database IDs
        Exam savedExam = examRepository.save(exam);

        // C. Second Pass: The Handshake
        // Match the 'correctoption' UUID to the generated Option ID
        for (Question q : savedExam.getQuestions()) {
            String bridgeUuid = q.getCorrectoption(); // The UUID from React

            if (q.getOptions() != null && bridgeUuid != null) {
                q.getOptions().stream()
                    .filter(opt -> bridgeUuid.equals(opt.getTempId())) // Match @Transient tempId
                    .findFirst()
                    .ifPresent(matchedOpt -> {
                        // Swap UUID for the REAL Database Primary Key (Long ID)
                        q.setCorrectoption(matchedOpt.getId().toString());
                    });
            }
        }

        // D. Final Save: Update the pointers in the database
        Exam finalExam = examRepository.save(savedExam);
        return ResponseEntity.status(HttpStatus.CREATED).body(finalExam);
    }

    // 2. Admin: Get all Exams
    @GetMapping("/all")
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    // 3. Student Dashboard: Get exams based on Dept/Semester
    @PostMapping("/studentexampage")	
    public ResponseEntity<?> setStudentsExams(@RequestBody Student student){
        String dept = student.getDepartment();
        int semester = student.getSemester();
        List<Exam> currentexams = examRepository.findByDepartmentAndSemester(dept, semester);
        
        if(currentexams.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Exams for Now");
        }
        
        List<StudentExamDTO> dtos = currentexams.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }
    
    // 4. Exam Page: Get single exam by ID
    @GetMapping("/{examid}")
    public ResponseEntity<?> getStudentExam(@PathVariable long examid){
        Optional<Exam> currentexam = examRepository.findById(examid);
        
        if(currentexam.isPresent()) {
            return ResponseEntity.ok(convertToDTO(currentexam.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exam not found");
        }
    }

    /**
     * Helper Method: Strips out correct answers for students.
     */
    private StudentExamDTO convertToDTO(Exam exam) {
        StudentExamDTO dto = new StudentExamDTO();
        dto.setId(exam.getId());
        dto.setTitle(exam.getTitle());
        dto.setExamtime(exam.getExamtime());
        dto.setSemester(exam.getSemester());
        dto.setDepartment(exam.getDepartment());

        if (exam.getQuestions() != null) {
            dto.setQuestions(exam.getQuestions().stream().map(q -> {
                StudentQuestionDTO qDto = new StudentQuestionDTO();
                qDto.setId(q.getId());
                qDto.setQuestiontext(q.getQuestiontext());
                
                if (q.getOptions() != null) {
                    qDto.setOptions(q.getOptions().stream().map(opt -> {
                        StudentOptionDTO optDto = new StudentOptionDTO();
                        optDto.setId(opt.getId());
                        optDto.setText(opt.getText());
                        return optDto;
                    }).collect(Collectors.toSet()));
                }
                return qDto;
            }).collect(Collectors.toSet()));
        }
        return dto;
    }
}
package com.exam.controller;

import com.exam.model.Exam;
import com.exam.model.Student;
import com.exam.repository.ExamRepository;
//import com.exam.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = {"http://localhost:5173","https://your-app-name.onrender.com"}) // Connects to your React app
public class ExamController {

    @Autowired
    private ExamRepository examRepository;
    //@Autowired
    //    private StudentRepository studentrepo;
    // 1. Create a new Exam (POST)
    @PostMapping("/create")
    public Exam createExam(@RequestBody Exam exam) {
        return examRepository.save(exam);
    }

    // 2. Get all Exams (GET)
    @GetMapping("/all")
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }
    
// // 2. Get all Exams (GET)
//    @GetMapping("/all")
//    public List<Exam> getAllStudentExams() {
//        return examRepository.findAll();
//    }
//    
    @PostMapping("/studentexampage")	
    public ResponseEntity<?> setStudentsExams(@RequestBody Student student){
    	String dept=student.getDepartment();
    	int semester=student.getSemester();
    	List<Exam> currentexams=examRepository.findByDepartmentAndSemester(dept, semester);
    	if(currentexams.isEmpty()) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Exams for Now");
    	}
    	
    	return ResponseEntity.ok(currentexams);
    }
}
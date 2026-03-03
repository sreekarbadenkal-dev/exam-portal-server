package com.exam.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.model.Student;
import com.exam.repository.StudentRepository;

@RestController
@RequestMapping("/api/studentlogin")
@CrossOrigin(origins="http://localhost:5173")
public class StudentController {
	
	@Autowired
	private StudentRepository studentrepo;
	
	@GetMapping("/all")
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(studentrepo.findAll());
    }
	
	@PostMapping
	public ResponseEntity<?> login(@RequestBody Map<String,String> loginData)
	{
		String email=loginData.get("email");
		String password=loginData.get("password");
		Optional <Student> logingstudent=studentrepo.findByEmail(email);
		if(logingstudent.isPresent()) {
			Student loggedstudent=logingstudent.get();
			if(loggedstudent.getPassword().equals(password))
			{
				return ResponseEntity.ok(loggedstudent);
			}
			else {
				return ResponseEntity.status(401).body("Invalid Password");
			}
			}
		else {
			return ResponseEntity.status(404).body("Student Not Found");
		}
		
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createStudent(@RequestBody Student student){
		try {
			if(studentrepo.findByEmail(student.getEmail()).isPresent()) {
				return ResponseEntity.badRequest().body("User already exsists");
			}
				Student savedstudent=studentrepo.save(student);
				return ResponseEntity.ok(savedstudent);
			}
		catch(Exception e) {
			return ResponseEntity.status(500).body("Registering student went wrong");
		}
	}
}

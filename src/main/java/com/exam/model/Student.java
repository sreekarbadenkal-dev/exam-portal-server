package com.exam.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@JsonProperty("username")
	private String name;
	
	@JsonProperty("email")
	@Column(unique = true, nullable = false)
	private String email;
	
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("rollnumber")
	@Column(unique = true, nullable = false)
	private String rollnumber;
	
	@JsonProperty("department")
	private String department;
	
	@JsonProperty("semester")
	private int semester;
	
//	public String getPassword() {
//	    return this.password;
//	}

}

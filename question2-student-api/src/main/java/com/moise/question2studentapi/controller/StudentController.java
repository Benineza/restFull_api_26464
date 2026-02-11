package com.moise.question2studentapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moise.question2studentapi.model.Student;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private List<Student> studentList = new ArrayList<>();

    public StudentController() {
        studentList.add(new Student(1L, "Kim", "Manzi", "kim@gmail.com", "Software", 3.8));
        studentList.add(new Student(2L, "Bob", "Cyusa", "bob@gmail.com", "Networks", 3.2));
        studentList.add(new Student(3L, "Kelly", "Shema", "kelly@gmail.com", "Information", 3.6));
        studentList.add(new Student(4L, "Dev", "Mugisha", "dev@gmail.com", "Software", 3.0));
        studentList.add(new Student(5L, "Sam", "David", "sam@gmail.com", "Networks", 3.9));
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentList);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long studentId) {
        return studentList.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/major/{major}")
    public ResponseEntity<List<Student>> getStudentsByMajor(@PathVariable String major) {
        List<Student> results = studentList.stream()
                .filter(s -> s.getMajor().equalsIgnoreCase(major))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Student>> filterByGpa(@RequestParam Double gpa) {
        List<Student> results = studentList.stream()
                .filter(s -> s.getGpa() >= gpa)
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student newStudent) {
        newStudent.setStudentId((long) (studentList.size() + 1));
        studentList.add(newStudent);
        return ResponseEntity.status(HttpStatus.CREATED).body(newStudent);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long studentId, @RequestBody Student updatedStudent) {
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).getStudentId().equals(studentId)) {
                updatedStudent.setStudentId(studentId); // preserve ID
                studentList.set(i, updatedStudent);
                return ResponseEntity.ok(updatedStudent);
            }
        }
        return ResponseEntity.notFound().build();
    }
}

package com.example.springboottutorialdemo.service;

import com.example.springboottutorialdemo.entity.StudentEntity;
import com.example.springboottutorialdemo.exception.StudentNotFoundException;
import com.example.springboottutorialdemo.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@SpringBootTest
class StudentServiceTest {

    @Autowired
    StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    @DisplayName("This tests that if student id is existing, getStudentById will return the student entity")
    public void testGetStudentById_Success() {
        //given: student_id is existing
        int existing_student_id = 1;
        StudentEntity studentEntity = new StudentEntity(1,"Test Name",1, "Test Address");
        given(studentRepository.findById(existing_student_id)).willReturn(Optional.of(studentEntity));
        //when: studentService.getStudentById is executed
        StudentEntity studentServicesResult = studentService.getStudentById(existing_student_id);
        //then: return of studentService.getStudentById should be equal to return of studentRepository.findById
        assertEquals(studentServicesResult, studentEntity);
    }

    @Test
    @DisplayName("This tests that if student id is non-existing, getStudentById will throw StudentNotFoundException")
    public void testGetStudentById_Fail() {
        //given: student_id is non-existing
        int non_existing_student_id = 1;
        given(studentRepository.findById(non_existing_student_id)).willThrow(new StudentNotFoundException("Student with id : " + non_existing_student_id + " doesn't exist."));
        //when: studentService.getStudentById is executed
        StudentNotFoundException result = assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentById(non_existing_student_id);
        });
        //then: studentService.getStudentById will throw a StudentNotFoundException with message "Student with id : <non_existing student_id> doesn't exist."
        assertEquals("Student with id : 1 doesn't exist.", result.getMessage());
    }

    @Test
    @DisplayName("This test is if studentService add student method executed then it will return new student entity saved in the studentRepository")
    public void testAddNewStudent(){
        //Given new student entity
            StudentEntity studentEntity = new StudentEntity(1,"testName", 1, "testAddress");
            given(studentRepository.save(studentEntity)).willReturn(studentEntity);
        //When studentService add student method is executed
            StudentEntity result = studentService.addStudent(studentEntity);
        //Then return new student entity saved in the studentRepository
            assertEquals(result, studentEntity);
    }

    @Test
    @DisplayName("This test is if studentService delete student method executed  it will return null")
    public  void testDeleteStudentById_Success(){
        //Given existing student id
        int existing_studentId = 1;
        StudentEntity studentEntity = new StudentEntity(1,"testName", 1, "testAddress");
        given(studentRepository.findById(existing_studentId)).willReturn(Optional.of(studentEntity));
        //When the studentService deleteStudentById method is executed
        StudentEntity studentServiceResult = studentService.deleteStudentById(existing_studentId);
        //Then it should return null
        assertEquals(studentServiceResult, null);
        verify(studentRepository,atLeastOnce()).delete(studentEntity);
    }

    @Test
    @DisplayName("This test is if studentService delete student method executed it will return exception ")
    public void testDeleteStudentById_Fail(){
        //Given student id is non-existing
        int nonExisting_studentId = 1;
        StudentEntity studentEntity = new StudentEntity(1,"testName", 1, "testAddress");
        given(studentRepository.findById(nonExisting_studentId)).willThrow(new StudentNotFoundException("Student with id : " + nonExisting_studentId + " doesn't exist."));
        //When the studentService deleteStudentById method is executed
        StudentNotFoundException result = assertThrows(StudentNotFoundException.class, () ->{
            studentService.deleteStudentById(nonExisting_studentId);
        });
        //Then it should throw a StudentNotFoundException exception with message, "Student with id : <non-existing_id> doesn't exist."
        assertEquals("Student with id : " + nonExisting_studentId + " doesn't exist.", result.getMessage());
    }

}
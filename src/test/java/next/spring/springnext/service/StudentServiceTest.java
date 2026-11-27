package next.spring.springnext.service;

import next.spring.springnext.model.Student;
import next.spring.springnext.repository.StudentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StudentServiceTest {
    private StudentRepo studentRepo;
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentRepo = mock(StudentRepo.class);
        studentService = new StudentService(studentRepo);
    }

    @Test
    void getStudentsReturnsRecordsFromRepository() {
        List<Student> students = List.of(new Student(1, "Avery", "avery@example.com", "Science"));
        when(studentRepo.findAll()).thenReturn(students);

        assertEquals(students, studentService.getStudents());
        verify(studentRepo).findAll();
    }

    @Test
    void getStudentReturnsRecordFromRepository() {
        Student student = new Student(1, "Avery", "avery@example.com", "Science");
        when(studentRepo.findById(1)).thenReturn(Optional.of(student));

        assertEquals(student, studentService.getStudent(1));
        verify(studentRepo).findById(1);
    }

    @Test
    void getStudentThrowsNotFoundWhenRecordDoesNotExist() {
        when(studentRepo.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> studentService.getStudent(1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void createStudentSavesRecord() {
        Student student = new Student(1, "Avery", "avery@example.com", "Science");
        when(studentRepo.save(student)).thenReturn(student);

        assertEquals(student, studentService.createStudent(student));
        verify(studentRepo).save(student);
    }

    @Test
    void updateStudentChangesExistingRecord() {
        Student existingStudent = new Student(1, "Avery", "avery@example.com", "Science");
        Student updates = new Student(2, "Jordan", "jordan@example.com", "Arts");
        when(studentRepo.findById(1)).thenReturn(Optional.of(existingStudent));
        when(studentRepo.save(existingStudent)).thenReturn(existingStudent);

        Student updatedStudent = studentService.updateStudent(1, updates);

        assertEquals(1, updatedStudent.getId());
        assertEquals("Jordan", updatedStudent.getName());
        assertEquals("jordan@example.com", updatedStudent.getEmail());
        assertEquals("Arts", updatedStudent.getDepartment());
        verify(studentRepo).save(existingStudent);
    }

    @Test
    void deleteStudentDeletesExistingRecord() {
        Student student = new Student(1, "Avery", "avery@example.com", "Science");
        when(studentRepo.findById(1)).thenReturn(Optional.of(student));

        studentService.deleteStudent(1);

        verify(studentRepo).delete(student);
    }
}
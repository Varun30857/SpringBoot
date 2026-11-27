package next.spring.springnext.controller;

import next.spring.springnext.model.Student;
import next.spring.springnext.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class StudentControllerTest {
    private StudentService studentService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        studentService = mock(StudentService.class);
        mockMvc = standaloneSetup(new StudentController(studentService)).build();
    }

    @Test
    void getStudentsReturnsList() throws Exception {
        when(studentService.getStudents()).thenReturn(List.of(
                new Student(1, "Avery", "avery@example.com", "Science")));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Avery\",\"email\":\"avery@example.com\",\"department\":\"Science\"}]"));
    }

    @Test
    void getStudentReturnsRecord() throws Exception {
        when(studentService.getStudent(1)).thenReturn(
                new Student(1, "Avery", "avery@example.com", "Science"));

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Avery\",\"email\":\"avery@example.com\",\"department\":\"Science\"}"));
    }

    @Test
    void createStudentReturnsCreatedRecord() throws Exception {
        Student student = new Student(1, "Avery", "avery@example.com", "Science");
        when(studentService.createStudent(org.mockito.ArgumentMatchers.any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Avery\",\"email\":\"avery@example.com\",\"department\":\"Science\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"name\":\"Avery\",\"email\":\"avery@example.com\",\"department\":\"Science\"}"));
    }

    @Test
    void updateStudentReturnsUpdatedRecord() throws Exception {
        Student student = new Student(1, "Avery", "avery@example.com", "Science");
        when(studentService.updateStudent(org.mockito.ArgumentMatchers.eq(1), org.mockito.ArgumentMatchers.any(Student.class)))
                .thenReturn(student);

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Avery\",\"email\":\"avery@example.com\",\"department\":\"Science\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Avery\",\"email\":\"avery@example.com\",\"department\":\"Science\"}"));
    }

    @Test
    void deleteStudentReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent(1);
    }
}
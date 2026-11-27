package next.spring.springnext.service;

import next.spring.springnext.model.Student;
import next.spring.springnext.repository.StudentRepo;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StudentService {
    private final StudentRepo studentRepo;

    public StudentService(StudentRepo studentRepo)
    {
        this.studentRepo = studentRepo;
    }

    public List<Student> getStudents()
    {
        return studentRepo.findAll();
    }

    public Student getStudent(int id)
    {

        return findStudent(id);
    }

    public Student createStudent(Student student)
    {
        return studentRepo.save(student);
    }

    public Student updateStudent(int id, Student student) {
        Student existingStudent = findStudent(id);
        existingStudent.setName(student.getName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setDepartment(student.getDepartment());
        return studentRepo.save(existingStudent);
    }

    public void deleteStudent(int id) {
        studentRepo.delete(findStudent(id));
    }

    private Student findStudent(int id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }
}

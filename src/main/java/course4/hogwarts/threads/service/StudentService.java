package course4.hogwarts.threads.service;

import course4.hogwarts.threads.model.Student;
import course4.hogwarts.threads.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.Collection;
import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;


    public Student createStudent(Student student){
        return studentRepository.save(student);
    }
    public Student findStudent(long id){
        return studentRepository.findById(id).orElse(null);
    }
    public Student editStudent(Student student){
        return studentRepository.save(student);
    }
    public void deleteStudent(long id){
        studentRepository.deleteById(id);
    }
    public Collection<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public Collection<Student> findByAgeBetween(Long min, Long max){
        return studentRepository.findByAgeBetween(min, max);
    }

    public Long getTotalStudents() {
        return studentRepository.countAllStudents();
    }
    public Long getAvgAge(){
        return studentRepository.avgAgeStudents();
    }
    public List<Student> getLastFiveStudents() {
        Pageable pageable = (Pageable) PageRequest.of(0, 5);
        return studentRepository.findLastFiveStudents(pageable);
    }
}


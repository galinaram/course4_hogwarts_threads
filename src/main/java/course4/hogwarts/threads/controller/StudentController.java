package course4.hogwarts.threads.controller;

import course4.hogwarts.threads.model.Student;
import course4.hogwarts.threads.service.AvatarService;
import course4.hogwarts.threads.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("students")
public class StudentController {
    @Autowired
    public StudentService studentService;
    @Autowired
    public AvatarService avatarService;

    @GetMapping ("/info")
    public ResponseEntity getInfoAboutAuthor() {
        return ResponseEntity.ok("Author of this application is Good person!");
    }

    @GetMapping ("/all")
    public ResponseEntity<Collection<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable long id){
        Student student = studentService.findStudent(id);
        if (student == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student){
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student){
        Student editedStudent = studentService.editStudent(student);
        if (editedStudent == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable long id){
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getStudentWithAgeBetween(@RequestParam(required = false) Long min,
                                                                        @RequestParam(required = false) Long max){
        if (min != null && max != null){
            return ResponseEntity.ok(studentService.findByAgeBetween(min, max));
        }
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createStudent (@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() >= 1024 * 300){
            return  ResponseEntity.badRequest().body("File is too big");
        }
        avatarService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public Long getCountOfStudents() {
        return studentService.getTotalStudents();
    }
    @GetMapping("/avg")
    public Long getAvgAgeStudents(){
        return studentService.getAvgAge();
    }
    @GetMapping("/last-five")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping("/print-parallel")
    public void getStudentsParallel() {
        for (int i = 0; i < 2 && i < studentService.getAllStudents().size(); i++) {
            System.out.println(studentService.findStudent(i).getName());
        }

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            for (int i = 2; i < 4 && i < studentService.getAllStudents().size(); i++) {
                System.out.println(studentService.findStudent(i).getName());
            }
        });

        executor.submit(() -> {
            for (int i = 4; i < 6 && i < studentService.getAllStudents().size(); i++) {
                System.out.println(studentService.findStudent(i).getName());
            }
        });

        executor.shutdown();
    }
    @GetMapping("/print-synchronized")
    public void getStudentsSynchronized() {
        printStudentName(0);
        printStudentName(1);

        for (int i = 0; i < 2 && i < studentService.getAllStudents().size(); i++) {
            printStudentName(i);
        }

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            for (int i = 2; i < 4 && i < studentService.getAllStudents().size(); i++) {
                printStudentName(i);
            }
        });

        executor.submit(() -> {
            for (int i = 4; i < 6 && i < studentService.getAllStudents().size(); i++) {
                printStudentName(i);
            }
        });

        executor.shutdown();
    }

    private void printStudentName(int index) {
        synchronized (StudentService.class){
            if (index < studentService.getAllStudents().size()) {
                String name = studentService.findStudent(index).getName();
                System.out.println(name);
            }
        }
    }
}

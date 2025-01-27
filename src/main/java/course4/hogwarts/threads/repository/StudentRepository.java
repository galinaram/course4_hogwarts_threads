package course4.hogwarts.threads.repository;

import course4.hogwarts.threads.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAgeBetween(Long min, Long max);
    @Override
    Student getById(Long id);

    @Query("SELECT COUNT(s) FROM Student s")
    Long countAllStudents();

    @Query("SELECT AVG(age) FROM Student s")
    Long avgAgeStudents();

    @Query(value = "SELECT s FROM Student s ORDER BY s.id DESC")
    List<Student> findLastFiveStudents(Pageable pageable);
}

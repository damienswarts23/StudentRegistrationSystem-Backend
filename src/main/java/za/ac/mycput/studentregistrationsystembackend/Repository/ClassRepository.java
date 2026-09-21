package za.ac.mycput.studentregistrationsystembackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.Class;

import java.util.List;

public interface ClassRepository extends JpaRepository<Class, Integer> {
    java.util.Optional<Class> findFirstByOrderByClassIdDesc();

    List<Class> findByCourse_CourseIdOrderByClassIdAsc(int courseId);
}
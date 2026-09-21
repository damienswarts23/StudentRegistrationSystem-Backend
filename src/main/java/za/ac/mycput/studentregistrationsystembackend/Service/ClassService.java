package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Domain.Lecturer;
import za.ac.mycput.studentregistrationsystembackend.Factory.ClassFactory;
import za.ac.mycput.studentregistrationsystembackend.Repository.ClassRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.CourseRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.DepartmentRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.LecturerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClassService {

    private final ClassRepository classRepository;
    private final LecturerRepository lecturerRepository;
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;

    public ClassService(
            ClassRepository classRepository,
            LecturerRepository lecturerRepository,
            CourseRepository courseRepository,
            DepartmentRepository departmentRepository) {

        this.classRepository = classRepository;
        this.lecturerRepository = lecturerRepository;
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
    }

    public Class create(Class courseClass) {

        if (!courseClass.getLecturers().isEmpty()) {
            throw new IllegalArgumentException(
                    "A class must be created before lecturers are assigned"
            );
        }

        int classId = classRepository.findFirstByOrderByClassIdDesc()
                .map(item -> item.getClassId() + 1).orElse(1);
        za.ac.mycput.studentregistrationsystembackend.Domain.Course course =
                courseRepository.findById(courseClass.getCourse().getCourseId())
                        .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        Class generatedClass = ClassFactory.createClass(classId,
                courseClass.getClassCode(), courseClass.getClassName(), course);
        return classRepository.save(generatedClass);
    }

    public Class read(int classId) {

        Optional<Class> courseClass =
                classRepository.findById(classId);

        return courseClass.orElse(null);
    }

    public Class update(Class courseClass) {

        Class existingClass = classRepository
                .findById(courseClass.getClassId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Class not found"
                        )
                );

        za.ac.mycput.studentregistrationsystembackend.Domain.Course course =
                courseRepository.findById(courseClass.getCourse().getCourseId())
                        .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Class updatedClass = new Class.Builder()
                .setClassId(existingClass.getClassId())
                .setClassCode(courseClass.getClassCode())
                .setClassName(courseClass.getClassName())
                .setCourse(course)
                .setLecturers(existingClass.getLecturers())
                .build();

        validateLecturerDepartments(updatedClass);

        return classRepository.save(updatedClass);
    }

    public boolean delete(int classId) {

        if (!classRepository.existsById(classId)) {
            return false;
        }

        classRepository.deleteById(classId);
        return true;
    }

    public List<Class> getAll() {
        return classRepository.findAll();
    }

    public List<Class> getAvailableClassesForLecturer(
            int lecturerPersonId) {

        Lecturer lecturer = lecturerRepository
                .findById(lecturerPersonId)
                .orElse(null);

        if (lecturer == null) {
            return null;
        }

        List<Class> result = new ArrayList<>();
        int departmentId = lecturer.getDepartment().getDepartmentId();
        for (Class courseClass : classRepository.findAll()) {
            if (courseClass.getCourse().getDepartment().getDepartmentId() == departmentId
                    && !courseClass.hasLecturer(lecturerPersonId)) {
                result.add(courseClass);
            }
        }
        return result;
    }

    public Class assignLecturer(
            int classId,
            int lecturerPersonId) {

        Class courseClass = classRepository
                .findById(classId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Class not found"
                        )
                );

        Lecturer lecturer = lecturerRepository
                .findById(lecturerPersonId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Lecturer not found"
                        )
                );

        if (courseClass.hasLecturer(lecturerPersonId)) {
            return courseClass;
        }

        int classDepartmentId =
                courseClass
                        .getCourse()
                        .getDepartment()
                        .getDepartmentId();

        int lecturerDepartmentId =
                lecturer
                        .getDepartment()
                        .getDepartmentId();

        if (classDepartmentId != lecturerDepartmentId) {

            throw new IllegalArgumentException(
                    "Lecturer can only teach classes in their department"
            );
        }

        courseClass.addLecturer(lecturer);
        return classRepository.save(courseClass);
    }


    public Class unassignLecturer(int classId, int lecturerPersonId) {

        Class courseClass = classRepository
                .findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));

        if (!courseClass.hasLecturer(lecturerPersonId)) {
            return courseClass;
        }

        courseClass.removeLecturer(lecturerPersonId);
        return classRepository.save(courseClass);
    }

    /*
     * With the many-to-many rule every class in the department is available
     * for another lecturer too, even if another lecturer already teaches it.
     */
    public List<Class> getAvailableClassesForDepartment(int departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            return List.of();
        }
        List<Class> result = new ArrayList<>();
        for (Class courseClass : classRepository.findAll()) {
            if (courseClass.getCourse().getDepartment().getDepartmentId() == departmentId) {
                result.add(courseClass);
            }
        }
        return result;
    }

    public List<Class> getByCourse(int courseId) {
        return classRepository.findByCourse_CourseIdOrderByClassIdAsc(courseId);
    }

    private void validateLecturerDepartments(Class courseClass) {
        int classDepartmentId = courseClass.getCourse().getDepartment().getDepartmentId();
        for (Lecturer lecturer : courseClass.getLecturers()) {
            if (lecturer.getDepartment().getDepartmentId() != classDepartmentId) {
                throw new IllegalArgumentException(
                        "Lecturer can only teach classes in their department"
                );
            }
        }
    }
}

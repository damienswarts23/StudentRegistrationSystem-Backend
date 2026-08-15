package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Domain.Lecturer;
import za.ac.mycput.studentregistrationsystembackend.Repository.ClassRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.LecturerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClassService {

    private final ClassRepository classRepository;
    private final LecturerRepository lecturerRepository;

    public ClassService(
            ClassRepository classRepository,
            LecturerRepository lecturerRepository) {

        this.classRepository = classRepository;
        this.lecturerRepository = lecturerRepository;
    }

    public Class create(Class courseClass) {

        if (courseClass.getLecturer() != null) {
            throw new IllegalArgumentException(
                    "A class must be created before a lecturer is assigned"
            );
        }

        return classRepository.save(courseClass);
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

        Class updatedClass = new Class.Builder()
                .setClassId(existingClass.getClassId())
                .setClassCode(courseClass.getClassCode())
                .setClassName(courseClass.getClassName())
                .setCourse(courseClass.getCourse())
                .setLecturer(existingClass.getLecturer())
                .build();

        if (updatedClass.getLecturer() != null) {
            validateLecturerDepartment(updatedClass);
        }

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

        return classRepository
                .findByCourse_DepartmentAndLecturerIsNull(
                        lecturer.getDepartment()
                );
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

        if (courseClass.getLecturer() != null) {
            throw new IllegalArgumentException(
                    "Class already has a lecturer"
            );
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

        Class updatedClass = new Class.Builder()
                .setClassId(courseClass.getClassId())
                .setClassCode(courseClass.getClassCode())
                .setClassName(courseClass.getClassName())
                .setCourse(courseClass.getCourse())
                .setLecturer(lecturer)
                .build();

        return classRepository.save(updatedClass);
    }

    private void validateLecturerDepartment(
            Class courseClass) {

        int classDepartmentId =
                courseClass
                        .getCourse()
                        .getDepartment()
                        .getDepartmentId();

        int lecturerDepartmentId =
                courseClass
                        .getLecturer()
                        .getDepartment()
                        .getDepartmentId();

        if (classDepartmentId != lecturerDepartmentId) {

            throw new IllegalArgumentException(
                    "Lecturer can only teach classes in their department"
            );
        }
    }
}
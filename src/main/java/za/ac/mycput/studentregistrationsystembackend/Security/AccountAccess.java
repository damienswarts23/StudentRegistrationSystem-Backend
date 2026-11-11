package za.ac.mycput.studentregistrationsystembackend.Security;

import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.*;
import jakarta.servlet.http.HttpSession;
import za.ac.mycput.studentregistrationsystembackend.Repository.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;

@Component("accountAccess")
public class AccountAccess {
    private final AppUserRepository users;
    private final StudentRepository students;
    private final LecturerRepository lecturers;
    private final RegistrationRepository registrations;
    private final ClassRepository classes;

    public AccountAccess(AppUserRepository users, StudentRepository students, LecturerRepository lecturers, RegistrationRepository registrations, ClassRepository classes) {
        this.users = users;
        this.students = students;
        this.lecturers = lecturers;
        this.registrations = registrations;
        this.classes = classes;
    }

    public AppUser current() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        return a == null ? null : users.findByUsernameIgnoreCase(a.getName()).orElse(null);
    }

    public boolean admin() {
        var u = current();
        return u != null && u.getRole() == Role.ADMIN;
    }

    public boolean canActivateStudent(int id) {
        if (admin()) return true;
        var session = session();
        if (session == null || !(session.getAttribute("newApplicantId") instanceof Integer applicantId)) return false;
        return students.findById(id).map(student -> student.getApplicant().getPersonId() == applicantId.intValue()).orElse(false);
    }

    public boolean student(int id) {
        var u = current();
        return admin() || u != null && u.getRole() == Role.STUDENT && Integer.valueOf(id).equals(u.getLinkedId()) && students.existsById(id);
    }

    public boolean lecturer(int id) {
        var u = current();
        return admin() || u != null && u.getRole() == Role.LECTURER && Integer.valueOf(id).equals(u.getLinkedId()) && lecturers.existsById(id);
    }

    public boolean lecturerEmail(String email) {
        return admin() || lecturers.findByLecturerEmailIgnoreCase(email).map(l -> lecturer(l.getPersonId())).orElse(false);
    }

    public boolean registration(int id) {
        return admin() || registrations.findById(id).map(r -> student(r.getStudent().getStudentId())).orElse(false);
    }

    public boolean classStudents(String code) {
        if (admin()) return true;
        var u = current();
        if (u == null || u.getRole() != Role.LECTURER) return false;
        return classes.findAll().stream().filter(c -> c.getClassCode().equals(code))
                .anyMatch(c -> c.getLecturers().stream().anyMatch(l -> Integer.valueOf(l.getPersonId()).equals(u.getLinkedId())));
    }

    private HttpSession session() {
        var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs == null ? null : attrs.getRequest().getSession(false);
    }

    public boolean applicant(int id) {
        if (admin()) return true;
        var s = session();
        return s != null && Integer.valueOf(id).equals(s.getAttribute("newApplicantId"));
    }

    public boolean application(Application application) {
        return application != null && application.getApplicant() != null && applicant(application.getApplicant().getPersonId());
    }

    public Integer resultPersonId() {
        var u = current();
        if (u != null && u.getRole() == Role.STUDENT && u.getLinkedId() != null)
            return students.findById(u.getLinkedId()).map(s -> s.getApplicant().getPersonId()).orElse(null);
        var s = session();
        return s == null ? null : (Integer) s.getAttribute("newApplicantId");
    }

    public boolean result(String email) {
        if (admin()) return true;
        var u = current();
        if (u != null && u.getRole() == Role.STUDENT && u.getLinkedId() != null) {
            var student = students.findById(u.getLinkedId()).orElse(null);
            if (student != null && (student.getStudentEmail().equalsIgnoreCase(email) || student.getApplicant().getContactDetails().getEmail().equalsIgnoreCase(email)))
                return true;
        }
        var s = session();
        return s != null && email.equalsIgnoreCase(String.valueOf(s.getAttribute("newApplicantEmail")));
    }
}

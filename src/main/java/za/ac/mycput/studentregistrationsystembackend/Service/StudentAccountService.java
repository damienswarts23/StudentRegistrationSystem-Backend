package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import za.ac.mycput.studentregistrationsystembackend.Domain.AppUser;
import za.ac.mycput.studentregistrationsystembackend.Domain.Role;
import za.ac.mycput.studentregistrationsystembackend.Domain.Student;
import za.ac.mycput.studentregistrationsystembackend.Repository.AppUserRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.StudentRepository;

@Service
public class StudentAccountService {
    private final StudentRepository records;
    private final AppUserRepository users;
    private final PasswordEncoder encoder;
    public StudentAccountService(StudentRepository records,AppUserRepository users,PasswordEncoder encoder) {
        this.records=records;this.users=users;this.encoder=encoder;
    }
    @Transactional
    public String enable(int studentId,String password) {
        validate(password);
        Student student=records.findById(studentId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Student not found."));
        provision(student,password);
        return student.getStudentEmail();
    }
    @Transactional
    public String reset(int studentId,String password) {
        validate(password);
        Student student=records.findById(studentId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Student not found."));
        var existing=users.findByUsernameIgnoreCase(student.getStudentEmail());
        if(existing.isEmpty())provision(student,password);
        else {
            AppUser account=existing.get();
            if(account.getRole()!=Role.STUDENT || !Integer.valueOf(studentId).equals(account.getLinkedId()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,"This email belongs to a different account. No changes were made.");
            account.setPassword(encoder.encode(password));users.saveAndFlush(account);
        }
        return student.getStudentEmail();
    }
    private void provision(Student student,String password) {
        String email=student.getStudentEmail();
        if(email==null || email.isBlank())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This student needs a student email.");
        if(users.findByUsernameIgnoreCase(email).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT,"A login already exists for this email. Its password has not been changed.");
        users.saveAndFlush(new AppUser(email,encoder.encode(password),Role.STUDENT,
            student.getApplicant().getFirstName()+" "+student.getApplicant().getLastName(),student.getStudentId()));
    }
    private static void validate(String password) {
        if(password==null || password.length()<10 || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72
            || password.chars().noneMatch(Character::isUpperCase) || password.chars().noneMatch(Character::isLowerCase)
            || password.chars().noneMatch(Character::isDigit))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Use 10+ characters with uppercase, lowercase and a number (maximum 72 UTF-8 bytes).");
    }
}

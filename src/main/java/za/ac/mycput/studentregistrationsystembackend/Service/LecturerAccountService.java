package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import za.ac.mycput.studentregistrationsystembackend.Domain.AppUser;
import za.ac.mycput.studentregistrationsystembackend.Domain.Role;
import za.ac.mycput.studentregistrationsystembackend.Domain.Lecturer;
import za.ac.mycput.studentregistrationsystembackend.Repository.AppUserRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.LecturerRepository;

@Service
public class LecturerAccountService {
    private final LecturerService lecturers;
    private final LecturerRepository records;
    private final AppUserRepository users;
    private final PasswordEncoder encoder;
    public LecturerAccountService(LecturerService lecturers,LecturerRepository records,AppUserRepository users,PasswordEncoder encoder) {
        this.lecturers=lecturers;this.records=records;this.users=users;this.encoder=encoder;
    }
    @Transactional
    public Lecturer create(Lecturer lecturer,String password) {
        validate(password);
        if(lecturer==null)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Lecturer details are required.");
        Lecturer saved=lecturers.create(lecturer);
        provision(saved,password);
        return saved;
    }
    @Transactional
    public String enable(int personId,String password) {
        validate(password);
        Lecturer lecturer=records.findById(personId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Lecturer not found."));
        provision(lecturer,password);
        return lecturer.getLecturerEmail();
    }
    private void provision(Lecturer lecturer,String password) {
        String email=lecturer.getLecturerEmail();
        if(email==null || email.isBlank())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This lecturer needs a lecturer email.");
        if(users.findByUsernameIgnoreCase(email).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT,"A login already exists for this email. Its password has not been changed.");
        users.saveAndFlush(new AppUser(email,encoder.encode(password),Role.LECTURER,
            lecturer.getFirstName()+" "+lecturer.getLastName(),lecturer.getPersonId()));
    }
    private static void validate(String password) {
        if(password==null || password.length()<10 || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72
            || password.chars().noneMatch(Character::isUpperCase) || password.chars().noneMatch(Character::isLowerCase)
            || password.chars().noneMatch(Character::isDigit))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Use 10+ characters with uppercase, lowercase and a number (maximum 72 UTF-8 bytes).");
    }
}

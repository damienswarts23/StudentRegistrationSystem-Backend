package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.*;
import za.ac.mycput.studentregistrationsystembackend.Repository.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.Role;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AppUserRepository users;
    private final StudentRepository students;
    private final LecturerRepository lecturers;

    public CustomUserDetailsService(AppUserRepository users, StudentRepository students, LecturerRepository lecturers) {
        this.users = users;
        this.students = students;
        this.lecturers = lecturers;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var u = users.findByUsernameIgnoreCase(username.trim()).orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
        boolean active = u.getRole() == Role.ADMIN || (u.getLinkedId() != null &&
                (u.getRole() == Role.STUDENT ? students.existsById(u.getLinkedId()) : lecturers.existsById(u.getLinkedId())));
        // Explicit ROLE_ authority mapping matches the lecturer's example.
        return new User(u.getUsername(), u.getPassword(), active, true, true, true,
                java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + u.getRole().name())));
    }
}

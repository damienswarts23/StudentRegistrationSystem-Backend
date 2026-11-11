package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import za.ac.mycput.studentregistrationsystembackend.Repository.AppUserRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.AppUser;
import za.ac.mycput.studentregistrationsystembackend.Domain.Role;

/**
 * Account persistence follows the lecturer's UserService pattern.
 */
@Service
public class AccountService {
    private final AppUserRepository users;
    private final PasswordEncoder encoder;

    public AccountService(AppUserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Transactional
    public void changePassword(String username, String currentPassword, String newPassword) {
        var u = users.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in again."));
        if (!encoder.matches(currentPassword, u.getPassword())) bad("Current password is incorrect.");
        validatePassword(newPassword);
        u.setPassword(encoder.encode(newPassword));
        users.save(u);
    }

    @Transactional
    public String createAdmin(Role role, String username, String displayName, String password) {
        if (role != Role.ADMIN)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only administrator accounts can be created here.");
        validatePassword(password);
        username = username.trim().toLowerCase(java.util.Locale.ROOT);
        displayName = displayName.trim();
        if (!username.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+") || username.length() > 254 || displayName.isBlank() || displayName.length() > 100 || displayName.chars().anyMatch(Character::isISOControl))
            bad("Enter the admin's email and name.");
        if (users.findByUsernameIgnoreCase(username).isPresent()) bad("That account already exists.");
        users.save(new AppUser(username, encoder.encode(password), Role.ADMIN, displayName, null));
        return username;
    }

    private static void validatePassword(String password) {
        int bytes = password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length;
        if (password.length() < 10 || bytes > 72 || password.chars().noneMatch(Character::isUpperCase) || password.chars().noneMatch(Character::isLowerCase) || password.chars().noneMatch(Character::isDigit))
            bad("Use 10 or more characters with uppercase, lowercase and a number (maximum 72 UTF-8 bytes).");
    }

    private static void bad(String message) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}

package za.ac.mycput.studentregistrationsystembackend.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.*;
import org.springframework.transaction.annotation.Transactional;
import za.ac.mycput.studentregistrationsystembackend.Repository.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Security.AccountAccess;

import java.io.*;
import java.util.Base64;
import javax.imageio.ImageIO;

@RestController
@RequestMapping("/api/auth")
@PreAuthorize("isAuthenticated()")
public class AccountController {
    private final AppUserRepository users;
    private final za.ac.mycput.studentregistrationsystembackend.Service.AccountService accountService;
    private final AccountAccess access;
    private final StudentRepository students;
    private final LecturerRepository lecturers;

    public AccountController(AppUserRepository users, za.ac.mycput.studentregistrationsystembackend.Service.AccountService accountService, AccountAccess access, StudentRepository students, LecturerRepository lecturers) {
        this.users = users;
        this.accountService = accountService;
        this.access = access;
        this.students = students;
        this.lecturers = lecturers;
    }

    @GetMapping(value = "/csrf", produces = "text/plain")
    @PreAuthorize("permitAll()")
    public String csrf(CsrfToken token) {
        return token.getToken();
    }

    private AppUser current() {
        AppUser u = access.current();
        if (u == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in again.");
        return u;
    }

    @GetMapping(value = "/me", produces = "text/plain")
    public String me() {
        var u = current();
        return u.getUsername() + "\n" + u.getRole() + "\n" + u.getDisplayName() + "\n" +
                (u.getLinkedId() == null ? "" : u.getLinkedId()) + "\n" + (u.getPhoto() == null ? "" : Base64.getEncoder().encodeToString(u.getPhoto()));
    }

    @PostMapping("/profile")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void profile(@RequestParam String displayName) {
        displayName = displayName.trim();
        if (displayName.isBlank() || displayName.length() > 100 || displayName.chars().anyMatch(Character::isISOControl))
            bad("Enter a name of 1–100 characters.");
        var u = current();
        u.setDisplayName(displayName);
        users.save(u);
    }

    @PostMapping("/photo")
    @Transactional
    public void photo(HttpServletRequest request) throws IOException {
        byte[] bytes = request.getInputStream().readNBytes(2 * 1024 * 1024 + 1);
        if (bytes.length == 0 || bytes.length > 2 * 1024 * 1024) bad("Choose a PNG or JPEG photo smaller than 2 MB.");
        try (var input = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {
            var readers = ImageIO.getImageReaders(input);
            if (!readers.hasNext()) bad("The file is not a readable image.");
            var reader = readers.next();
            try {
                reader.setInput(input);
                String format = reader.getFormatName();
                if (!format.equalsIgnoreCase("png") && !format.equalsIgnoreCase("jpeg")) bad("Use PNG or JPEG.");
                int w = reader.getWidth(0), h = reader.getHeight(0);
                if (w < 1 || h < 1 || w > 6000 || h > 6000 || (long) w * h > 16000000)
                    bad("Photo dimensions are too large.");
                var original = reader.read(0);
                double scale = Math.min(1.0, 512.0 / Math.max(w, h));
                var scaled = new java.awt.image.BufferedImage(Math.max(1, (int) (w * scale)), Math.max(1, (int) (h * scale)), java.awt.image.BufferedImage.TYPE_INT_RGB);
                var g = scaled.createGraphics();
                g.setColor(java.awt.Color.WHITE);
                g.fillRect(0, 0, scaled.getWidth(), scaled.getHeight());
                g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g.drawImage(original, 0, 0, scaled.getWidth(), scaled.getHeight(), null);
                g.dispose();
                var output = new ByteArrayOutputStream();
                ImageIO.write(scaled, "png", output);
                var u = current();
                u.setPhoto(output.toByteArray());
                users.save(u);
            } finally {
                reader.dispose();
            }
        }
    }

    @DeleteMapping("/photo")
    @Transactional
    public void removePhoto() {
        var u = current();
        u.setPhoto(null);
        users.save(u);
    }

    @PostMapping("/password")
    @Transactional
    public void password(@RequestParam String currentPassword, @RequestParam String newPassword, HttpServletRequest request) {
        accountService.changePassword(current().getUsername(), currentPassword, newPassword);
        // Expire this login; the client must sign in using the new password.
        request.getSession().invalidate();
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }

    @PostMapping(value = "/accounts", produces = "text/plain")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public String createAccount(@RequestParam Role role, @RequestParam(defaultValue = "0") int linkedId,
                                @RequestParam(defaultValue = "") String username, @RequestParam(defaultValue = "") String displayName, @RequestParam String password) {
        return accountService.createAdmin(role, username, displayName, password);
    }

    private static void bad(String message) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}

package za.ac.mycput.studentregistrationsystembackend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import za.ac.mycput.studentregistrationsystembackend.Repository.AppUserRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.AppUser;
import za.ac.mycput.studentregistrationsystembackend.Domain.Role;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initializeAdmin(AppUserRepository users, PasswordEncoder encoder,
                                      @Value("${app.admin.username:admin@cput.ac.za}") String username,
                                      @Value("${app.admin.password:Admin@2026!}") String password,
                                      @Value("${app.admin.name:Damien Swarts}") String name) {
        return args -> {
            if (users.findByUsernameIgnoreCase(username).isEmpty())
                users.save(new AppUser(username, encoder.encode(password), Role.ADMIN, name, null));
        };
    }
}

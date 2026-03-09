package config;

import com.example.cinema.entity.UserEntity;
import com.example.cinema.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInit(UserRepository userRepository,
                     PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        String username = System.getenv("ADMIN_USERNAME");
        String rawPassword = System.getenv("ADMIN_PASSWORD");

        if (username == null || rawPassword == null) {
            System.out.println("\nADMIN NOT CREATED — переменные среды не заданы\n");
            return;
        }

        if (!userRepository.existsByUsername(username)) {
            UserEntity admin = new UserEntity();
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode(rawPassword));
            admin.setRole("ADMIN");
            userRepository.save(admin);

            System.out.println("\nADMIN CREATED: " + username + "\n");
        } else {
            System.out.println("\nADMIN ALREADY EXISTS\n");
        }
    }
}
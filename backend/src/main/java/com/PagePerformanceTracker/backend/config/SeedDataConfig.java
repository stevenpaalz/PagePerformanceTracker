package com.PagePerformanceTracker.backend.config;

        import com.PagePerformanceTracker.backend.models.Role;
        import com.PagePerformanceTracker.backend.models.User;
        import com.PagePerformanceTracker.backend.repositories.UserRepository;
        import com.PagePerformanceTracker.backend.service.UserService;
        import lombok.RequiredArgsConstructor;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.boot.CommandLineRunner;
        import org.springframework.security.crypto.password.PasswordEncoder;
        import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {

            User admin = User
                    .builder()
                    .firstName("admin")
                    .lastName("admin")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ROLE_ADMIN)
                    .build();

            userService.save(admin);
            log.debug("created ADMIN user - {}", admin);
        }
    }

}
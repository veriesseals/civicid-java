package com.civicid.apps.accounts;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserService userService;

    public DataInitializer(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
           userService.createUser(
                   "admin",
                   "admin@civicid.gov",
                   "Tyaanah201!",
                   Role.SUPER_ADMIN,
                   "Administration"
           );
           System.out.println("Default admin user created.");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}

package com.mitrais.rms;

import java.util.HashSet;
import java.util.Set;

import com.devskiller.jfairy.Fairy;
import com.mitrais.rms.domains.Role;
import com.mitrais.rms.domains.User;
import com.mitrais.rms.repositories.RoleRepository;
import com.mitrais.rms.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("development")
@Component
@Transactional
public class Seeder implements CommandLineRunner {

    private Logger logger;
    private String[] roles = { "admin", "user" };

    @Autowired
    private Fairy faker;

    @Autowired
    private PasswordEncoder encoder;

    Seeder() {
        this.logger = LoggerFactory.getLogger(Seeder.class);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        this.generateRoles();
        this.generateUsers();
    }

    private void generateUsers() {
        this.logger.info("Populate list of user.");

        User person;
        Set<User> users = new HashSet<User>();

        for (int i = 0; i < 10; i++) {
            long id = (i % 2) == 0 ? 1 : 2;
            person = new User();
            person.setUsername(faker.person().getUsername());
            person.setName(faker.person().getFullName());
            person.setEmail(faker.person().getEmail());
            person.setPassword(encoder.encode("password"));
            person.getRoles().add(roleRepository.findById(id).get());
            users.add(person);
        }

        userRepository.saveAll(users);
        this.logger.info("Finished populating users.");
    }

    private void generateRoles() {
        this.logger.info("Populate list of role.");
        Set<Role> roles = new HashSet<Role>();

        Role role;

        for (int i = 0; i < this.roles.length; i++) {
            role = new Role();
            role.setName(this.roles[i]);
            roles.add(role);
        }

        roleRepository.saveAll(roles);
        this.logger.info("Finished populating role");
    }

}
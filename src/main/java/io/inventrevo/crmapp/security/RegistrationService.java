package io.inventrevo.crmapp.security;

import io.inventrevo.crmapp.user.AccountType;
import io.inventrevo.crmapp.user.User;
import io.inventrevo.crmapp.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(final UserRepository userRepository,
            final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(final RegistrationRequest registrationRequest) {
        log.info("registering new user: {}", registrationRequest.getUsername());

        final User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setFullname(registrationRequest.getFullname());
        user.setAddress(registrationRequest.getAddress());
        user.setAccountType(AccountType.SALESPERSON);
        userRepository.save(user);
    }

}

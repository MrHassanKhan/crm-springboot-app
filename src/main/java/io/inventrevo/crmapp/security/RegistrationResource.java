package io.inventrevo.crmapp.security;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RegistrationResource {

    private final RegistrationService registrationService;

    public RegistrationResource(final RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody @Valid final RegistrationRequest registrationRequest) {
        registrationService.register(registrationRequest);
        return ResponseEntity.ok().build();
    }

}

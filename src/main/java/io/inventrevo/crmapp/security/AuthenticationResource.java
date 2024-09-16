package io.inventrevo.crmapp.security;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class AuthenticationResource {

    private final AuthenticationManager authenticationManager;
    private final JwtSecurityUserDetailsService jwtSecurityUserDetailsService;
    private final JwtSecurityTokenService jwtSecurityTokenService;

    public AuthenticationResource(final AuthenticationManager authenticationManager,
            final JwtSecurityUserDetailsService jwtSecurityUserDetailsService,
            final JwtSecurityTokenService jwtSecurityTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtSecurityUserDetailsService = jwtSecurityUserDetailsService;
        this.jwtSecurityTokenService = jwtSecurityTokenService;
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(
            @RequestBody @Valid final AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = jwtSecurityUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(jwtSecurityTokenService.generateToken(userDetails));
        return authenticationResponse;
    }

}

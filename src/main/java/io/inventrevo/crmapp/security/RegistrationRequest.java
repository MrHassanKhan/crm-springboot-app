package io.inventrevo.crmapp.security;

import io.inventrevo.crmapp.user.AccountType;
import io.inventrevo.crmapp.user.UserEmailUnique;
import io.inventrevo.crmapp.user.UserUsernameUnique;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegistrationRequest {

    @NotNull
    @Size(max = 255)
    @UserUsernameUnique(message = "{registration.register.taken}")
    private String username;

    @NotNull
    @Size(max = 255)
    @UserEmailUnique
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    @Size(max = 255)
    private String fullname;

    @Size(max = 255)
    private String address;

    @NotNull
    private AccountType accountType;

}

package io.inventrevo.crmapp.lead;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LeadDTO {

    private Long id;

    @Size(max = 255)
    private String firstname;

    @Size(max = 255)
    private String lastname;

    @NotNull
    @Size(max = 255)
    @LeadEmailUnique
    private String email;

    @Size(max = 255)
    private String phone;

    private LeadStatus status;

    private String assignedUserName;

}

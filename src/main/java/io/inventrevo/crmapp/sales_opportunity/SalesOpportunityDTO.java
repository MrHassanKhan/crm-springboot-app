package io.inventrevo.crmapp.sales_opportunity;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SalesOpportunityDTO {

    private Long id;

    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String description;

    private SalesOpportunityStage stage;

    @Size(max = 255)
    private String value;

    private Long lead;

    private Long assignedTo;

}

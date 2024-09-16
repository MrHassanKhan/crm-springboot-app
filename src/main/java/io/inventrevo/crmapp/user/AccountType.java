package io.inventrevo.crmapp.user;

import lombok.experimental.FieldNameConstants;


@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum AccountType {

    @FieldNameConstants.Include
    ADMIN,
    @FieldNameConstants.Include
    SALESPERSON

}

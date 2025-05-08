package sesch0357.crud.dto;

import jakarta.validation.constraints.NotBlank;

public record EmployeeRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String phoneNumber
) {}

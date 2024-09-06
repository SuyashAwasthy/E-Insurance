package com.techlabs.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancellationRequestDto {

    @NotNull(message = "Policy ID is required")
    private Long policyId;

    @NotBlank(message = "Reason for cancellation is required")
    private String cancellationReason;

    @NotBlank(message = "Bank Name is required")
    private String bankName;

    @NotBlank(message = "Branch Name is required")
    private String branchName;

    @NotBlank(message = "Bank Account ID is required")
    private String bankAccountId;

    @NotBlank(message = "IFSC Code is required")
    private String ifscCode;
}

package com.techlabs.app.service;

import java.util.List;
import java.util.Optional;

import com.techlabs.app.dto.ClaimDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.ClaimResponseDto;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.util.PagedResponse;

public interface ClaimService {

	List<ClaimDto> getAllClaims();

	void approveClaim(Long claimId);

	void rejectClaim(Long claimId);

	ClaimResponseDto processClaim(ClaimRequestDto claimRequestDto, Long customerId);

	PagedResponse<ClaimDto> getAllClaims(int page, int size);

	void approveClaim(Long claimId, String remark);

	void rejectClaim(Long claimId, String remark);

	ClaimDto getClaimStatusByPolicyId(Long policyId, Long customerId);

	Optional<Claim> getClaimStatusByPolicyId(Long policyId);

}

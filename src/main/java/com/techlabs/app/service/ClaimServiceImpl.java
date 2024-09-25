package com.techlabs.app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.ClaimDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.ClaimResponseDto;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.Payment;
import com.techlabs.app.repository.ClaimRepository;
import com.techlabs.app.repository.InsurancePolicyRepository;
import com.techlabs.app.repository.PaymentRepository;
import com.techlabs.app.util.PagedResponse;

import jakarta.transaction.Transactional;

@Service
public class ClaimServiceImpl implements ClaimService {

	@Autowired
	private ClaimRepository claimRepository;
	private InsurancePolicy policy;
	@Autowired 
	private PaymentRepository paymentRepository;

	@Autowired
	private InsurancePolicyRepository policyRepository;

//	@Override
//	public List<ClaimDto> getAllClaims() {
//		List<Claim> claims = claimRepository.findAll();
//		return claims.stream().map(ClaimDto::new).collect(Collectors.toList());
//	}

	@Transactional
	public void approveClaim(Long claimId,String remark) {
		Claim claim = claimRepository.findById(claimId).orElseThrow(() -> new RuntimeException("Claim not found"));
		InsurancePolicy policy = claim.getPolicy();
		boolean check = policy.getMaturityDate().isBefore(claim.getDate().toLocalDate());
		if (check && policy.getTotalAmountPaid() == policy.getPremiumAmount()) {
			policy.setPolicyStatus("CLAIMED");
		} else {
			policy.setPolicyStatus("DROPED");
		}
		policyRepository.save(policy);
		claim.setClaimedStatus("APPROVED"); // Set status to APPROVED
		claim.setRemark(remark); // Set the remark
		// Logic to transfer amount to customer can be added here
		claimRepository.save(claim);
	}

	@Transactional
	public void rejectClaim(Long claimId,String remark) {
		Claim claim = claimRepository.findById(claimId).orElseThrow(() -> new RuntimeException("Claim not found"));

		claim.setClaimedStatus("REJECTED"); // Set status to REJECTED
		  claim.setRemark(remark); // Set the remark
		claimRepository.save(claim);
	}

	@Override
	public ClaimResponseDto processClaim(ClaimRequestDto claimRequestDto, Long customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagedResponse<ClaimDto> getAllClaims(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Claim> claimsPage = claimRepository.findAll(pageable);
		
		// Fetch payments for the claims, adjust this logic as necessary
	    List<Payment> allPayments = paymentRepository.findAll(); // Assuming you can fetch all payments

		//List<ClaimDto> claimDtos = claimsPage.stream().map(ClaimDto::new).collect(Collectors.toList());
		List<ClaimDto> claimDtos = claimsPage.stream()
		        .map(claim -> new ClaimDto(claim, allPayments.stream()
		                .filter(payment -> payment.getPolicy().getInsuranceId() == claim.getPolicy().getInsuranceId()) // Filter payments for the current claim
		                .collect(Collectors.toList()))
		        )
		        .collect(Collectors.toList());
		return new PagedResponse<>(claimDtos, claimsPage.getNumber(), claimsPage.getSize(),
				claimsPage.getTotalElements(), claimsPage.getTotalPages(), claimsPage.isLast());
	}

	@Override
	public List<ClaimDto> getAllClaims() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void approveClaim(Long claimId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rejectClaim(Long claimId) {
		// TODO Auto-generated method stub
		
	}

	 public ClaimDto getClaimStatusByPolicyId(Long policyId, Long customerId) {
	        Optional<Claim> claim = claimRepository.findByPolicyId(policyId);
	        if (claim.isPresent()) {
	            // Convert the Claim entity to a DTO for response
	            return new ClaimDto(
	                claim.get().getClaimedStatus(),
	                claim.get().getBankName(),
	                claim.get().getBranchName(),
	                claim.get().getBankAccountId(),
	                claim.get().getIfscCode()
	            );
	        }
	        return null; // Claim not found
	    }

	@Override
	public Optional<Claim> getClaimStatusByPolicyId(Long policyId) {
		Optional<Claim> claim = claimRepository.findByPolicyId(policyId);
	    
	    if (claim.isPresent()) {
	        System.out.println("Claim Status: " + claim.get().getClaimedStatus());
	    } else {
	        System.out.println("No claim found for the given policyId.");
	    }
	    
	    return claim;
}
}

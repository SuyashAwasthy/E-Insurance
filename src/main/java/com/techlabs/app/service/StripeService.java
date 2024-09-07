//package com.techlabs.app.service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.Charge;
//import com.techlabs.app.dto.StripeChargeDto;
//
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class StripeService {
//	@Value("${stripe.key}")
//    private String stripeApiKey;
//
//
//    @PostConstruct
//    public void init(){
//
//        Stripe.apiKey = stripeApiKey;
//    }
//
//    public StripeChargeDto charge(StripeChargeDto chargeRequest) {
//
//
//        try {
//            chargeRequest.setSuccess(false);
//            Map<String, Object> chargeParams = new HashMap<>();
//            chargeParams.put("amount", (int) (chargeRequest.getAmount() * 100));
//            chargeParams.put("currency", "USD");
//            chargeParams.put("description", "Payment for id " + chargeRequest.getAdditionalInfo().getOrDefault("ID_TAG", ""));
//            chargeParams.put("source", chargeRequest.getStripeToken());
//            Map<String, Object> metaData = new HashMap<>();
//            metaData.put("id", chargeRequest.getChargeId());
//            metaData.putAll(chargeRequest.getAdditionalInfo());
//            chargeParams.put("metadata", metaData);
//            Charge charge = Charge.create(chargeParams);
//            chargeRequest.setMessage(charge.getOutcome().getSellerMessage());
//
//            if (charge.getPaid()) {
//                chargeRequest.setChargeId(charge.getId());
//                chargeRequest.setSuccess(true);
//
//            }
//            return chargeRequest;
//        } catch (StripeException e) {
//            throw new RuntimeException(e.getMessage());
//        }
//
//    }
//
//
//}

//package com.techlabs.app.service;
//
//import com.techlabs.app.dto.StripeChargeDto;
//import com.techlabs.app.entity.Agent;
//import com.techlabs.app.entity.Commission;
//import com.techlabs.app.entity.InsurancePolicy;
//import com.techlabs.app.entity.PayementStatus;
//import com.techlabs.app.entity.Payment;
//import com.techlabs.app.repository.AgentRepository;
//import com.techlabs.app.repository.CommissionRepository;
//import com.techlabs.app.repository.InsurancePolicyRepository;
//import com.techlabs.app.repository.PaymentRepository;
//import com.stripe.exception.StripeException;
//import com.stripe.model.Charge;
//import com.stripe.Stripe;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import jakarta.annotation.PostConstruct;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class StripeService {
//    
//    @Value("${stripe.key}")
//    private String stripeApiKey;
//    
//    private final PaymentRepository paymentRepository;
//    private final CommissionRepository commissionRepository;
//    private final AgentRepository agentRepository;
//    private final InsurancePolicyRepository insurancePolicyRepository;
//    
//   
//    public StripeService(String stripeApiKey, PaymentRepository paymentRepository,
//			CommissionRepository commissionRepository, AgentRepository agentRepository,
//			InsurancePolicyRepository insurancePolicyRepository) {
//		
//		
//		this.paymentRepository = paymentRepository;
//		this.commissionRepository = commissionRepository;
//		this.agentRepository = agentRepository;
//		this.insurancePolicyRepository = insurancePolicyRepository;
//	}
//
//	@PostConstruct
//    public void init() {
//        Stripe.apiKey = stripeApiKey;
//    }
//
//    public StripeChargeDto charge(StripeChargeDto chargeRequest) {
//
//        try {
//            chargeRequest.setSuccess(false);
//
//            Map<String, Object> chargeParams = new HashMap<>();
//            chargeParams.put("amount", (int) (chargeRequest.getAmount() * 100));
//            chargeParams.put("currency", "USD");
//            chargeParams.put("description", "Payment for id " + chargeRequest.getAdditionalInfo().getOrDefault("ID_TAG", ""));
//            chargeParams.put("source", chargeRequest.getStripeToken());
//            Map<String, Object> metaData = new HashMap<>();
//            metaData.put("id", chargeRequest.getChargeId());
//            metaData.putAll(chargeRequest.getAdditionalInfo());
//            chargeParams.put("metadata", metaData);
//
//            Charge charge = Charge.create(chargeParams);
//            chargeRequest.setMessage(charge.getOutcome().getSellerMessage());
//
//            if (charge.getPaid()) {
//                chargeRequest.setChargeId(charge.getId());
//                chargeRequest.setSuccess(true);
//                
//                // Save payment details
//                Payment payment = new Payment();
//                payment.setPaymentType("Stripe");
//                payment.setAmount(chargeRequest.getAmount());
//                payment.setTax(0.0); // Assume tax is 0 for simplicity
//                payment.setTotalPayment(chargeRequest.getAmount());
//                payment.setPaymentStatus(PayementStatus.PAID.name());
//                paymentRepository.save(payment);
//              
//				
//
//                
//                // Assuming you have the policyId and agentId in chargeRequest.additionalInfo
//                Long policyId = Long.valueOf(chargeRequest.getAdditionalInfo().get("policyId").toString());
//                Long agentId = Long.valueOf(chargeRequest.getAdditionalInfo().get("agentId").toString());
//                
//                Agent agent = agentRepository.findById(agentId)
//                        .orElseThrow(() -> new RuntimeException("Agent not found with id: " + agentId));
//                
//                InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(policyId)
//                        .orElseThrow(() -> new RuntimeException("Insurance Policy not found with id: " + policyId));
//
//                // Save commission details
//                Commission commission = new Commission();
//                commission.setAgent(agent);
//                commission.setInsurancePolicy(insurancePolicy);
//                commission.setAmount(chargeRequest.getAmount() * 0.1); // Example commission calculation
//                commission.setDate(LocalDateTime.now());
//                commission.setCommissionType("Registration");
//                commissionRepository.save(commission);
//            }
//
//            return chargeRequest;
//        } catch (StripeException e) {
//            throw new RuntimeException(e.getMessage());
//        }
//    }
//}
//

package com.techlabs.app.service;

import com.techlabs.app.dto.StripeChargeDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.Commission;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.PayementStatus;
import com.techlabs.app.entity.Payment;
import com.techlabs.app.exception.AgentNotFoundException;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.ClaimRepository;
import com.techlabs.app.repository.CommissionRepository;
import com.techlabs.app.repository.InsurancePolicyRepository;
import com.techlabs.app.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.Stripe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//@Service
//public class StripeService {
//
//    @Value("${stripe.key}")
//    private String stripeApiKey;
//@Autowired
//    private  PaymentRepository paymentRepository;
//@Autowired
//    private  CommissionRepository commissionRepository;
//@Autowired
//    private  AgentRepository agentRepository;
//@Autowired
//    private InsurancePolicyRepository insurancePolicyRepository;
//
//@Autowired
//private ClaimRepository claimRepository;
//
//    
//
//    public StripeService(String stripeApiKey, PaymentRepository paymentRepository,
//		CommissionRepository commissionRepository, AgentRepository agentRepository,
//		InsurancePolicyRepository insurancePolicyRepository, ClaimRepository claimRepository) {
//	super();
//	this.stripeApiKey = stripeApiKey;
//	this.paymentRepository = paymentRepository;
//	this.commissionRepository = commissionRepository;
//	this.agentRepository = agentRepository;
//	this.insurancePolicyRepository = insurancePolicyRepository;
//	this.claimRepository = claimRepository;
//}
//
//	@PostConstruct
//    public void init() {
//        Stripe.apiKey = stripeApiKey;
//    }
//
//    public StripeChargeDto charge(StripeChargeDto chargeRequest) {
//
//        try {
//            chargeRequest.setSuccess(false);
//
//            Map<String, Object> chargeParams = new HashMap<>();
//            chargeParams.put("amount", (int) (chargeRequest.getAmount() * 100));
//            chargeParams.put("currency", "USD");
//            chargeParams.put("description", "Payment for id " + chargeRequest.getAdditionalInfo().getOrDefault("ID_TAG", ""));
//            chargeParams.put("source", chargeRequest.getStripeToken());
//            Map<String, Object> metaData = new HashMap<>();
//            metaData.put("id", chargeRequest.getChargeId());
//            metaData.putAll(chargeRequest.getAdditionalInfo());
//            chargeParams.put("metadata", metaData);
//            
//          
//            Charge charge = Charge.create(chargeParams);
//            chargeRequest.setMessage(charge.getOutcome().getSellerMessage());
//
//            if (charge.getPaid()) {
//                chargeRequest.setChargeId(charge.getId());
//                chargeRequest.setSuccess(true);
//               
//
//                // Save payment details
//                Payment payment = new Payment();
//                payment.setPaymentType("Stripe");
//                payment.setAmount(chargeRequest.getAmount());
//                payment.setTax(0.0); // Assume tax is 0 for simplicity
//                payment.setTotalPayment(chargeRequest.getAmount());
//                payment.setPaymentStatus(PayementStatus.PAID.name());
//
//                paymentRepository.save(payment);
//
//                // Assuming you have the policyId and agentId in chargeRequest.additionalInfo
//                Long policyId = Long.valueOf(chargeRequest.getAdditionalInfo().get("policyId").toString());
//                Long agentId = Long.valueOf(chargeRequest.getAdditionalInfo().get("agentId").toString());
//
//                
//                Agent agent = agentRepository.findById(agentId)
//                        .orElseThrow(() -> new RuntimeException("Agent not found with id: " + agentId));
//
//                InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(policyId)
//                        .orElseThrow(() -> new RuntimeException("Insurance Policy not found with id: " + policyId));
//
//                // Save commission details
//                Commission commission = new Commission();
//                commission.setAgent(agent);
//                commission.setInsurancePolicy(insurancePolicy);
//                commission.setAmount(chargeRequest.getAmount() * 0.1); // Example commission calculation
//                commission.setDate(LocalDateTime.now());
//                commission.setCommissionType("PAID");
//                commissionRepository.save(commission);
//            }
//
//            return chargeRequest;
//        } catch (StripeException e) {
//            throw new RuntimeException(e.getMessage());
//        }
//    }
//}
@Service
public class StripeService {

    @Value("${stripe.key}")
    private String stripeApiKey;

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private CommissionRepository commissionRepository;
    
    @Autowired
    private AgentRepository agentRepository;
    
    @Autowired
    private InsurancePolicyRepository insurancePolicyRepository;

    public StripeService(PaymentRepository paymentRepository,
                         CommissionRepository commissionRepository,
                         AgentRepository agentRepository,
                         InsurancePolicyRepository insurancePolicyRepository) {
        this.paymentRepository = paymentRepository;
        this.commissionRepository = commissionRepository;
        this.agentRepository = agentRepository;
        this.insurancePolicyRepository = insurancePolicyRepository;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    // Method to calculate the total paid amount for a policy
    private double calculateTotalPaidAmount(InsurancePolicy policy) {
        return policy.getPayments().stream()
                     .mapToDouble(Payment::getTotalPayment)
                     .sum();
    }

    public StripeChargeDto charge(StripeChargeDto chargeRequest) {
        try {
            chargeRequest.setSuccess(false);

            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", (int) (chargeRequest.getAmount() * 100));
            chargeParams.put("currency", "USD");
            chargeParams.put("description", "Payment for id " + chargeRequest.getAdditionalInfo().getOrDefault("ID_TAG", ""));
            chargeParams.put("source", chargeRequest.getStripeToken());
            Map<String, Object> metaData = new HashMap<>();
            metaData.put("id", chargeRequest.getChargeId());
            metaData.putAll(chargeRequest.getAdditionalInfo());
            chargeParams.put("metadata", metaData);

            Charge charge = Charge.create(chargeParams);
            chargeRequest.setMessage(charge.getOutcome().getSellerMessage());

            if (charge.getPaid()) {
                chargeRequest.setChargeId(charge.getId());
                chargeRequest.setSuccess(true);

                // Save payment details
                Payment payment = new Payment();
                payment.setPaymentType("Stripe");
                payment.setAmount(chargeRequest.getAmount());
                payment.setTax(0.0); // Assume tax is 0 for simplicity
                payment.setTotalPayment(chargeRequest.getAmount());
                payment.setPaymentStatus(PayementStatus.PAID.name());
                paymentRepository.save(payment);

                // Assuming you have the policyId and agentId in chargeRequest.additionalInfo
                Long policyId = Long.valueOf(chargeRequest.getAdditionalInfo().get("policyId").toString());
                Long agentId = Long.valueOf(chargeRequest.getAdditionalInfo().get("agentId").toString());

                Agent agent = agentRepository.findById(agentId)
                        .orElseThrow(() -> new RuntimeException("Agent not found with id: " + agentId));

                InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(policyId)
                        .orElseThrow(() -> new RuntimeException("Insurance Policy not found with id: " + policyId));

                // Calculate the new total paid amount
                double totalPaidAmount = calculateTotalPaidAmount(insurancePolicy) + chargeRequest.getAmount();

                // Save commission details
                Commission commission = new Commission();
                commission.setAgent(agent);
                commission.setInsurancePolicy(insurancePolicy);
                commission.setAmount(chargeRequest.getAmount() * 0.1); // Example commission calculation
                commission.setDate(LocalDateTime.now());
                commission.setCommissionType("PAID");
                commissionRepository.save(commission);
// Update total amount paid in the policy
                insurancePolicy.setTotalAmountPaid(totalPaidAmount);
                insurancePolicyRepository.save(insurancePolicy);
            }

            return chargeRequest;
        } catch (StripeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}


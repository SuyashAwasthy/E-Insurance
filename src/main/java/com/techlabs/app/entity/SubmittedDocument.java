package com.techlabs.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SubmittedDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String documentName;

    @NotBlank
    @Column(nullable = false)
    private String documentStatus=DocumentStatus.PENDING.name();

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String documentImage;
    
//    @ManyToOne
//    @JoinColumn(name = "customer_id", nullable = false) // Foreign key column
//    private Customer customer; // Link to the Customer entity

    @ManyToOne
    @JoinColumn(name = "insurance_policy_id", nullable = false)
    private InsurancePolicy insurancePolicy;
}

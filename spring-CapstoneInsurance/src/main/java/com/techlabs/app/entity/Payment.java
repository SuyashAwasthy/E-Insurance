package com.techlabs.app.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String paymentType;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime paymentDate=LocalDateTime.now();

    @Column(nullable = false)
    private Double tax;

    @Column(nullable = false)
    private Double totalPayment;

    @NotBlank
    @Column(nullable = false)
    private String cardNumber;

    @NotNull
    @Column(nullable = false)
    private Integer cvv;

    @Column(nullable = false)
//    Validation(5 digit)
    private String expiry;

    @Column(nullable = false)
    private String paymentStatus= PayementStatus.UNPAID.name();
}


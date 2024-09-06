package com.techlabs.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String paymentType;

  @Column(nullable = false)
  private Double amount;

  @Column(nullable = false)
  private LocalDateTime paymentDate = LocalDateTime.now();

  @Column(nullable = false)
  private Double tax;

  @Column(nullable = false)
  private Double totalPayment;

  @Column(nullable = false)
  private String paymentStatus = PayementStatus.UNPAID.name();

  
}

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String documentName;

    @NotBlank
    @Column(nullable = false)
    private String documentStatus=DocumentStatus.PENDING.name();

    @NotBlank
    @Column(nullable = false)
    private String documentImage;
}

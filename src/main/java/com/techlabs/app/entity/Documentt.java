package com.techlabs.app.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "documentttsss")
public class Documentt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name="documentId")
	private long documentId;
	
	//@Enumerated(EnumType.STRING)
	@Column(name="document_name")
	private DocumentType documentName;
	
	@Column(name="verified")
	private boolean verified;
	
	@ManyToOne
	@JoinColumn(name="customer_id", referencedColumnName="customerId")
	private Customer customer;
//	
	@ManyToOne
	@JoinColumn(name="verified_by",referencedColumnName="employeeId")
	private Employee verifyBy;
	
	@Lob
	@Column(name="content",columnDefinition="LONGBLOB")
	private byte[] content;
	
			

}

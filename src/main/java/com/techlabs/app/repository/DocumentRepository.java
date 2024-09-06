package com.techlabs.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.City;
import com.techlabs.app.entity.SubmittedDocument;

@Repository
public interface DocumentRepository extends JpaRepository<SubmittedDocument, Long>{

}

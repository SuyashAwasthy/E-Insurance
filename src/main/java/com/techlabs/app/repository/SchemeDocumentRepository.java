package com.techlabs.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.SchemeDocument;

@Repository
public interface SchemeDocumentRepository  extends JpaRepository<SchemeDocument, Long> {

}

package com.techlabs.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.OtpEntity;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long>{

	 List<OtpEntity> findByEmail(String email);

}

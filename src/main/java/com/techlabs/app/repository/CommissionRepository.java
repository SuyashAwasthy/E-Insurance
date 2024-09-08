package com.techlabs.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Commission;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Long>{

	//List<Commission> findCommissionsByDateRange(LocalDateTime atStartOfDay, LocalDateTime atTime);

	List<Commission> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

	List<Commission> findCommissionsByDateBetween(LocalDateTime atStartOfDay, LocalDateTime atTime);


}

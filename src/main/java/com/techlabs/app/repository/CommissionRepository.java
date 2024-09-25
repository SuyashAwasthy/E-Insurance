package com.techlabs.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Commission;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Long>{

	//List<Commission> findCommissionsByDateRange(LocalDateTime atStartOfDay, LocalDateTime atTime);

	List<Commission> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

	List<Commission> findCommissionsByDateBetween(LocalDateTime atStartOfDay, LocalDateTime atTime); List<Commission> findByAgent(Agent agent);
	
	@Modifying
    @Query("UPDATE Commission c SET c.availableCommission = c.availableCommission - :amount WHERE c.agent.id = :agentId")
    void updateCommissionBalance(@Param("agentId") Long agentId, @Param("amount") double amount);
	
	@Query("SELECT c FROM Commission c WHERE c.agent.id = :agentId")
    List<Commission> findCommissionsForAgent(@Param("agentId") Long agentId);
	
	@Query("SELECT SUM(c.amount) FROM Commission c WHERE c.agent.id = :agentId")
    Double getTotalCommissionForAgent(@Param("agentId") Long agentId);


}

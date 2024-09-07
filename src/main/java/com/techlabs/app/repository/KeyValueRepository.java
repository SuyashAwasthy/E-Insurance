package com.techlabs.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techlabs.app.entity.KeyValueEntity;

public interface KeyValueRepository extends JpaRepository<KeyValueEntity, Long>{
	
	@Query("SELECT k.value FROM KeyValueEntity k WHERE k.settingKey = :key")
    String getValueByKey(@Param("key") String key);

}

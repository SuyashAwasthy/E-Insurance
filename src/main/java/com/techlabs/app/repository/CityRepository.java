package com.techlabs.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.City;
import com.techlabs.app.entity.State;
@Repository
public interface CityRepository extends JpaRepository<City, Long>{

	

//	 @Query("SELECT COUNT(c) > 0 FROM City c WHERE c.cityName = :cityName AND c.state.id = :stateId")
//	    boolean existsByCityNameAndStateId(@Param("cityName") String cityName, @Param("stateId") Long stateId);
	
//	List<City> findByStateId(long stateId);

	//List<City> findByStateId(long id);
	List<City> findByState_StateId(Long stateId);

	// Correct method to check for the existence of a city with a specific name and state
   // boolean existsByCity_nameAndState_Id(String city_name, Long state_id);

	//boolean existsByCity_nameAndState_StateId(String city_name, Long stateId);
	}

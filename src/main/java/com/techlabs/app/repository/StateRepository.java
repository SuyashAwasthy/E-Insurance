package com.techlabs.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.City;
import com.techlabs.app.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long>{

	Optional<State> findByName(String name);


}

package com.techlabs.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Employee;
import com.techlabs.app.entity.User;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	Page<Employee> findByIsActive(boolean active, Pageable pageable);

	Optional<Employee> findByUser(User user);

}

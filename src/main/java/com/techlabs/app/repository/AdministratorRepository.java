package com.techlabs.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Administrator;
import com.techlabs.app.entity.User;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

}

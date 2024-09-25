package com.techlabs.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.app.repository.CustomerQueryRepository;

@Service
public class CustomerQueryServiceImpl implements CustomerQueryService {
	@Autowired
    private CustomerQueryRepository queryRepository;

    @Override
    public int countTotalQueries() {
        return queryRepository.countAllQueries();
    }

}

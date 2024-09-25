package com.techlabs.app.service;

import java.util.List;
import java.util.Arrays;


import org.springframework.stereotype.Service;

import com.techlabs.app.entity.DocumentType;

@Service
public class DocumenetTypeServiceImpl implements DocumentTypeService {

	@Override
	public List<DocumentType> findAll() {
		 return Arrays.asList(DocumentType.values());
	}

}

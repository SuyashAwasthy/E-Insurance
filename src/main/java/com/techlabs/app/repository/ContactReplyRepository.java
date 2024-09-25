package com.techlabs.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.entity.ContactMessage;
import com.techlabs.app.entity.ContactReply;

public interface ContactReplyRepository extends JpaRepository<ContactReply, Long>{

}

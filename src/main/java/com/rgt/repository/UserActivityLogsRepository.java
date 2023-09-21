package com.rgt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rgt.entity.UserActivityLogsEntity;

@Repository
public interface UserActivityLogsRepository extends JpaRepository<UserActivityLogsEntity, String>{

	List<UserActivityLogsEntity> findByNameOrderByLastLoginDesc(String emailId);

}

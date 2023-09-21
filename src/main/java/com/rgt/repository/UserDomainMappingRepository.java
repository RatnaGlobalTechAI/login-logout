package com.rgt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rgt.entity.UserDomainMappingEntity;

@Repository
public interface UserDomainMappingRepository extends JpaRepository<UserDomainMappingEntity, Integer>{

}

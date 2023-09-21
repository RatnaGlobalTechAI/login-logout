package com.rgt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rgt.entity.DomainEntity;

@Repository
public interface DomainRepository extends JpaRepository<DomainEntity, Integer> {

	DomainEntity findByName(String name);

}

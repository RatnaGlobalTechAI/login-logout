package com.rgt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rgt.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

	RoleEntity findByRole(String role);

	RoleEntity findByRoleId(Integer roleId);

}

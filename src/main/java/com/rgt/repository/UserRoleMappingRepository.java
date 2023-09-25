package com.rgt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rgt.entity.UserRoleMappingEntity;

@Repository
public interface UserRoleMappingRepository extends JpaRepository<UserRoleMappingEntity, Integer>{

	//UserRoleMappingEntity findByEmailIdAndDomainId(String emailId, Integer domainId);

	//@Query("select new com.rgt.entity.UserRoleMappingEntity(e.id , e.emailId, e.domainId ,e.roleId , e.createdOn , e.createdBy ,e.updatedOn , e.updatedBy)  from UserRoleMappingEntity e where e.emailId = ?1 and e.domainId = ?2 and e.roleId = ?3")
	//@Query(value = "select * from user_role_mapping where email_id = :emailId AND role_id =:roleId and DOMAIN_ID =:domainId", nativeQuery = true)
	//UserRoleMappingEntity findByUserMapping(@Param("emailId")String emailId, @Param("domainId")Integer domainId, @Param("roleId")Integer roleId);

	UserRoleMappingEntity findByEmailId(String emailId);

	UserRoleMappingEntity findByEmailIdAndDomainIdAndRoleId(String emailId, Integer domainId, Integer roleId);

	UserRoleMappingEntity findByEmailIdAndDomainId(String trim, Integer id);


}

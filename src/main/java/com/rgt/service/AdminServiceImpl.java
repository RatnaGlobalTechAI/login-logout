package com.rgt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rgt.entity.DomainEntity;
import com.rgt.entity.RoleEntity;
import com.rgt.entity.UserInfoEntity;
import com.rgt.entity.UserRoleMappingEntity;
import com.rgt.repository.DomainRepository;
import com.rgt.repository.RoleRepository;
import com.rgt.repository.UserInfoRepository;
import com.rgt.repository.UserRoleMappingRepository;
import com.rgt.request.AssignRoleRequest;
import com.rgt.request.RoleRequest;
import com.rgt.response.ResponseObject;
import com.rgt.utils.CommonUtility;
import com.rgt.utils.Constant;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;

	@Autowired
	private DomainRepository domainRepository;

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Override
	public ResponseObject createRole(RoleRequest roleRequest) {
		ResponseObject response = new ResponseObject();
		try {

			RoleEntity roleEntity = roleRepository.findByRole(roleRequest.getRole());
			if (roleEntity == null) {

				RoleEntity entity = new RoleEntity();
				entity.setRole(roleRequest.getRole().toUpperCase());
				roleRepository.save(entity);

				response.setStatus(true);
				response.setSuccessMessage("Role Created Successfully");

			} else {
				//
				response.setErrorMessage("Role already exist");
				response.setStatus(false);

			}

		} catch (Exception e) {
			response.setErrorMessage(e.getMessage());
			response.setStatus(false);
		}

		return response;
	}

	@Override
	@Transactional
	public ResponseObject assignRole(AssignRoleRequest assignRoleRequest) {
		ResponseObject response = new ResponseObject();

		try {
			DomainEntity domainEntity = domainRepository.findByName(assignRoleRequest.getDomain().trim());
			RoleEntity roleEntity = roleRepository.findByRole(assignRoleRequest.getRole().trim());
			UserRoleMappingEntity userRoleMappingEntity = userRoleMappingRepository.findByEmailIdAndDomainId(assignRoleRequest.getEmailId().trim(), domainEntity.getId());
					
			
			if (userRoleMappingEntity != null && roleEntity != null) {
				userRoleMappingEntity.setRoleId(roleEntity.getRoleId());
				userRoleMappingRepository.save(userRoleMappingEntity);
				response.setStatus(true);
				response.setSuccessMessage("User Role Mapping Updated to user");

			} else {
				
				if(assignRoleRequest.getRole().equalsIgnoreCase(roleEntity.getRole())) {
					response.setStatus(false);
					response.setSuccessMessage("Already Role Assigned");

				}else {
					//UserRoleMappingEntity entity = new UserRoleMappingEntity();
					userRoleMappingEntity.setRoleId(roleEntity.getRoleId());
					userRoleMappingEntity.setDomainId(domainEntity.getId());
					userRoleMappingEntity.setEmailId(assignRoleRequest.getEmailId());
					userRoleMappingEntity.setCreatedOn(new Date());
					userRoleMappingEntity.setCreatedBy(CommonUtility.getLoginUserName());
					userRoleMappingEntity.setUpdatedBy(CommonUtility.getLoginUserName());
					userRoleMappingEntity.setUpdatedOn(new Date());
					userRoleMappingRepository.save(userRoleMappingEntity);

					response.setStatus(true);
					response.setSuccessMessage("Role Assign to user");
				}

				

			}

		} catch (Exception e) {
			response.setErrorMessage(e.getMessage());
			response.setStatus(false);
		}

		return response;
	}

	@Transactional
	public UserInfoEntity getUserDetail(UserInfoEntity userInfoEntity) {
		UserInfoEntity entity = new UserInfoEntity();
		entity = userInfoRepository.findByEmailId(userInfoEntity.getEmailId().trim());
		
		UserRoleMappingEntity userRoleMappingEntity = userRoleMappingRepository.findByEmailId(entity.getEmailId());
		
		List<RoleEntity> userRoles = new ArrayList<RoleEntity>();
		if (null != entity) {
			//RoleEntity roleEntity = new RoleEntity();
			RoleEntity roleEntity = roleRepository.findByRoleId(userRoleMappingEntity.getRoleId());
			userRoles.add(roleEntity);
			//entity.setUserRoleList(userRoles);

		}

		return entity;
	}

}

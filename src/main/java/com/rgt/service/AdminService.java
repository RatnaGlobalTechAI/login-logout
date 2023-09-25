package com.rgt.service;

import com.rgt.entity.UserInfoEntity;
import com.rgt.request.AssignRoleRequest;
import com.rgt.request.RoleRequest;
import com.rgt.response.ResponseObject;

public interface AdminService {

	ResponseObject createRole(RoleRequest roleRequest);

	ResponseObject assignRole(AssignRoleRequest assignRoleRequest);

	UserInfoEntity getUserDetail(UserInfoEntity userInfoEntity);

}

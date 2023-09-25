package com.rgt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rgt.request.AssignRoleRequest;
import com.rgt.request.RoleRequest;
import com.rgt.response.ResponseObject;
import com.rgt.service.AdminService;

@RestController
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	private static Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@PostMapping(value = "/createRole")
	@PreAuthorize("hasAuthority('Admin')")
	public ResponseObject createRole(@RequestBody RoleRequest roleRequest) {
		ResponseObject response = new ResponseObject();
		response = adminService.createRole(roleRequest);
		return response;

	}
	
	@PostMapping(value = "/assignRole")
	@PreAuthorize("hasAuthority('Admin') ")
	public ResponseObject assignRole(@RequestBody AssignRoleRequest assignRoleRequest) {
		ResponseObject response = new ResponseObject();
		response = adminService.assignRole(assignRoleRequest);
		return response;

	}

}

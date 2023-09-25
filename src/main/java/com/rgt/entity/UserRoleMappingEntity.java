package com.rgt.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_role_mapping")
public class UserRoleMappingEntity {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
	
	@Column(name = "EMAIL_ID")
	private String emailId;

	@Column(name = "ROLE_ID")
	private Integer roleId;
	
	@Column(name = "DOMAIN_ID")
	private Integer domainId;
	
	@Column(name = "CREATED_ON")
	private Date createdOn;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "UPDATED_ON")
	private Date updatedOn;
	
	@Column(name = "UPDATED_BY")
	private String updatedBy;

	
	



	public UserRoleMappingEntity() {
	}

	public UserRoleMappingEntity(String id, String emailId, Integer roleId, Integer domainId, Date createdOn,
			String createdBy, Date updatedOn, String updatedBy) {
		this.id = id;
		this.emailId = emailId;
		this.roleId = roleId;
		this.domainId = domainId;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.updatedOn = updatedOn;
		this.updatedBy = updatedBy;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}

	

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
	
	
}

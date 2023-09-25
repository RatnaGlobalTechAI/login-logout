package com.rgt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "user_info")
public class UserInfoEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "EMAIL_ID")
	private String emailId;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "AGE")
	private String age;

	@Column(name = "GENDER")
	private String gender;

	@Column(name = "DOB")
	private String dob;

	@Column(name = "CITY")
	private String city;

	@Column(name = "PINCODE")
	private String pincode;

	@Column(name = "STATE")
	private String state;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "MOBILE_NUMBER")
	private String mobile;

	@Column(name = "QUALIFICATION")
	private String qualification;

	@Column(name = "JOB_TITLE")
	private String jobTitle;

	@Column(name = "MARITAL_STATUS")
	private String maritalStatus;

	@Column(name = "ADHARCARD_NUMBER")
	private String adharcard;

	@Column(name = "PANCARD_NUMBER")
	private String pancard;

	@Column(name = "CREATED_ON")
	private Date createdOn;

//	@JoinTable(name = "user_role_mapping", joinColumns = {
//			@JoinColumn(name = "EMAIL_ID", referencedColumnName = "EMAIL_ID") }, inverseJoinColumns = {
//					@JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID") })
	
	/*
	 * @JoinTable(name = "user_role_mapping", joinColumns = { @JoinColumn(name =
	 * "EMAIL_ID", referencedColumnName = "EMAIL_ID") }, inverseJoinColumns =
	 * { @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID") })
	 * 
	 * @ManyToMany private List<RoleEntity> userRoleList;
	 */
	
	
	/*
	 * @NotEmpty(message = "{user.role.NotEmpty}")
	 * 
	 * @Pattern(regexp="^(Admin)$" , message="{user.role.valid}") private String
	 * role;
	 */
	
	@Column(name = "LOGOUT_TIME")
	private Date logoutTime;
	
	
	
	

	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}


	/*
	 * @XmlTransient public List<RoleEntity> getUserRoleList() { return
	 * userRoleList; }
	 * 
	 * public void setUserRoleList(List<RoleEntity> userRoleList) {
	 * this.userRoleList = userRoleList; }
	 */

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getAdharcard() {
		return adharcard;
	}

	public void setAdharcard(String adharcard) {
		this.adharcard = adharcard;
	}

	public String getPancard() {
		return pancard;
	}

	public void setPancard(String pancard) {
		this.pancard = pancard;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}

package cn.tgxy.tgbase.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BsUserDto {
	private Long id;
	private String jobNo; // 工号
	private String username;
	private String _password;

	private String realName; // 真实姓名
	private Long departmentId; // 主岗部门

	private Integer gender; // 性别
	private String avatar; // 头像;
	private String email; // email
	private String mobilePhone; // 手机号码

	private Integer enable; // 是否启用
	private Integer status;
	
	private boolean uploadAvatar;//记录是否上传了头像，用于aop推送给微信

	private Set<String> permissions;

	private List<Long> departmentIdList; // 兼岗部门ID集

	private List<BsRoleDto> roleList; // 角色集

	private List<Long> roleIdList; // Role Id List
	
	private List<BsApplicationDto> appList;
	
	private List<Long> appIdList;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Integer getEnable() {
		return enable == null ? 0 : enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public boolean isUploadAvatar() {
		return uploadAvatar;
	}

	public void setUploadAvatar(boolean uploadAvatar) {
		this.uploadAvatar = uploadAvatar;
	}

	@JsonIgnore
	public String getPassword() {
		return _password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this._password = password;
	}

	public Set<String> getPermissions() {
		if (permissions == null) {
			return new HashSet<String>();
		}
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public List<Long> getDepartmentIdList() {
		return departmentIdList;
	}

	public void setDepartmentIdList(List<Long> departmentIdList) {
		this.departmentIdList = departmentIdList;
	}

	public List<BsRoleDto> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<BsRoleDto> roleList) {
		this.roleList = roleList;
	}

	public List<Long> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<Long> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public List<BsApplicationDto> getAppList() {
		return appList;
	}

	public void setAppList(List<BsApplicationDto> appList) {
		this.appList = appList;
	}

	public List<Long> getAppIdList() {
		return appIdList;
	}

	public void setAppIdList(List<Long> appIdList) {
		this.appIdList = appIdList;
	}

}

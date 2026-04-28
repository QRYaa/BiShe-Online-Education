package cn.tgxy.tgbase.po;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;


/**
 * @author chris deng
 * @Date 2023年12月10日 下午6:32:35
 */
@Entity
@Table(name = "bs_user")
@NamedQuery(name = "User.findByUsername", query = "from BsUser u where u.username = ?1")
public class BsUser extends BsAuditingPo implements Serializable {

    private static final long serialVersionUID = -9190732554741565019L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jobNo; //工号
    @Column(unique = true, length = 100) private String username;
    private String password;
    
    private String realName; //真实姓名
    
    private Long departmentId; //主岗部门
    
    private Integer gender; // 性别
    private String avatar; //头像;
    private String email; // email
    private String mobilePhone; //手机号码
   
    private Integer enable; //是否启用
    private Integer status;

    private Boolean isSuperAdmin; //是否超级管理员
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
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
		return enable;
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
	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}
	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	@Override
	public String toString() {
		return "AbtUser [id=" + id + ", jobNo=" + jobNo + ", username=" + username + ", password=" + password
				+ ", realName=" + realName + ", gender=" + gender + ", avatar=" + avatar + ", email=" + email
				+ ", mobilePhone=" + mobilePhone + ", enable=" + enable 
				+ ", isSuperAdmin=" + isSuperAdmin + "]";
	}
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
   

}

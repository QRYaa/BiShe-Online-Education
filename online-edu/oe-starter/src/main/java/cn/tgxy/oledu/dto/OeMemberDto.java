package cn.tgxy.oledu.dto;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.tgxy.tgbase.common.util.area.AreaUtils;

public class OeMemberDto {
	private Long id;

	private String name;

	private String code;

	private String avatar;
	
	private Integer type;

	private String tel;

	private Integer gender;

	private String weixinOpenId;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	private Date birthday;

	private String password;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	private Date registerDate;
	
	private Integer areaId;
	
	private String areaName;
	
	private Boolean realNameAuthenticated;//是否实名认证
	
	private String remark;

	private Integer enable;


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public Integer getGender() {
		return this.gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}
	
	public String getWeixinOpenId() {
		return this.weixinOpenId;
	}

	public void setWeixinOpenId(String weixinOpenId) {
		this.weixinOpenId = weixinOpenId;
	}
	
	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return AreaUtils.format(areaId);
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Boolean getRealNameAuthenticated() {
		return realNameAuthenticated;
	}

	public void setRealNameAuthenticated(Boolean realNameAuthenticated) {
		this.realNameAuthenticated = realNameAuthenticated;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Integer getEnable() {
		return this.enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	
}

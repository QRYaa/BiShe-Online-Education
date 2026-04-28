package cn.tgxy.tgbase.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "登录表单")
public class BsAuthDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private String cid;
	private String captchaCode;
	private Boolean sso;
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
	@Override
	public String toString() {
		return "BsAuthDto [username=" + username + ", password=" + password + "]";
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCaptchaCode() {
		return captchaCode;
	}
	public void setCaptchaCode(String captchaCode) {
		this.captchaCode = captchaCode;
	}
	public Boolean getSso() {
		return sso;
	}
	public void setSso(Boolean sso) {
		this.sso = sso;
	}

}

package cn.tgxy.oledu.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "绑定电话表单")
public class OeBindTelDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String tel;
	private String mobileSmsCode;
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getMobileSmsCode() {
		return mobileSmsCode;
	}
	public void setMobileSmsCode(String mobileSmsCode) {
		this.mobileSmsCode = mobileSmsCode;
	}
	
}

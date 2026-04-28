package cn.tgxy.oledu.dto;

public class OeMemberTokenDto {
	private String token;
	private boolean hasTel;
	
	public OeMemberTokenDto() {
		
	}
	
	public OeMemberTokenDto(String token,boolean hasTel) {
		this.token=token;
		this.hasTel=hasTel;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public boolean isHasTel() {
		return hasTel;
	}
	public void setHasTel(boolean hasTel) {
		this.hasTel = hasTel;
	}
	
}

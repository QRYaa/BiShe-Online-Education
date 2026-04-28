package cn.tgxy.tgbase.dto;

public class BsCaptchaDto {
	
	private Boolean captchaEnabled;
	
	private String cid;
	
	private String image;

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getCaptchaEnabled() {
		return captchaEnabled;
	}

	public void setCaptchaEnabled(Boolean captchaEnabled) {
		this.captchaEnabled = captchaEnabled;
	}

}

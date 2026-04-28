package cn.tgxy.oledu.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;

@Component
@ConfigurationProperties(prefix = "wx.mp")
public class WxMpProperties {
	private String appId;
	private String secret;
	private Boolean useStableAccessToken;
	private Boolean enable;
	public void config(WxMpDefaultConfigImpl wxMpDefaultConfigImpl) {
		wxMpDefaultConfigImpl.setAppId(appId);
		wxMpDefaultConfigImpl.setSecret(secret);
		wxMpDefaultConfigImpl.setUseStableAccessToken(useStableAccessToken);
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public Boolean getUseStableAccessToken() {
		return useStableAccessToken;
	}
	public void setUseStableAccessToken(Boolean useStableAccessToken) {
		this.useStableAccessToken = useStableAccessToken;
	}
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	
	
	
}

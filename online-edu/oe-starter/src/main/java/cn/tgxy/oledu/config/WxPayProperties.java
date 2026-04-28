package cn.tgxy.oledu.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;

@Component
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {
	private String appId;
	private String mchId;
	private String mchKey;
	private String notifyUrl;
	private String tradeType;
	private boolean useSandboxEnv;
	public void config(WxPayService wxPayService) {
		WxPayConfig wxPayConfig=new WxPayConfig();
		wxPayConfig.setAppId(appId);
		wxPayConfig.setMchId(mchId);
		wxPayConfig.setMchKey(mchKey);
		wxPayConfig.setNotifyUrl(notifyUrl);
		wxPayConfig.setTradeType(tradeType);
		wxPayConfig.setUseSandboxEnv(useSandboxEnv);
		wxPayService.setConfig(wxPayConfig);
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public String getMchKey() {
		return mchKey;
	}
	public void setMchKey(String mchKey) {
		this.mchKey = mchKey;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public boolean isUseSandboxEnv() {
		return useSandboxEnv;
	}
	public void setUseSandboxEnv(boolean useSandboxEnv) {
		this.useSandboxEnv = useSandboxEnv;
	}
}

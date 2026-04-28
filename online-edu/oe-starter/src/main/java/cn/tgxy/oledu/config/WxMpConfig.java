package cn.tgxy.oledu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.Resource;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;

@Configuration
public class WxMpConfig {
	@Resource
	private WxMpProperties wxMpProperties;

	@Bean
	WxMpDefaultConfigImpl wxMpDefaultConfigImpl() {
		WxMpDefaultConfigImpl wxMpDefaultConfigImpl = new WxMpDefaultConfigImpl();
		if(wxMpProperties.getEnable()) {
			wxMpProperties.config(wxMpDefaultConfigImpl);
		}
		else {
			wxMpDefaultConfigImpl.setAppId("xxx");
		}
		return wxMpDefaultConfigImpl;
	}
	@Bean
	WxMpService wxMpService(WxMpDefaultConfigImpl wxMpDefaultConfigImpl) {
		WxMpService wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(wxMpDefaultConfigImpl);
		return wxMpService;
	}
	@Bean
    WxMpMessageRouter wxMpMessageRouter(WxMpService wxMpService) {
		// TODO 配置消息路由应根据消息类型不同，分配不同的
        return new WxMpMessageRouter(wxMpService);
    }
}

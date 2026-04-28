package cn.tgxy.oledu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.binarywang.wxpay.service.WxPayService;

import jakarta.annotation.Resource;

@Configuration
public class WxPayConfig {

	@Resource
	private WxPayProperties wxPayProperties;
	
	@Bean
	WxPayService wxPayService() {
		WxPayService wxPayService=new WxPayServiceImpl2();
		wxPayProperties.config(wxPayService);
		return wxPayService;
	}
}

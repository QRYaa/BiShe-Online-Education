package cn.tgxy.oledu.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.tgxy.oledu.service.OeBannerService;
import cn.tgxy.oledu.service.OeOrderService;
import cn.tgxy.tgbase.task.BsTaskHandler;

@Configuration
public class TgOeTaskManager {
	
	@Autowired
	private OeBannerService bannerService;
	
	@Autowired
	private OeOrderService orderService;
	
	@Bean("updateBannerStatus")
    BsTaskHandler updateBannerStatus() {
		return new BsTaskHandler() {
			@Override
			public String execute(String param) throws Exception {
				StringBuilder result = new StringBuilder();
				bannerService.updateStatus(result);
				return result.toString();
			}
		};
	}
	
	@Bean("cancelOrder")
	BsTaskHandler cancelOrder() {
		return new BsTaskHandler() {
			
			@Override
			public String execute(String param) throws Exception {
				StringBuilder result=new StringBuilder();
				orderService.cancelOrder(result);
				return result.toString();
			}
		};
	}
}

package cn.tgxy.tgbase;

import java.util.Date;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;

public class WxTestMain {

	static WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
	static WxMpService wxMpService = new WxMpServiceImpl();
	static {
		config.setAppId("wx89c3b5046d972d60");
		config.setSecret("949c7bc4c992f20d132eb06420eec126");
		config.setUseStableAccessToken(true); // 使用stableAccessToken
	}

	public static void main(String[] args) throws Exception {

		wxMpService.setWxMpConfigStorage(config);
		config.autoRefreshToken();
//		wxMpService.getAccessToken();
		
		// 基础测试
		baseTest();
		
		// 用户测试
		userTest();

		// ------ 测试模板消息
		templateMsgTest();
	}
	
	static void templateMsgTest() throws Exception {
		System.out.println("测试模板消息");
		String testOpenId = "oSqjgt3kACQ-w7NdfXrrpNt_RefY";
		WxMpTemplateMessage tm = new WxMpTemplateMessage();
		tm.setTemplateId("kNykuQtSljZmsyZtyOoCsVRmCm0ZrKcQKRUWiiB0Qbw");
		tm.setToUser(testOpenId);
		String msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(tm);
		System.out.println("msgId:" + msgId);
		
	}
	
	static void userTest() throws Exception {

		WxMpUserList userList = wxMpService.getUserService().userList();
		if (userList != null) {
			userList.getOpenids().forEach(openId -> {
				System.out.println("-----" + openId + "-----");
				try {
					WxMpUser mpUser = wxMpService.getUserService().userInfo(openId);
					System.out.println(mpUser);

				} catch (WxErrorException e) {
					e.printStackTrace();
				}

			});
		}
	}

	static void baseTest() throws Exception {

		String accessToken = wxMpService.getAccessToken();

		long expiresTime = config.getExpiresTime();
		System.out.println(new Date(expiresTime));

		System.out.println("accessToken:" + accessToken);
	}

}

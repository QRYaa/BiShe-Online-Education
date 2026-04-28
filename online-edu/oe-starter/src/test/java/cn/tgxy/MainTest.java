package cn.tgxy;

import com.github.binarywang.wxpay.config.WxPayConfig;

import cn.tgxy.oledu.config.WxPayServiceImpl2;

public class MainTest {
	public static void main(String[] args) {
		try {
			String mchKey="WxzfApiV2Gztgxy20230919Tgdyc2024";
			WxPayConfig wxPayConfig = new WxPayConfig();
			wxPayConfig.setAppId("wxf6bfd22f0aba11b2");
			wxPayConfig.setMchId("1674857007");
			wxPayConfig.setNotifyUrl("https://qry.tgxy.com.cn/server/wxPayment/success");
			wxPayConfig.setTradeType("JSAPI");
			wxPayConfig.setUseSandboxEnv(true);
			wxPayConfig.setMchKey(mchKey);
			WxPayServiceImpl2 wxPayService = new WxPayServiceImpl2();
			wxPayService.setConfig(wxPayConfig);

			String str=wxPayService.getSandboxSignKey();
			System.out.println(str);
//			String xmlBody = "<xml>" + "  <mch_id><![CDATA[1674857007]]></mch_id>"
//					+ "  <nonce_str><![CDATA[1747100922951]]></nonce_str>"
//					+ "  <sign><![CDATA[11042E2457D49D53554E95A9B7A225C7]]></sign>" + "</xml>";
//			;
//			String str = wxPayService.post2("https://api.mch.weixin.qq.com/xdc/apiv2getsignkey/sign/getsignkey",
//					xmlBody, false);
//			System.out.println(str);
			
//			String toSign="mch_id=1707693491&nonce_str=aff8d001d43042678b74312ebb77a257&key=1707693491";
//			System.out.println(DigestUtils.md5Hex(toSign).toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

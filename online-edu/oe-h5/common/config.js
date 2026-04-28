module.exports = {
	authToken: 'OlEduMemAuth',
	//scope=snsapi_base
	//authorizeUrl: 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=###APPID###&redirect_uri=###REURI###&response_type=code&scope=snsapi_base&state=#wechat_redirect',
	//scope=snsapi_userinfo
	authorizeUrl: 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=###APPID###&redirect_uri=###REURI###&response_type=code&scope=snsapi_userinfo&state=#wechat_redirect',
	dev: {
		baseUrl: 'http://localhost:3004/server/',
		imagePrefix: 'http://localhost:28081/server/ximages?path=',
		videoPrefix: 'http://localhost:28081/server/xvideos?path=',
	},
	test: {
		// 外网域名测试
		// baseUrl: 'https://qry.tgxy.com.cn/server/',
		// loginUrl: '/fe/mp/login',
		// imagePrefix: 'https://qry.tgxy.com.cn/server/ximages?path=',
		// lessonVideoPrefix: 'https://qry.tgxy.com.cn/server/fe/mp/mcLessonVideo?lessonId=',
		// commonLessonVideoPrefix: 'https://qry.tgxy.com.cn/server/fe/mp/lessonVideo?lessonId=',
		// priImgPrefix:'http://192.168.10.60:3004/server/fe/mp/memberIdCardAuditIdCardImg?memberIdCardAuditId='
		// appid: 'wxf6bfd22f0aba11b2',
		// redirect_uri: 'https://qry.tgxy.com.cn/h5/pages/minePages/login',
		
		
		//内网测试
		baseUrl: 'http://192.168.10.60:3004/server/',
		loginUrl: '/fe/mp/loginTest',
		imagePrefix: 'http://192.168.10.60:3004/server/ximages?path=',
		lessonVideoPrefix: 'http://192.168.10.60:3004/server/fe/mp/mcLessonVideo?lessonId=',
		commonLessonVideoPrefix: 'http://192.168.10.60:3004/server/fe/mp/lessonVideo?lessonId=',
		priImgPrefix:'http://192.168.10.60:3004/server/fe/mp/memberIdCardAuditIdCardImg?memberIdCardAuditId='
		//OSS测试
		// oss: 'true',
		// baseUrl: 'http://192.168.10.60:3004/server/',
		// loginUrl: '/fe/mp/loginTest',
		// imagePrefix: 'https://test-pub.tgxy.com.cn/',
		// lessonVideoPrefix: 'https://test-online-video.tgxy.com.cn/',
		// commonLessonVideoPrefix: 'https://test-online-video.tgxy.com.cn/',
		// priImgPrefix: 'https://test-pri.tgxy.com.cn/',
	},
	test2:{
		baseUrl: 'https://qry.tgxy.com.cn/server/',
		loginUrl: '/fe/mp/loginTest',
		imagePrefix: 'https://qry.tgxy.com.cn/server/ximages?path=',
		lessonVideoPrefix: 'https://qry.tgxy.com.cn/server/fe/mp/mcLessonVideo?lessonId=',
		commonLessonVideoPrefix: 'https://qry.tgxy.com.cn/server/fe/mp/lessonVideo?lessonId=',
		priImgPrefix:'https://qry.tgxy.com.cn/server/fe/mp/memberIdCardAuditIdCardImg?memberIdCardAuditId=',
	},
	prod: {
		oss: 'true',
		baseUrl: 'https://online.tgxy.com.cn/server/',
		loginUrl: '/fe/mp/loginTest',
		imagePrefix: 'https://test-pub.tgxy.com.cn/',
		lessonVideoPrefix: 'https://test-online-video.tgxy.com.cn/',
		commonLessonVideoPrefix: 'https://test-online-video.tgxy.com.cn/',
		priImgPrefix: 'https://test-pri.tgxy.com.cn/',
	}
}
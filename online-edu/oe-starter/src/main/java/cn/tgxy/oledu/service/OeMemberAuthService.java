package cn.tgxy.oledu.service;

import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.dto.OeMemberTokenDto;

public interface OeMemberAuthService {

	/**
	 * 微信登录
	 * 根据code，向微信发送请求拿到返回的accessToken（Json数据）的openId
	 * @param 微信网页的URI的code参数值
	 * @return token值和是否绑定电话
	 * @throws
	 */
	OeMemberTokenDto memberLoginByWx(String code,boolean userInfo) throws Exception;

	OeMemberDto getCurrentMember();

	void memberLogout();

	void updateCurrentMember(OeMemberDto memberDto) throws Exception;

	void checkBindTel();

	OeMemberTokenDto memberLoginByWxTest(String code, boolean userInfo) throws Exception;

	OeMemberDto getCurrentMemberByToken();

	void updateSessionByMemberId(Long memberId) throws Exception;

}

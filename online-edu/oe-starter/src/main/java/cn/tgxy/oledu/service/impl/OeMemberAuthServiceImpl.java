package cn.tgxy.oledu.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tgxy.oledu.constant.OeConstant;
import cn.tgxy.oledu.constant.OeErrorConstant;
import cn.tgxy.oledu.dao.OeMemberAuthTokenDao;
import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.dto.OeMemberTokenDto;
import cn.tgxy.oledu.po.OeMemberAuthToken;
import cn.tgxy.oledu.service.OeMemberAuthService;
import cn.tgxy.oledu.service.OeMemberService;
import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.common.util.ServletUtils;
import cn.tgxy.tgbase.common.util.password.TokenGenerator;
import cn.tgxy.tgbase.constant.BsConstant;
import cn.tgxy.tgbase.service.RedisService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.service.WxOAuth2Service;
import me.chanjar.weixin.mp.api.WxMpService;

@Service
public class OeMemberAuthServiceImpl implements OeMemberAuthService {

	@Autowired
	private RedisService redisService;

	@Autowired
	private WxMpService wxMpService;

	private WxOAuth2Service wxOAuth2Service = null;

	@Autowired
	private OeMemberService memberService;

	@Autowired
	private OeMemberAuthTokenDao authTokenDao;

	@PostConstruct
	public void init() {
		wxOAuth2Service = wxMpService.getOAuth2Service();
	}

	@Override
	public OeMemberTokenDto memberLoginByWxTest(String code, boolean userInfo) throws Exception {
		OeMemberDto memberDto = memberService.get(1L);

		if (memberDto.getEnable().intValue() == BsConstant.INVALID) {
			throw new ServiceException(OeErrorConstant.MEMBER_STATUS_INVAL);// 学员被禁用
		}

		OeMemberAuthToken authToken = authTokenDao.findByMemberId(memberDto.getId());
		if (authToken == null)
			authToken = new OeMemberAuthToken();
		if (authToken.getExpireDate() == null || authToken.getExpireDate().getTime() >= System.currentTimeMillis()) { // 会话验证过期或者会话初始化
			authToken.setToken(memberDto.getId() + TokenGenerator.generateValue());
			authToken.setExpireDate(new Date(System.currentTimeMillis() + OeConstant.EXPIRE_TIME * 1000L));
			authToken.setMemberId(memberDto.getId());
			authTokenDao.save(authToken);
		}

		// 创建会话
		redisService.set(OeConstant.MEM_TOKEN_PREFIX + authToken.getToken(), memberDto, OeConstant.EXPIRE_TIME);

		return new OeMemberTokenDto(authToken.getToken(), StringUtils.isNotEmpty(memberDto.getTel()));
	}

	/**
	 * 微信登录 根据code，向微信发送请求拿到返回的accessToken（Json数据,包含openId）
	 * 
	 * @param 微信网页的URI的code参数值
	 * @return token值
	 * @throws
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public OeMemberTokenDto memberLoginByWx(String code, boolean userInfo) throws Exception {
		WxOAuth2AccessToken wxOAuth2AccessToken = wxOAuth2Service.getAccessToken(code);// 获得网页授权accessToken
		String openId = wxOAuth2AccessToken.getOpenId();
		OeMemberDto memberDto = memberService.findByWeixinOpenId(openId);
		if (memberDto == null || userInfo) {
			if (memberDto == null)
				memberDto = new OeMemberDto();
			memberDto.setName("默认用户0303");
			memberDto.setWeixinOpenId(openId);
			memberDto.setRegisterDate(new Date());
			memberDto.setEnable(BsConstant.VALID);
			if (userInfo) {
				WxOAuth2UserInfo wxOAuth2UserInfo = wxOAuth2Service.getUserInfo(wxOAuth2AccessToken, openId);
				memberDto.setName(wxOAuth2UserInfo.getNickname());
				// TODO: 思考scope为base或user_info不同的时机；还可以拿取wxOAuth2UserInfo的其他信息（仅服务号可调用）
			}
			memberService.save(memberDto);
		}

		if (memberDto.getEnable().intValue() == BsConstant.INVALID) {
			throw new ServiceException(OeErrorConstant.MEMBER_STATUS_INVAL);// 学员被禁用
		}

		OeMemberAuthToken authToken = authTokenDao.findByMemberId(memberDto.getId());
		if (authToken == null)
			authToken = new OeMemberAuthToken();
		if (authToken.getExpireDate() == null || authToken.getExpireDate().getTime() >= System.currentTimeMillis()) { // 会话验证过期或者会话初始化
			authToken.setToken(memberDto.getId() + TokenGenerator.generateValue());
			authToken.setExpireDate(new Date(System.currentTimeMillis() + OeConstant.EXPIRE_TIME * 1000L));
			authToken.setMemberId(memberDto.getId());
			authTokenDao.save(authToken);
		}

		// 创建会话
		redisService.set(OeConstant.MEM_TOKEN_PREFIX + authToken.getToken(), memberDto, OeConstant.EXPIRE_TIME);

		return new OeMemberTokenDto(authToken.getToken(), StringUtils.isNotEmpty(memberDto.getTel()));
	}

	@Override
	public OeMemberDto getCurrentMember() {
		String token = this.getToken();
		if (token == null) {
			throw new ServiceException(HttpStatus.UNAUTHORIZED.value());
		}
		OeMemberDto memberDto = (OeMemberDto) redisService.get(OeConstant.MEM_TOKEN_PREFIX + token);
		if (memberDto == null)
			throw new ServiceException(HttpStatus.UNAUTHORIZED.value());
		return memberDto;
	}

	@Override
	public OeMemberDto getCurrentMemberByToken() {
		String token = this.getTokenByParameterValues();
		if (token == null) {
			throw new ServiceException(HttpStatus.UNAUTHORIZED.value());
		}
		OeMemberDto memberDto = (OeMemberDto) redisService.get(OeConstant.MEM_TOKEN_PREFIX + token);
		if (memberDto == null)
			throw new ServiceException(HttpStatus.UNAUTHORIZED.value());
		return memberDto;
	}

	@Override
	public void memberLogout() {
		String token = this.getToken();
		if (token != null) {
			redisService.remove(OeConstant.MEM_TOKEN_PREFIX + token);
		}
	}

	@Override
	public void updateCurrentMember(OeMemberDto memberDto) throws Exception {

		String token = this.getToken();
		if (token != null) {
			redisService.set(OeConstant.MEM_TOKEN_PREFIX + token, memberDto, OeConstant.EXPIRE_TIME);
		}

	}

	@Override
	public void updateSessionByMemberId(Long memberId) throws Exception {
		OeMemberAuthToken authToken = authTokenDao.findByMemberId(memberId);
		if (authToken != null && authToken.getExpireDate() != null
				&& authToken.getExpireDate().getTime() <= System.currentTimeMillis()) {
			redisService.set(OeConstant.MEM_TOKEN_PREFIX + authToken.getToken(), memberService.get(memberId));
		}
	}

	@Override
	public void checkBindTel() {
		OeMemberDto memberDto = this.getCurrentMember();
		if (memberDto == null) { // 用户未登录
			throw new ServiceException(HttpStatus.UNAUTHORIZED.value());
		}
		// 更新过期时间
		String token = this.getToken();
		redisService.expire(OeConstant.MEM_TOKEN_PREFIX + token, OeConstant.EXPIRE_TIME);
		if (StringUtils.isEmpty(memberDto.getTel()))
			throw new ServiceException(OeErrorConstant.TEL_EMPTY);
	}

	private String getToken() {
		String token = null;
		HttpServletRequest request = ServletUtils.getRequest();
		if (request != null) {
			token = request.getHeader(OeConstant.TOKEN_HEADER);
		}
		return token;
	}

	private String getTokenByParameterValues() {
		String token = null;
		HttpServletRequest request = ServletUtils.getRequest();
		if (request != null) {
			token = request.getParameterValues("token") != null && request.getParameterValues("token").length >= 1
					? request.getParameterValues("token")[0]
					: null;
		}
		return token;
	}

}

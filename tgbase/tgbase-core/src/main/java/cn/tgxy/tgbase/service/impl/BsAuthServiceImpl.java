package cn.tgxy.tgbase.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.common.util.password.PasswordUtils;
import cn.tgxy.tgbase.constant.BsConstant;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import cn.tgxy.tgbase.constant.BsCoreErrorConstant;
import cn.tgxy.tgbase.dao.BsApplicationDao;
import cn.tgxy.tgbase.dao.BsPermissionDao;
import cn.tgxy.tgbase.dto.BsAuthDto;
import cn.tgxy.tgbase.dto.BsUserDto;
import cn.tgxy.tgbase.dto.response.BsAustRespDto;
import cn.tgxy.tgbase.po.BsApplication;
import cn.tgxy.tgbase.po.BsPermission;
import cn.tgxy.tgbase.service.BsAppSettingService;
import cn.tgxy.tgbase.service.BsAuthService;
import cn.tgxy.tgbase.service.BsUserService;
import cn.tgxy.tgbase.service.RedisService;
import jakarta.annotation.PostConstruct;

@Aspect
@Service
public class BsAuthServiceImpl implements BsAuthService {

	private static Logger log = LoggerFactory.getLogger(BsAuthServiceImpl.class);

	private Map<String, String> permissionMapping = new HashMap<String, String>();

	@Value("${system.base.ssoEnable}")
	private boolean ssoEnable = false;

	@Value("${system.base.mainSystem}")
	private boolean mainSystem = true;

	private Map<String, BsApplication> applicationMap = new HashMap<>();

	@Autowired
	private BsUserService userService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private BsPermissionDao permissionDao;

	@Autowired
	private BsApplicationDao applicationDao;

	@Autowired
	private BsAppSettingService appSettingService;

	@Value("${system.sso.appCode}")
	private String appCode;

	@Value("${system.sso.appSecret}")
	private String appSecret;

	@Value("${system.sso.checkTicketUrl}")
	private String checkTicketUrl;

	@Value("${system.sso.logoutUrl}")
	private String logoutUrl;

	@PostConstruct
	private void initPermissionMapping() throws Exception {
		List<BsPermission> list = permissionDao.findAll();
		for (BsPermission permission : list) {
			if (StringUtils.isNotEmpty(permission.getPaths())) {
				String[] pathArray = permission.getPaths().split(",");
				for (String path : pathArray) {
					permissionMapping.put(path.trim(), permission.getCode());
				}
			}
		}
		refreshApplicationMap();
//		log.debug(JSONUtils.toJSONString(permissionMapping));

	}

	@Override
	@AfterReturning("execution(* cn.tgxy.tgbase.dao.BsApplicationDao.save(..))"
			+ " || execution(* cn.tgxy.tgbase.dao.BsApplicationDao.update*(..))"
			+ " || execution(* cn.tgxy.tgbase.dao.BsApplicationDao.delete*(..))")
	public void refreshApplicationMap() throws Exception {
		if (ssoEnable && mainSystem) { // 只有启用了sso，并且是主系统，才需要加载applicaitonMap
			List<BsApplication> list = applicationDao.findAll();
			Map<String, BsApplication> map = new HashMap<>();
			for (BsApplication app : list) {
				map.put(app.getCode(), app);
			}
			this.applicationMap = map;
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BsAustRespDto login(BsAuthDto auth) throws Exception {
		BsUserDto userDto = userService.findByUsername(auth.getUsername());

		if (userDto == null) {
			throw new ServiceException(BsCoreErrorConstant.USER_LOGIN_ERROR);
		}
		if (userDto.getEnable().intValue() == BsConstant.INVALID) {
			throw new ServiceException(BsCoreErrorConstant.USER_STATUS_INVAL);
		}
		if (!PasswordUtils.matches(auth.getPassword(), userDto.getPassword())) {// 判断密码是否正确
			throw new ServiceException(BsCoreErrorConstant.USER_LOGIN_ERROR);
		}

		// 以上完成密码判断，以下为session实现
		StpUtil.login(userDto.getId());
		StpUtil.getSession().set(BsCoreConstant.SESSION_USER_KEY, userDto);
		String token = StpUtil.getTokenInfo().getTokenValue();
		// 设置DTO
		BsAustRespDto dto = new BsAustRespDto();
		dto.setToken(token);

		// 判断是否需要SSO
		if (BooleanUtil.isTrue(auth.getSso())) {
			String ticket = this.genTicketByUserId(userDto.getId());
			dto.setTicket(ticket);
			StpUtil.getSession().set(BsCoreConstant.SESSION_TICKET_KEY, ticket);
		}

		return dto;
	}

	@Override
	public BsAustRespDto loginByTicket(String ticket) throws Exception {
		if (StrUtil.isEmpty(ticket)) {
			throw new ServiceException(BsCoreErrorConstant.SSO_TICKET_INVALID);
		}
		Long userId = null;
		// http call login
//		HttpUtil.post(ticket, null)

		Map<String, Object> params = new HashMap<>();
		params.put("ticket", ticket);
		params.put("code", appCode);
		params.put("secret", appSecret);
		String respStr = HttpUtil.post(checkTicketUrl, params);
		JSONObject jsonObj = JSONUtil.parseObj(respStr);
		log.debug(jsonObj.toString());
		Integer respCode = jsonObj.getInt("code");
		if (HttpStatus.OK.value() == respCode.intValue()) {
			String respUserId = jsonObj.getStr("data");
			if (StrUtil.isEmpty(respUserId)) {
				throw new ServiceException(BsCoreErrorConstant.SSO_TICKET_INVALID);
			} else {
				userId = Long.parseLong(respUserId);
			}
		} else {
			throw new ServiceException(BsCoreErrorConstant.SSO_TICKET_INVALID);
		}

		BsAustRespDto dto = new BsAustRespDto();
		BsUserDto userDto = userService.get(userId);
		if (userDto != null) {
			StpUtil.login(userId);
			StpUtil.getSession().set(BsCoreConstant.SESSION_USER_KEY, userDto);
			String token = StpUtil.getTokenInfo().getTokenValue();
			dto.setToken(token);
		} else {
			throw new ServiceException(BsCoreErrorConstant.USER_LOGIN_ERROR);
		}

		return dto;
	}

	private String genTicketByUserId(Long userId) throws Exception {
		String ticket = IdUtil.randomUUID();
		redisService.set(BsCoreConstant.TICKET_PREFIX + ticket, userId, BsCoreConstant.TICKET_EXPIRE_TIME);
		return ticket;
	}

	@Override
	public void logout() throws Exception {
		if (ssoEnable) { // 启动了SSO,需要根据是否主系统进行判断
			if (mainSystem) {
				// 删除对应ticket
				Object ticketObj = StpUtil.getSession().get(BsCoreConstant.SESSION_TICKET_KEY);
				if (ticketObj != null) {
					redisService.remove(BsCoreConstant.TICKET_PREFIX + ticketObj.toString());
				}
				// 获得当前LoginId
				Long loginId = Long.valueOf(StpUtil.getLoginId().toString());
				// mainSystem logout
				this.simpleLogout();
				// 子系统logout
				applicationMap.forEach((key, app) -> {
					Integer appType = app.getType();
					if (appType!=null && appType.intValue() == BsCoreConstant.APP_TYPE_INTERNAL) { // 只有内部应用才做统一退出
						String appSsoLogoutUrl = appSettingService.getSetting(key,
								BsCoreConstant.APP_SETTING_SSO_LOGOUT_URL_KEY);
						if (appSsoLogoutUrl != null) {
							log.debug("Request sso logout:" + key + "[" + appSsoLogoutUrl + "]");
							Map<String, Object> params = new HashMap<>();
							params.put("loginId", loginId);
							params.put("code", app.getCode());
							params.put("secret", app.getSecret());
							HttpRequest.post(appSsoLogoutUrl).form(params).executeAsync();
						}
					}

				});

			} else {
				// 获得当前LoginId
				Long loginId = Long.valueOf(StpUtil.getLoginId().toString());
				// 告知主系统登出
				Map<String, Object> params = new HashMap<>();
				params.put("loginId", loginId);
				params.put("code", appCode);
				params.put("secret", appSecret);
				HttpRequest.post(logoutUrl).form(params).executeAsync();
				// 本机登出
				this.simpleLogout();
			}

		} else { // 未启动SSO，简单logout即可
			this.simpleLogout();
		}
	}
	

	@Override
	public void serverLogout(String appCode, Long loginId) throws Exception {

		if (ssoEnable && mainSystem) { // 启动了SSO,需要根据是否主系统进行判断
				// 删除对应ticket
				Object ticketObj = StpUtil.getSessionByLoginId(loginId).get(BsCoreConstant.SESSION_TICKET_KEY);
				if (ticketObj != null) {
					redisService.remove(BsCoreConstant.TICKET_PREFIX + ticketObj.toString());
				}
				// mainSystem logout
				StpUtil.logout(loginId);
				// 子系统logout
				applicationMap.forEach((key, app) -> {
					Integer appType = app.getType();
					if (appType.intValue() == BsCoreConstant.APP_TYPE_INTERNAL && !StrUtil.equals(appCode, key)) { // 只有内部应用才做统一退出
						String appSsoLogoutUrl = appSettingService.getSetting(key,
								BsCoreConstant.APP_SETTING_SSO_LOGOUT_URL_KEY);
						if (appSsoLogoutUrl != null) {
							log.debug("Request sso logout:" + key + "[" + appSsoLogoutUrl + "]");
							Map<String, Object> params = new HashMap<>();
							params.put("loginId", loginId);
							params.put("code", app.getCode());
							params.put("secret", app.getSecret());
							HttpRequest.post(appSsoLogoutUrl).form(params).executeAsync();
						}
					}

				});


		}
	}

	public void simpleLogout() throws Exception {
		StpUtil.logout();
	}

	public void simpleLogout(Long loginId) throws Exception {
		StpUtil.logout(loginId);
	}

	@Override
	public BsUserDto getCurrentUser() throws Exception {
		if (!StpUtil.isLogin()) {
			// 未登录
			throw new ServiceException(HttpStatus.UNAUTHORIZED.value());
		}
		BsUserDto userDto = (BsUserDto) StpUtil.getSession().get(BsCoreConstant.SESSION_USER_KEY);
		return userDto;
	}

	@Override
	public Long getLoginIdByTicket(String ticket) throws Exception {
		Long loginId = null;
		Object loginIdObj = redisService.get(BsCoreConstant.TICKET_PREFIX + ticket);
		if (loginIdObj == null) {
			throw new ServiceException(BsCoreErrorConstant.SSO_TICKET_INVALID);
		} else {
			loginId = Long.parseLong(loginIdObj.toString());
		}
		return loginId;
	}

	@Override
	public String getCurrentUserTicket() throws Exception {
		BsUserDto userDto = this.getCurrentUser();
		String ticket = StrUtil.toStringOrNull(StpUtil.getSession().get(BsCoreConstant.SESSION_TICKET_KEY));
		if (ticket == null) { // 如无ticket，重新生成ticket
			ticket = this.genTicketByUserId(userDto.getId());
		} else { // 如有ticket，则判断ticket是否过期，如过期则重新生成，如未过期则刷新时间
			Object userId = redisService.get(BsCoreConstant.TICKET_PREFIX + ticket);
			if (userId == null) {
				ticket = this.genTicketByUserId(userDto.getId());
				StpUtil.getSession().set(BsCoreConstant.SESSION_TICKET_KEY, ticket);
			} else {
				redisService.expire(BsCoreConstant.TICKET_PREFIX + ticket, BsCoreConstant.TICKET_EXPIRE_TIME);
			}
		}
		return ticket;
	}

	@Override
	public void updateCurrentUser(BsUserDto userDto) throws Exception {
		StpUtil.getSession().set(BsCoreConstant.SESSION_USER_KEY, userDto);
	}

	@Override
	public void checkPermission(String requestURL) throws Exception {
		BsUserDto userDto = this.getCurrentUser();
		if (userDto == null) { // 用户未登录
			throw new ServiceException(HttpStatus.UNAUTHORIZED.value());
		}

		// 更新过期时间
		StpUtil.updateLastActiveToNow();

		// TODO 处理超级管理员
//		if(userDto.getIsSuperAdmin()) {
//			return;
//		}

		// 处理权限校验
		Set<String> pathSet = permissionMapping.keySet();
		Set<String> userPermissionCodes = userDto.getPermissions();

		for (String path : pathSet) {
			if (StringUtils.isNotBlank(path) && requestURL.startsWith(path)) {
				String code = permissionMapping.get(path);
				if (!userPermissionCodes.contains(code)) {
					throw new ServiceException(BsCoreErrorConstant.PERMISSION_DENIED_ERROR);
				}
			}
		}
	}

	@Override
	public boolean checkCurrentSystemSecret(String appCode, String appSecret) {
		if (appCode == null || appSecret == null) {
			return false;
		} else {
			if (appCode.equals(this.appCode) && appSecret.equals(this.appSecret)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void checkToken(String token) throws Exception {
		if(StpUtil.isLogin()) return;
		if(StpUtil.getLoginIdByToken(token)==null) throw new ServiceException(BsCoreErrorConstant.TOKEN_ERROR);
	}


//	@Override
//	public void ssoLogoutForServer(Long loginId) {
//		if(loginId==null) {
//			loginId=Long.valueOf(StpUtil.getLoginId().toString());
//		}
//		//主系统注销
//		logoutByLoginId(loginId);
//		//子系统注销
//		for(String appCode:appCodes) {
//			BsApplication app=appDao.findByCode(appCode);
//			if(app==null) {
//				continue;
//			}
//			BsAppSetting appSetting=appSettingDao.findByApplicationIdAndCode(app.getId(),"callbackUrl");
//			if(appSetting==null) {
//				continue;
//			}
//			log.debug("子系统注销："+appSetting.getContent());
//			Map<String, Object> params = new HashMap<>();
//			params.put("code", appCode);
//			params.put("secret", appSecret);
//			params.put("loginId", loginId);
//			HttpUtil.post(appSetting.getContent(),params);
//		}
//	}
//	@Override
//	public void ssoLogoutForClient() {
//		//执行主系统注销方法
//		Map<String, Object> params = new HashMap<>();
//		params.put("code", appCode);
//		params.put("secret", appSecret);
//		params.put("loginId", Long.valueOf(StpUtil.getLoginId().toString()));
//		String respStr = HttpUtil.post(logoutUrl, params);
//		JSONObject jsonObj = JSONUtil.parseObj(respStr);
//		log.debug(jsonObj.toString());
//	}
//	@Override
//	public void logoutByLoginId(Long loginId) {
//		Object ticketObj = StpUtil.getSessionByLoginId(loginId).get(BsCoreConstant.SESSION_TICKET_KEY);
//		String token=StpUtil.getTokenValueByLoginId(loginId);
//		if (ticketObj != null) {
//			redisService.remove(BsCoreConstant.TICKET_PREFIX + ticketObj.toString());
//		}
//		StpUtil.logoutByTokenValue(token);
//	}
//	@Override
//	public void ssoLogoutInServer() {
//		String token=StpUtil.getTokenInfo().getTokenValue();
//		if(StringUtils.isNotEmpty(token)){
//			
//			//子系统注销
//			for(String appCode:appCodes) {
//				BsApplication app=appDao.findByCode(appCode);
//				if(app==null) {
//					continue;
//				}
//				BsAppSetting appSetting=appSettingDao.findByApplicationIdAndCode(app.getId(),"callbackUrl");
//				if(appSetting==null) {
//					continue;
//				}
//				log.debug("子系统注销："+appSetting.getContent());
//				Map<String, Object> params = new HashMap<>();
//				params.put("code", appCode);
//				params.put("secret", appSecret);
//				params.put("loginId", StpUtil.getLoginId());
//				HttpUtil.post(appSetting.getContent(),params);
//			}
//			//主系统注销
//			StpUtil.logout();
//		}
//		else {
//			return;
//		}
//	}

}

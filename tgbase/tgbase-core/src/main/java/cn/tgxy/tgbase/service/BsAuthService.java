package cn.tgxy.tgbase.service;

import cn.tgxy.tgbase.dto.BsAuthDto;
import cn.tgxy.tgbase.dto.BsUserDto;
import cn.tgxy.tgbase.dto.response.BsAustRespDto;

/**
 * 授权登录
 * 
 * @author chris deng
 * @Date 2024/02/07 14:41:54
 */
public interface BsAuthService {
	/**
	 * 验证授权登录
	 * 
	 * @param auth
	 * @return
	 * @throws Exception
	 */
	public BsAustRespDto login(BsAuthDto auth) throws Exception;

	/**
	 * 注销登录
	 * 
	 * @throws Exception
	 */
	public void logout() throws Exception;
	
	/**
	 * 简单注销登录，不判断是否SSO
	 * @throws Exception
	 */
	public void simpleLogout() throws Exception;
	
	/**
	 * 简单注销登录，不判断是否SSO
	 * @param loginId
	 * @throws Exception
	 */
	public void simpleLogout(Long loginId) throws Exception;

	/**
	 * 获取当前登录用户
	 * 
	 * @return
	 * @throws Exception
	 */
	public BsUserDto getCurrentUser() throws Exception;

	/**
	 * 更改当前登录用户信息
	 * 
	 * @param user
	 * @throws Exception
	 */
	public void updateCurrentUser(BsUserDto userDto) throws Exception;

	/**
	 * 通过token判断用户对访问路径的权限
	 * 
	 * @param requestURL
	 * @throws Exception
	 */
	public void checkPermission(String requestURL) throws Exception;


	// ---------------------------------------------------------
	// 						以下为SSO相关
	// ---------------------------------------------------------


	/**
	 * 【Client】sso ticket验证，用ticket向主系统换LoginId，并在子系统进行登录
	 * 
	 * @param ticket
	 * @return
	 * @throws Exception
	 */
	public BsAustRespDto loginByTicket(String ticket) throws Exception;
	
	/**
	 * 【Client】检查code/secret是否和配置的一致
	 * @param appCode
	 * @param appSecret
	 * @return
	 */
	public boolean checkCurrentSystemSecret(String appCode, String appSecret);

	/**
	 * 【Server】获得or生成 -> 当前登录用户的ticket
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getCurrentUserTicket() throws Exception;

	/**
	 * 【Server】根据ticket换LoginId，client会用ticket换loginId
	 * 
	 * @param ticket
	 * @return
	 * @throws Exception
	 */
	public Long getLoginIdByTicket(String ticket) throws Exception;
	
	/**
	 * 【Server】applicaiton更新时调用，用于更新aApplicationMap，由于application更新几率不大，每次全量更新
	 * @throws Exception
	 */
	public void refreshApplicationMap() throws Exception;
	
	/**
	 * 【Server】被客户端调用，除appCode外，其他均调用logout
	 * @param appCode
	 * @param loginId
	 * @throws Exception
	 */
	public void serverLogout(String appCode, Long loginId) throws Exception;

	public void checkToken(String token) throws Exception;

}

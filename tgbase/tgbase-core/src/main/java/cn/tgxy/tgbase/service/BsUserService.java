package cn.tgxy.tgbase.service;

import java.util.List;

import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dto.BsUserDto;
import cn.tgxy.tgbase.po.BsUser;

public interface BsUserService extends BsBaseService<BsUser, BsUserDto> {
	/**
	 * 管理员修改用户密码
	 * @param userId
	 * @param newPassword
	 * @throws Exception
	 */
	public void changePassword(Long userId, String newPassword) throws Exception;
	
	public void changeMyPassword(BsUserDto currentUser, String oldPassword, String newPassword) throws Exception;
	
	public List<BsUserDto> search(String keyword) throws Exception;
	
	/**
	 * 根据appId查找用户
	 * @param appCode
	 * @return
	 * @throws Exception
	 */
	public List<BsUserDto> findUserByAppId(Long appId) throws Exception;
	
	public BsUserDto findByUsername(String username) throws Exception;
	
	public PageContent<BsUserDto> findPageData(String username, String realName, Long departmentId, Integer gender,
			String email, String mobilePhone, Integer enable, Long roleId, Long appId,int pageNum, int pageSize) throws Exception;
	
	public void assignUserRole(BsUserDto userDto) throws Exception;

	void addApp(Long userId, List<Long> appIdList) throws Exception;

	void deleteApp(Long userId, Long appIdList) throws Exception;

}

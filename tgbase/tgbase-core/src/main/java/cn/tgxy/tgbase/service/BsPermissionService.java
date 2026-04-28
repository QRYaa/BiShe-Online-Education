package cn.tgxy.tgbase.service;

import java.util.List;

import cn.tgxy.tgbase.dto.BsPermissionDto;
import cn.tgxy.tgbase.po.BsPermission;

public interface BsPermissionService extends BsBaseService<BsPermission, BsPermissionDto> {
	/**
	 * 检查某权限是否存在子节点
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean checkExistChildById(Long id) throws Exception;
	
	/**
	 * 根据UserId列出用户权限列表
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<BsPermission> listByUserId(Long userId) throws Exception;
	
}

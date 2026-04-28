package cn.tgxy.tgbase.dao;

import cn.tgxy.tgbase.po.BsRolePermissionMap;

public interface BsRolePermissionMapDao extends BsBaseDao<BsRolePermissionMap, Long>{
	
	void deleteByRoleId(Long roleId);

}

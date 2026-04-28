package cn.tgxy.tgbase.dao;

import cn.tgxy.tgbase.po.BsUserRoleMap;

public interface BsUserRoleMapDao extends BsBaseDao<BsUserRoleMap, Long>{
	

	void deleteByUserIdAndRoleId(Long userId, Long roleId);

}

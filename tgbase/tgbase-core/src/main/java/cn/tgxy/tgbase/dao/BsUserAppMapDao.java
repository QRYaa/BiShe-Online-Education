package cn.tgxy.tgbase.dao;

import cn.tgxy.tgbase.po.BsUserAppMap;

public interface BsUserAppMapDao extends BsBaseDao<BsUserAppMap, Long>{

	void deleteByUserIdAndAppId(Long id, Long appId);

	boolean existsByUserIdAndAppId(Long deleteId, Long appId);

}

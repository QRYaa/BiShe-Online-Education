package cn.tgxy.tgbase.dao;

import java.util.List;

import cn.tgxy.tgbase.po.BsUserDepartmentMap;

public interface BsUserDepartmentMapDao extends BsBaseDao<BsUserDepartmentMap, Long> {
	void deleteByUserId(Long userId);

	void deleteByUserIdAndMajor(Long userId, Boolean major);

	void deleteByUserIdAndDepartmentIdAndMajor(Long userId, Long departmentId, Boolean major);

	BsUserDepartmentMap findByUserIdAndDepartmentId(Long userId, Long departmentId);

	List<BsUserDepartmentMap> findByUserId(Long userId);
	
	List<BsUserDepartmentMap> findByUserIdAndMajor(Long userId, Boolean major);

	boolean existsByDepartmentId(Long departmentId);
}

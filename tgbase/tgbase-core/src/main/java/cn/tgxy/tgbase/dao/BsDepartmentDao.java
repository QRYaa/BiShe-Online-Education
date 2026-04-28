package cn.tgxy.tgbase.dao;

import java.util.List;

import cn.tgxy.tgbase.po.BsDepartment;

public interface BsDepartmentDao extends BsBaseDao<BsDepartment, Long> {
	List<BsDepartment> findByParentId(Long pid);

	boolean existsByParentId(Long pid);
}

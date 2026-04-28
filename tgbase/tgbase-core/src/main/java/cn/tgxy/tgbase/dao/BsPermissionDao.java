package cn.tgxy.tgbase.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.tgxy.tgbase.po.BsPermission;

public interface BsPermissionDao extends BsBaseDao<BsPermission, Long>{
	boolean existsByParentId(Long pid);
	boolean existsByCode(String code);
	
	@Query("SELECT p "
			+ "FROM BsPermission p, BsRolePermissionMap rp, BsRole r, BsUserRoleMap ur "
			+ "WHERE p.id=rp.permissionId AND r.id=rp.roleId AND r.id=ur.roleId AND r.enable = 1 "
			+ "AND ur.userId= :userId")
	List<BsPermission> findByUserId(@Param("userId") Long userId);
}

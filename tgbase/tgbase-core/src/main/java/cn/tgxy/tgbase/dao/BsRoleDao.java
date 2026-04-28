package cn.tgxy.tgbase.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.tgxy.tgbase.po.BsRole;

public interface BsRoleDao extends BsBaseDao<BsRole, Long> {
	
	boolean existsByCode(String code);

	@Query("select m.permissionId from BsRolePermissionMap m where m.roleId = :roleId")
	List<Long> findRolePermission(@Param("roleId") Long roleId);
	
	@Query("SELECT r FROM BsRole r INNER JOIN BsUserRoleMap m ON r.id = m.roleId where m.userId = :userId")
	List<BsRole> findByUserId(@Param("userId") Long userId);

}

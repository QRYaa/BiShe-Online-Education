package cn.tgxy.tgbase.dao;

import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.tgxy.tgbase.po.BsUser;
import jakarta.transaction.Transactional;

public interface BsUserDao extends BsBaseDao<BsUser, Long> {
	BsUser findByUsername(String username);
	
	boolean existsByUsername(String username);
	
	boolean existsByJobNo(String jobNo);
	
	boolean existsByMobilePhone(String mobilePhone);

	boolean existsByEmail(String email);
	
	@Query("select u from BsUser u where u.realName like %:realName%")
	List<BsUser> findByRealNameLike(@Param("realName") String realName, Limit maxResults);
	
	@Modifying
	@Transactional
	@Query("update BsUser u set u.password = :password where u.id = :userId")
	void changeUserPassword(@Param("userId") Long userId, @Param("password") String password);

	@Query("SELECT u FROM BsUser u INNER JOIN BsUserAppMap m ON u.id = m.userId WHERE m.appId = :appId")
	List<BsUser> findByAppId(Long appId);
	
	@Query("SELECT u FROM BsUser u INNER JOIN BsUserAppMap m ON u.id = m.userId WHERE m.appId = :appId AND u.id IN :ids")
	List<BsUser> findByIdsAndAppId(List<Long> ids,Long appId);
	
}

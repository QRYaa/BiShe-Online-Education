package cn.tgxy.tgbase.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.tgxy.tgbase.po.BsApplication;
import jakarta.transaction.Transactional;

public interface BsApplicationDao extends BsBaseDao<BsApplication, Long>{

	boolean existsByCodeAndSecret(String code, String secret);

	@Modifying
	@Transactional
	@Query("UPDATE BsApplication a SET a.secret = :secret WHERE a.id = :id")
	void updateSecretById(@Param("id")Long id, @Param("secret")String secret);

	boolean existsByCode(String code);

	BsApplication findByCode(String code);

	@Query("SELECT a FROM BsApplication a INNER JOIN BsUserAppMap m ON a.id = m.appId WHERE m.userId = :userId")
	List<BsApplication> findByUserId(@Param("userId")Long userId);

}

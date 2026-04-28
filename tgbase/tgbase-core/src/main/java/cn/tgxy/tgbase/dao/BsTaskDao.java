package cn.tgxy.tgbase.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.tgxy.tgbase.po.BsTask;
import jakarta.transaction.Transactional;

public interface BsTaskDao extends BsBaseDao<BsTask, Long>{

	boolean existsByHandlerName(String handlerName);

	void deleteById(Long taskId);
	
	@Modifying
	@Transactional
	@Query("UPDATE BsTask t SET t.lastTime = :lastTime WHERE t.id = :id")
	void updateLastTime(@Param("lastTime")Date lastTime,@Param("id")Long id);
	
	@Modifying
	@Transactional
	@Query("UPDATE BsTask t SET t.lastResult = :lastResult WHERE t.id = :id")
	void updateLastResult(@Param("lastResult")Integer lastResult,@Param("id")Long id);
	
	@Modifying
	@Transactional
	@Query("UPDATE BsTask t SET t.status = :status WHERE t.id = :id")
	void updateStatus(@Param("status")Integer status,@Param("id")Long id);

}

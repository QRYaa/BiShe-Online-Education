package cn.tgxy.oledu.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.tgxy.oledu.po.OeCourse;
import cn.tgxy.tgbase.dao.BsBaseDao;

public interface OeCourseDao extends BsBaseDao<OeCourse, Long>{

	@Modifying
	@Query("update OeCourse c set c.status = :status where c.id = :id")
	void updateStatusById(@Param("id") Long id, @Param("status") Integer status);
	
	@Modifying
	@Query("update OeCourse c set c.paid = :paid where c.id = :id")
	void updatePaidById(@Param("id") Long id, @Param("paid") Boolean paid);
	
	@Modifying
	@Query("update OeCourse c set c.published = :published where c.id = :id")
	void updatePublishedById(@Param("id") Long id, @Param("published") Boolean published);
	
	OeCourse findByCodeAndPublished(String code, boolean b);

	OeCourse findByIdAndPublished(Long id, boolean b);

	boolean existsByCodeAndPublished(String code, boolean b);

	boolean existsByIdAndPublished(Long id, boolean b);

	@Query("SELECT c.id FROM OeCourse c WHERE c.code = :code")
	Long getIdByCode(String code);
	
	@Query("SELECT c.name FROM OeCourse c WHERE c.id = :id")
	String getNameById(Long id);
		
}

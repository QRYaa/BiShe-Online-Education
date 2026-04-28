package cn.tgxy.oledu.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.tgxy.oledu.po.OeBanner;
import cn.tgxy.tgbase.dao.BsBaseDao;
import jakarta.transaction.Transactional;

public interface OeBannerDao extends BsBaseDao<OeBanner, Long>{

	@Modifying
	@Transactional
	@Query("UPDATE OeBanner b SET b.enable = :i WHERE b.id = :id")
	void updateEnableById(Integer i,Long id);
	
	@Modifying
	@Transactional
	@Query("UPDATE OeBanner b SET b.status = :i WHERE b.id = :id")
	void updateStatusById(Integer i, Long id);

	List<OeBanner> findByEnableAndStatus(Integer enable,Integer valid);

	List<OeBanner> findByStatus(Integer i);

}

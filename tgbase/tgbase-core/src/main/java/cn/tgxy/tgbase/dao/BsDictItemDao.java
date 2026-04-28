package cn.tgxy.tgbase.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.tgxy.tgbase.po.BsDictItem;

public interface BsDictItemDao extends BsBaseDao<BsDictItem, Long>{
	
	boolean existsByDictIdAndItemKey(Long dictId, String itemKey);

	@Query("select o from BsDictItem o where o.dictId = :dictId")
	List<BsDictItem> findByDictIdAndSort(@Param("dictId") Long dictId, @Param("sort")  Sort sort);
	
	void deleteByDictId(Long dictId);

}

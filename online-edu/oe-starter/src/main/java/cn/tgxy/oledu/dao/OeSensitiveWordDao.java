package cn.tgxy.oledu.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import cn.tgxy.oledu.po.OeSensitiveWord;
import cn.tgxy.tgbase.dao.BsBaseDao;

public interface OeSensitiveWordDao extends BsBaseDao<OeSensitiveWord, Long>{

	@Query("SELECT sw.sensitiveWord FROM OeSensitiveWord sw")
	List<String> findAllWords();
	
}

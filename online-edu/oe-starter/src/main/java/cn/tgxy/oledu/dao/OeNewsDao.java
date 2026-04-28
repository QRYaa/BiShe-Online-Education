package cn.tgxy.oledu.dao;

import cn.tgxy.tgbase.dao.BsBaseDao;
import cn.tgxy.oledu.po.OeNews;

public interface OeNewsDao extends BsBaseDao<OeNews, Long>{

	boolean existsByCode(String code);

	OeNews findByCodeAndPublished(String code, boolean b);
}

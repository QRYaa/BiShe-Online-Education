package cn.tgxy.tgbase.dao;

import java.util.Map;

import cn.tgxy.tgbase.common.page.PageContent;

/**
 * @author Chris Deng
 * @Date 2024/04/08 10:44:56
 * 复杂的跨表查询，使用BsExtBaseDao
 */
public interface BsExtBaseDao {
	
	public long count(String hql, Map<String, Object> paramMap);

	public <T> PageContent<T> findPageContent(Class<T> c, String hql, int pageNum, int pageSize,
			Map<String, Object> paramMap) throws Exception;

}

package cn.tgxy.tgbase.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.hutool.core.lang.Assert;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.util.SqlRemoveUtils;
import cn.tgxy.tgbase.dao.BsExtBaseDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

public class BsExtBaseDaoImpl implements BsExtBaseDao{
	
	@PersistenceContext 
	private EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public <T> PageContent<T> findPageContent(Class<T> c, String hql, int pageNum, int pageSize,
			Map<String, Object> paramMap) throws Exception {
		Assert.isTrue(pageSize>0, "PageSize > 0 must be true");
		final String countHql = "SELECT count(*) "
				+ SqlRemoveUtils
						.removeOrders(SqlRemoveUtils
								.removeSelect(SqlRemoveUtils
										.removeFetchKeyword((hql))));
		long totalItems = this.count(countHql, paramMap);
		PageContent<T> pageContent = new PageContent<>(totalItems, pageNum, pageSize);
		if (totalItems > 0) {
			List<T> list = this.list(hql, pageContent.getOffset(), pageSize,
					paramMap);
			pageContent.addAll(list);
		}
		return pageContent;
		
	}
	
	@Override
	public long count(String hql, Map<String, Object> paramMap) {
		Query query = getEntityManager().createQuery(hql);
		setParameters(query, paramMap);
		Long total = (Long)query.getSingleResult();
		return total.longValue();
	}
	
	protected <T> List<T> list(final String hql, final int offset,
			final int limit, final Map<String, Object> paramMap) {
		Query query = getEntityManager().createQuery(hql);
		setParameters(query, paramMap);
		query.setMaxResults(limit);
		query.setFirstResult(offset);
		return query.getResultList();
	}
	protected void setParameters(Query query, Map<String, Object> paramMap) {
		if (paramMap != null) {
			Set<String> keySet = paramMap.keySet();
			for (String key : keySet) {
				Object value = paramMap.get(key);
				query.setParameter(key, value);
			}
		}
	}

}

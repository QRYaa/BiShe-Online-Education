package cn.tgxy.tgbase.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import cn.tgxy.tgbase.common.page.IdIndexPageContent;
import cn.tgxy.tgbase.common.page.PageContent;

public interface BsBaseService<T, D> {

	/**
	 * 标准分页
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageContent<D> findPageData(int pageNum, int pageSize) throws Exception;

	/**
	 * 标准分页，增加搜索条件
	 * @param spec
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public PageContent<D> findPageData(Specification<T> spec, Pageable pageable) throws Exception;
	
	/**
	 * Controller需要处理nextId/direction参数
	 * @param spec
	 * @param direction
	 * @param sizeLimit
	 * @return
	 * @throws Exception
	 */
	public IdIndexPageContent<D> findIdIndexPageData(Specification<T> spec, Direction direction, Integer sizeLimit) throws Exception;

	public List<D> find(Specification<T> spec) throws Exception;

	public List<D> find(Sort sort) throws Exception;
	
	public List<D> find(Specification<T> spec, Sort sort) throws Exception;

	public List<D> findAll() throws Exception;

	public long countAll() throws Exception;

	public D get(Long id) throws Exception;

	public void deleteById(Long id) throws Exception;

	public void deleteByIdInBatch(List<Long> ids) throws Exception;

	public void save(D dto) throws Exception;

	public void update(D dto) throws Exception;

	/**
	 * 常亮集
	 */
	public final static String PAGE_NUM = "pageNo"; // 页码
	public final static String PAGE_LIMIT = "pageSize"; // 单页数量
	public static final String DEL_FLAG = "delFlag"; // 删除标识，0:正常; 1:已删除

}

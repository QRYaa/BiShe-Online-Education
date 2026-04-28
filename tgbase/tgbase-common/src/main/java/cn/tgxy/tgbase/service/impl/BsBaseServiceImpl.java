package cn.tgxy.tgbase.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import cn.tgxy.tgbase.common.page.IdIndexPageContent;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.page.PageRequestConverter;
import cn.tgxy.tgbase.dao.BsBaseDao;
import cn.tgxy.tgbase.service.BsBaseService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public abstract class BsBaseServiceImpl<T, D> implements BsBaseService<T, D> {
	private static Logger log = LoggerFactory.getLogger(BsBaseServiceImpl.class);

	protected BsBaseDao<T, Long> baseDao;

	
	// ---- 标准分页 start ----
	@Override
	public PageContent<D> findPageData(int pageNum, int pageSize) throws Exception {
		// org.springframework.data.domain.Page.Page的0为第一页，需要把系统的PageNum - 1
		Pageable pa = PageRequestConverter.of(pageNum, pageSize, Sort.by("id").descending());
		Page<T> page = baseDao.findAll(pa);
		List<T> list = page.getContent();
		PageContent<D> pageContent = new PageContent<>(transToDtoList(list), page.getTotalElements(), page.getTotalPages(),
				pageNum, pageSize);
		return pageContent;
	}

	@Override
	public PageContent<D> findPageData(Specification<T> spec, Pageable pageable) throws Exception {
		Page<T> page = baseDao.findAll(spec, pageable);
		List<T> list = page.getContent();
		
		// org.springframework.data.domain.Page.Page的0为第一页，
		PageContent<D> pageContent = new PageContent<>(transToDtoList(list), page.getTotalElements(), page.getTotalPages(),
				page.getNumber() + 1, page.getSize());
		return pageContent;
	}

	// ---- 标准分页 end ----

	// ---- ID INDEX 分页 start ----
	public IdIndexPageContent<D> findIdIndexPageData(Specification<T> spec, Direction direction, Integer sizeLimit) throws Exception{
		Page<T> page = baseDao.findAll(spec, PageRequest.of(0, sizeLimit, Sort.by(direction, "id")));
		List<T> list = page.getContent();
		
		IdIndexPageContent<D> pageContent = new IdIndexPageContent<>(transToDtoList(list), direction);
		return pageContent;
	}

	// ---- ID INDEX 分页 end ----

	@Override
	public List<D> findAll() throws Exception {
		List<T> tList = baseDao.findAll();
		return transToDtoList(tList);
	}

	@Override
	public List<D> find(Specification<T> spec) throws Exception {
		List<T> list = baseDao.findAll(spec, Sort.by("id").descending());
		return transToDtoList(list);
	}
	@Override
	public List<D> find(Sort sort) throws Exception {
		List<T> list = baseDao.findAll(new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return null;
			}}, sort);
		return transToDtoList(list);
	}

	@Override
	public List<D> find(Specification<T> spec, Sort sort) throws Exception {
		List<T> list = baseDao.findAll(spec, sort);
		return transToDtoList(list);
	}

	@Override
	public long countAll() throws Exception {
		return baseDao.count();
	}

	@Override
	public D get(Long id) throws Exception {
		T entity = baseDao.findById(id).orElse(null);
		return transToDtoDetail(entity);
	}

	@Override
	public void deleteById(Long id) throws Exception {
		baseDao.deleteById(id);
	}

	@Override
	public void deleteByIdInBatch(List<Long> ids) throws Exception {
		baseDao.deleteAllByIdInBatch(ids);
	}

	@Override
	public void save(D dto) throws Exception {

		T entity = transToEntity(null, dto);
		// TODO 处理软删除逻辑
		baseDao.save(entity);

		BeanUtils.copyProperties(entity, dto);

	}

	@Override
	public void update(D dto) throws Exception {
		Field idField = dto.getClass().getDeclaredField("id");
		idField.setAccessible(true);
		Long id = (Long)idField.get(dto);

		T e = baseDao.findById(id).orElse(null);
		T entity = transToEntity(e, dto);
		baseDao.save(entity);
	}

	/**
	 * 获取当前dto类型class,以便反射使用
	 *
	 * @return
	 */
	protected Class<D> currentDtoClass() {
		Type[] params = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
		return (Class<D>) params[1];
	}

	/**
	 * 获取当前entity类型class,以便反射使用
	 *
	 * @return
	 */
	protected Class<T> currentEntityClass() {

		Type[] params = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
		return (Class<T>) params[0];
	}

	/**
	 * entity转换dto,用于page和list方法使用
	 *
	 * @param entity
	 * @return dto
	 * @throws Exception
	 */
	public D transToDto(T entity) throws Exception {
		if (null == entity) {
			return null;
		}
		Class<D> clazz = currentDtoClass(); // 获取D的类型
		D dto = clazz.getDeclaredConstructor().newInstance();
		BeanUtils.copyProperties(entity, dto);
		return dto;
	}

	/**
	 * entity转换dto,用于get方法使用
	 *
	 * @param entity
	 * @return dto
	 * @throws Exception
	 */
	public D transToDtoDetail(T entity) throws Exception {
		if (null == entity) {
			return null;
		}
		Class<D> clazz = currentDtoClass(); // 获取D的类型
		D dto = clazz.getDeclaredConstructor().newInstance();
		BeanUtils.copyProperties(entity, dto);
		return dto;
	}

	/**
	 * entity转换dto,用于page和list方法使用
	 *
	 * @param entityList
	 * @return List<D> dto
	 * @throws Exception
	 */
	public List<D> transToDtoList(List<T> entityList) throws Exception {
		List<D> dtoList = new ArrayList<D>();
		if (entityList != null && entityList.size() > 0) {
			for (T entity : entityList) {
				D dto = transToDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}

	/**
	 * dto转换entity,用于save和update方法使用
	 *
	 * @param dto
	 * @return entity
	 * @throws Exception
	 */
	public T transToEntity(T entity, D dto) throws Exception {
		if (null == dto) {
			return null;
		}
		Class<T> clazz = currentEntityClass(); // 获取T的类型
		if(entity==null) {
			entity = clazz.getDeclaredConstructor().newInstance();
		}
		BeanUtils.copyProperties(dto, entity);
		return entity;
	}

//	/**
//	 *
//	 * @param dtoList
//	 * @return List<T> entity
//	 * @throws Exception
//	 */
//	public List<T> transToEntityList(List<D> dtoList) throws Exception {
//		List<T> entityList = new ArrayList<T>();
//		if (null != dtoList && dtoList.size() > 0) {
//			for (D dto : dtoList) {
//				T entity = transToEntity(dto);
//				entityList.add(entity);
//			}
//		}
//		return entityList;
//	}

	public abstract void initBaseDao();

}

package cn.tgxy.tgbase.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.tgbase.constant.BsCoreConstant;
import cn.tgxy.tgbase.dao.BsDepartmentDao;
import cn.tgxy.tgbase.dao.BsUserDepartmentMapDao;
import cn.tgxy.tgbase.dto.BsDepartmentDto;
import cn.tgxy.tgbase.po.BsDepartment;
import cn.tgxy.tgbase.service.BsDepartmentService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class BsDepartmentServiceImpl extends BsBaseServiceImpl<BsDepartment, BsDepartmentDto>
		implements BsDepartmentService {

	@Autowired
	private BsDepartmentDao departmentDao;

	@Autowired
	private BsUserDepartmentMapDao userDepartmentMapDao;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = departmentDao;
	}

	@Override
	@Transactional
	public void save(BsDepartmentDto dto) throws Exception {
		if (dto.getParentId() == null) {
			dto.setParentId(BsCoreConstant.TOP_P_ID);
			dto.setLevel(1);
		} else {
			Optional<BsDepartment> parent = departmentDao.findById(dto.getParentId());
			if (parent.isEmpty()) {
				dto.setParentId(BsCoreConstant.TOP_P_ID);
				dto.setLevel(1);
			} else {
				dto.setLevel(parent.get().getLevel().intValue() + 1);
			}
		}
		super.save(dto);
	}

	@Override
	@Transactional
	public void update(BsDepartmentDto dto) throws Exception {
		Optional<BsDepartment> d = departmentDao.findById(dto.getId());
		if (d.isPresent()) {
			BsDepartment department = d.get();
			department.setSort(dto.getSort());
			department.setName(dto.getName());
			department.setHeadId(dto.getHeadId());
			department.setEnable(dto.getEnable());
			departmentDao.save(department);
			BeanUtils.copyProperties(department, dto);
		}
	}
	
	@Override
	public void deleteById(Long id) throws Exception {
		super.deleteById(id);
	}
	
	@Override
	public void deleteByIdInBatch(List<Long> ids) throws Exception {
		super.deleteByIdInBatch(ids);
	}

	@Override
	public BsDepartmentDto transToDtoDetail(BsDepartment entity) throws Exception {
		BsDepartmentDto dto = super.transToDtoDetail(entity);
		if (dto != null) {
			Optional<BsDepartment> parent = departmentDao.findById(dto.getParentId());
			if (parent.isPresent()) {
				dto.setParentName(parent.get().getName());
			}
		}

		return dto;
	}

	@Override
	public boolean checkChild(Long id) throws Exception {
		return departmentDao.existsByParentId(id);
	}

	@Override
	public boolean checkUser(Long id) throws Exception {
		return userDepartmentMapDao.existsByDepartmentId(id);
	}

}

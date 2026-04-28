package cn.tgxy.tgbase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.constant.BsCoreErrorConstant;
import cn.tgxy.tgbase.dao.BsRoleDao;
import cn.tgxy.tgbase.dao.BsRolePermissionMapDao;
import cn.tgxy.tgbase.dto.BsRoleDto;
import cn.tgxy.tgbase.dto.request.BsSaveRolePermissionReqDto;
import cn.tgxy.tgbase.po.BsRole;
import cn.tgxy.tgbase.po.BsRolePermissionMap;
import cn.tgxy.tgbase.service.BsRoleService;
import jakarta.annotation.PostConstruct;

@Service
public class BsRoleServiceImpl extends BsBaseServiceImpl<BsRole, BsRoleDto> implements BsRoleService{

	@Autowired
	private BsRoleDao roleDao;
	
	@Autowired
	private BsRolePermissionMapDao rolePermissionMapDao;
	
	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = roleDao;
	}
	

	@Override
	public void save(BsRoleDto dto) throws Exception {
		// 判断编码是否有重复
		if (roleDao.existsByCode(dto.getCode())) {
			throw new ServiceException(BsCoreErrorConstant.ROLE_CODE_EXIST);
		}
		super.save(dto);
	}
	

	@Override
	public void update(BsRoleDto dto) throws Exception {
		BsRole original = roleDao.findById(dto.getId()).orElse(null);
		if (original != null) {
			// 判断code是否修改
			if (!original.getCode().equals(dto.getCode())) {
				if (roleDao.existsByCode(dto.getCode())) {
					throw new ServiceException(BsCoreErrorConstant.ROLE_CODE_EXIST);
				}
			}
			super.update(dto);
		}
	}
	
	@Override
	public List<Long> listRolePermissionId(Long roleId) throws Exception {
		return roleDao.findRolePermission(roleId);
	}

	@Override
	@Transactional
	public void assignRolePermission(BsSaveRolePermissionReqDto dto) throws Exception {
		rolePermissionMapDao.deleteByRoleId(dto.getRoleId());
		List<Long> permissionIds = dto.getPermissionIds();
		for (Long pId : permissionIds) {
			BsRolePermissionMap po = new BsRolePermissionMap();
			po.setRoleId(dto.getRoleId());
			po.setPermissionId(pId);
			rolePermissionMapDao.save(po);
		}
		
	}


	@Override
	public List<BsRoleDto> findByUserId(Long userId) throws Exception {
		List<BsRole> list = roleDao.findByUserId(userId);
		return transToDtoList(list);
	}


}

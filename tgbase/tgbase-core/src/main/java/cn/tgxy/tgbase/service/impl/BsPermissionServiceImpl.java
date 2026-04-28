package cn.tgxy.tgbase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.constant.BsConstant;
import cn.tgxy.tgbase.constant.BsCoreErrorConstant;
import cn.tgxy.tgbase.dao.BsPermissionDao;
import cn.tgxy.tgbase.dto.BsPermissionDto;
import cn.tgxy.tgbase.po.BsPermission;
import cn.tgxy.tgbase.service.BsPermissionService;
import jakarta.annotation.PostConstruct;

@Service
public class BsPermissionServiceImpl extends BsBaseServiceImpl<BsPermission, BsPermissionDto>
implements BsPermissionService{

	@Autowired
	private BsPermissionDao permissionDao;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = permissionDao;
	}
	
	@Override
	public boolean checkExistChildById(Long id) throws Exception {
		return permissionDao.existsByParentId(id);
	}
	
	@Override
	public void save(BsPermissionDto dto) throws Exception {
		if(dto.getParentId() == null || dto.getParentId().intValue()==0) {
			dto.setParentId(BsConstant.TOP_P_ID);
		}
		if(permissionDao.existsByCode(dto.getCode())) {
			throw new ServiceException(BsCoreErrorConstant.PERMISSION_CODE_EXIST);
		}
		super.save(dto);
	}
	
	@Override
	public void update(BsPermissionDto dto) throws Exception {

		if(dto.getParentId() == null || dto.getParentId().intValue()==0) {
			dto.setParentId(BsConstant.TOP_P_ID);
		}
		BsPermission original = permissionDao.findById(dto.getId()).orElse(null);
		if(original!=null) {
			if(!original.getCode().equals(dto.getCode())) {
				if(permissionDao.existsByCode(dto.getCode())) {
					throw new ServiceException(BsCoreErrorConstant.PERMISSION_CODE_EXIST);
				}
			}
			super.update(dto);
		}
	}
	
	@Override
	public BsPermissionDto transToDtoDetail(BsPermission entity) throws Exception{
		BsPermissionDto dto = super.transToDtoDetail(entity);
		if(dto!=null && dto.getParentId()!=null && !dto.getParentId().equals(BsConstant.TOP_P_ID)) {
			BsPermission parent = permissionDao.findById(dto.getParentId()).orElse(null);
			if(parent!=null) {
				dto.setParentName(parent.getName());
			}
		}
		return dto;
	}

	@Override
	public List<BsPermission> listByUserId(Long userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}






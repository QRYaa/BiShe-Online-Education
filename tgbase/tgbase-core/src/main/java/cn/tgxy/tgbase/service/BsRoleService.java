package cn.tgxy.tgbase.service;

import java.util.List;

import cn.tgxy.tgbase.dto.BsRoleDto;
import cn.tgxy.tgbase.dto.request.BsSaveRolePermissionReqDto;
import cn.tgxy.tgbase.po.BsRole;

public interface BsRoleService extends BsBaseService<BsRole, BsRoleDto> {
	
	public List<Long> listRolePermissionId(Long roleId) throws Exception;
	
	public void assignRolePermission(BsSaveRolePermissionReqDto dto) throws Exception;
	
	public List<BsRoleDto> findByUserId(Long userId) throws Exception;
}

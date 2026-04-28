package cn.tgxy.tgbase.dto.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BsSaveRolePermissionReqDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long roleId;

	private List<Long> permissionIds = new ArrayList<>();

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public List<Long> getPermissionIds() {
		return permissionIds;
	}

	public void setPermissionIds(List<Long> permissionIds) {
		this.permissionIds = permissionIds;
	}

	@Override
	public String toString() {
		return "BsSaveRolePermissionReqDto [roleId=" + roleId + ", permissionIds=" + permissionIds + "]";
	}

}

package cn.tgxy.tgbase.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chris Deng
 * @Date 2024/04/02 10:46:07
 */
public class BsPermissionDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private Long parentId;
	private String parentName;
	private String name;
	private String code;
	private String paths;
	private Integer sort;
	private List<BsPermissionDto> children = new ArrayList<BsPermissionDto>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPaths() {
		return paths;
	}

	public void setPaths(String paths) {
		this.paths = paths;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public List<BsPermissionDto> getChildren() {
		return children;
	}

	public void setChildren(List<BsPermissionDto> children) {
		this.children = children;
	}
}

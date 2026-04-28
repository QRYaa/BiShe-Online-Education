package cn.tgxy.tgbase.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BsDepartmentDto implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Long parentId;
	private String parentName;//父级部门名称
	private Integer level;
	private Long headId;
	private Integer sort;
	private Integer enable;
	private List<BsDepartmentDto> children = new ArrayList<>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Long getHeadId() {
		return headId;
	}
	public void setHeadId(Long headId) {
		this.headId = headId;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
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
	public List<BsDepartmentDto> getChildren() {
		return children;
	}
	public void setChildren(List<BsDepartmentDto> children) {
		this.children = children;
	}
	
}

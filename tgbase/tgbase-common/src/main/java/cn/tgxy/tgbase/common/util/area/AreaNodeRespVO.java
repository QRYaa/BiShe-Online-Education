package cn.tgxy.tgbase.common.util.area;

import java.util.List;

public class AreaNodeRespVO {

    private Integer id;

    private String name;

    /**
     * 子节点
     */
    private List<AreaNodeRespVO> children;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AreaNodeRespVO> getChildren() {
		return children;
	}

	public void setChildren(List<AreaNodeRespVO> children) {
		this.children = children;
	}

}

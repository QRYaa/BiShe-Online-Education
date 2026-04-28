package cn.tgxy.tgbase.common.util.area;

import java.util.List;

/**
 * 区域节点，包括国家、省份、城市、地区等信息
 *
 * 数据可见 resources/area.csv 文件
 *
 * @author 芋道源码
 */

public class Area {

    /**
     * 编号 - 全球，即根目录
     */
    public static final Integer ID_GLOBAL = 0;
    /**
     * 编号 - 中国
     */
    public static final Integer ID_CHINA = 1;

    /**
     * 编号
     */
    private Integer id;
    /**
     * 名字
     */
    private String name;
    /**
     * 类型
     *
     * 枚举 {@link AreaTypeEnum}
     */
    private Integer type;

    /**
     * 父节点
     */
    private Area parent;
    /**
     * 子节点
     */
    private List<Area> children;
    
    public Area(Integer id, String name, Integer type, Area parent, List<Area> children) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.parent = parent;
        this.children = children;
    }
    
    public Area() {
    }
    
	public static Integer getIdGlobal() {
		return ID_GLOBAL;
	}
	public static Integer getIdChina() {
		return ID_CHINA;
	}
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Area getParent() {
		return parent;
	}
	public void setParent(Area parent) {
		this.parent = parent;
	}
	public List<Area> getChildren() {
		return children;
	}
	public void setChildren(List<Area> children) {
		this.children = children;
	}
    
    

}

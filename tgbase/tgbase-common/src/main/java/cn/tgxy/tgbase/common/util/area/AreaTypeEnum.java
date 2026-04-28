package cn.tgxy.tgbase.common.util.area;

import java.util.Arrays;

/**
 * 区域类型枚举
 *
 * @author 芋道源码
 */
public enum AreaTypeEnum{

    COUNTRY(1, "国家"),
    PROVINCE(2, "省份"),
    CITY(3, "城市"),
    DISTRICT(4, "地区"), // 县、镇、区等
    ;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(AreaTypeEnum::getType).toArray();

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 名字
     */
    private final String name;
    
    AreaTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }
    
    public int[] array() {
        return ARRAYS;
    }

    public static int[] getArrays() {
		return ARRAYS;
	}

	public Integer getType() {
		return type;
	}

	public String getName() {
		return name;
	}

}

package cn.tgxy.tgbase.dao;

import cn.tgxy.tgbase.po.BsSetting;

public interface BsSettingDao extends BsBaseDao<BsSetting, Long>{
	BsSetting findByCode(String code);
	boolean existsByCode(String code);

}

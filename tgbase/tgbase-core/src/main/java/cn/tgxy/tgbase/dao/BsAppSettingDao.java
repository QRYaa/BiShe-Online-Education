package cn.tgxy.tgbase.dao;

import cn.tgxy.tgbase.po.BsAppSetting;

public interface BsAppSettingDao extends BsBaseDao<BsAppSetting, Long>{

	BsAppSetting findByApplicationIdAndCode(Long id, String settingCode);

	boolean existsByCode(String code);

	boolean existsByApplicationIdAndCode(Long applicationId, String code);

}

package cn.tgxy.tgbase.service;

import cn.tgxy.tgbase.dto.BsAppSettingDto;
import cn.tgxy.tgbase.po.BsAppSetting;

public interface BsAppSettingService extends BsBaseService<BsAppSetting, BsAppSettingDto>{

	String getSetting(String appCode, String settingCode);

}

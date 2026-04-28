package cn.tgxy.tgbase.service;

import cn.tgxy.tgbase.dto.BsSettingDto;
import cn.tgxy.tgbase.po.BsSetting;

public interface BsSettingService extends BsBaseService<BsSetting, BsSettingDto>{
	public BsSettingDto findByCode(String code) throws Exception;
	public String findValueByCode(String code) throws Exception;
}

package cn.tgxy.tgbase.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.constant.BsCoreErrorConstant;
import cn.tgxy.tgbase.dao.BsAppSettingDao;
import cn.tgxy.tgbase.dao.BsApplicationDao;
import cn.tgxy.tgbase.dto.BsAppSettingDto;
import cn.tgxy.tgbase.po.BsAppSetting;
import cn.tgxy.tgbase.po.BsApplication;
import cn.tgxy.tgbase.service.BsAppSettingService;
import jakarta.annotation.PostConstruct;

@Service
public class BsAppSettingServiceImpl extends BsBaseServiceImpl<BsAppSetting, BsAppSettingDto> implements BsAppSettingService{

	@Autowired
	private BsAppSettingDao appSettingDao;
	
	@Autowired
	private BsApplicationDao applicationDao;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = appSettingDao;
	}
	
	@Override
	public void save(BsAppSettingDto dto) throws Exception {
		if(appSettingDao.existsByApplicationIdAndCode(dto.getApplicationId(),dto.getCode())) {
			throw new ServiceException(BsCoreErrorConstant.APP_SETTING_CODE_EXIST);
		}
		super.save(dto);
	}
	
	@Override
	public void update(BsAppSettingDto dto) throws Exception {
		BsAppSetting appSetting=appSettingDao.findById(dto.getId()).orElse(null);
		if(appSetting==null) return;
		if(!appSetting.getCode().equals(dto.getCode())) {
			if(appSettingDao.existsByApplicationIdAndCode(dto.getApplicationId(),dto.getCode())) {
				throw new ServiceException(BsCoreErrorConstant.APP_SETTING_CODE_EXIST);
			}
		}
		super.update(dto);
	}
	
	@Override
	public String getSetting(String appCode,String settingCode) {
		BsApplication app=applicationDao.findByCode(appCode);
		if(app==null) return null;
		BsAppSetting appSetting= appSettingDao.findByApplicationIdAndCode(app.getId(),settingCode);
		if(appSetting==null) return null;
		return appSetting.getContent();
	}
}

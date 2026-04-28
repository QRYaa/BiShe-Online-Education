package cn.tgxy.tgbase.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.constant.BsCoreErrorConstant;
import cn.tgxy.tgbase.dao.BsSettingDao;
import cn.tgxy.tgbase.dto.BsSettingDto;
import cn.tgxy.tgbase.po.BsSetting;
import cn.tgxy.tgbase.service.BsSettingService;
import jakarta.annotation.PostConstruct;

@Service
public class BsSettingServiceImpl extends BsBaseServiceImpl<BsSetting, BsSettingDto> implements BsSettingService {

	@Autowired
	private BsSettingDao settingDao;

	@Override
	public BsSettingDto findByCode(String code) throws Exception {
		return transToDto(settingDao.findByCode(code));
	}

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = settingDao;
	}

	@Override
	public void save(BsSettingDto dto) throws Exception {
		if (settingDao.existsByCode(dto.getCode())) {
			throw new ServiceException(BsCoreErrorConstant.SETTING_CODE_EXIST);
		}
		super.save(dto);
	}

	@Override
	public void update(BsSettingDto dto) throws Exception {
		if (dto.getId() != null) {
			BsSetting s = settingDao.findById(dto.getId()).get();
			if (!s.getCode().equals(dto.getCode())) {
				if (settingDao.existsByCode(dto.getCode())) {
					throw new ServiceException(BsCoreErrorConstant.SETTING_CODE_EXIST);
				}
			}
			super.update(dto);
		}
	}

	@Override
	public String findValueByCode(String code) throws Exception {
		BsSetting s = settingDao.findByCode(code);
		if (s != null) {
			return s.getContent();
		} else {
			return "";
		}
	}

}

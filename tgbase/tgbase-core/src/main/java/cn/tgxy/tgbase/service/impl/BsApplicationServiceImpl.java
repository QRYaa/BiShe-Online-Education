package cn.tgxy.tgbase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.constant.BsCoreErrorConstant;
import cn.tgxy.tgbase.dao.BsApplicationDao;
import cn.tgxy.tgbase.dto.BsApplicationDto;
import cn.tgxy.tgbase.po.BsApplication;
import cn.tgxy.tgbase.service.BsApplicationService;
import jakarta.annotation.PostConstruct;

@Service
public class BsApplicationServiceImpl extends BsBaseServiceImpl<BsApplication, BsApplicationDto> implements BsApplicationService{

	@Autowired
	private BsApplicationDao applicationDao;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = applicationDao;
	}
	
	@Override
	public boolean verify(String code,String secret) {
		return applicationDao.existsByCodeAndSecret(code,secret);
	}

	@Override
	public void save(BsApplicationDto dto) throws Exception {
		if(applicationDao.existsByCode(dto.getCode())) {
			throw new ServiceException(BsCoreErrorConstant.APP_CODE_EXIST);
		}
		super.save(dto);
	}
	
	@Override
	public void update(BsApplicationDto dto) throws Exception {
		BsApplication app=applicationDao.findById(dto.getId()).orElse(null);
		if(app==null) {
			return;
		}
		if(!dto.getCode().equals(app.getCode())) {
			if(applicationDao.existsByCode(dto.getCode())) {
				throw new ServiceException(BsCoreErrorConstant.APP_CODE_EXIST);
			}
		}
		super.update(dto);
	}
	
	@Override
	public void changeSecret(Long id, String secret) {
		applicationDao.updateSecretById(id,secret);
	}

	@Override
	public List<BsApplicationDto> findByUserId(Long userId) throws Exception {
		List<BsApplication> appList=applicationDao.findByUserId(userId);
		return transToDtoList(appList);
	}
	
}

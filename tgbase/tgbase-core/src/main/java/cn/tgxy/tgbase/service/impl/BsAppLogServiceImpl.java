package cn.tgxy.tgbase.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import cn.tgxy.tgbase.constant.BsCoreConstant;
import cn.tgxy.tgbase.dao.BsAppLogDao;
import cn.tgxy.tgbase.dto.BsAppLogDto;
import cn.tgxy.tgbase.po.BsAppLog;
import cn.tgxy.tgbase.service.BsAppLogService;
import jakarta.annotation.PostConstruct;

@Service("bsAppLogService")
public class BsAppLogServiceImpl extends BsBaseServiceImpl<BsAppLog, BsAppLogDto> implements BsAppLogService{

	@Autowired
	private BsAppLogDao appLogDao;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = appLogDao;
	}
	
	@Override
	@Async
	public void saveLog(String trackId, Long appId, Integer type, String content, String exceptionInfo, Integer result) {
		BsAppLog log = new BsAppLog();
		log.setTrackId(trackId);
		log.setAppId(appId);
		log.setType(type);
		if(content.length()>BsCoreConstant.LOG_MESSAGE_LEN) content=content.substring(0,BsCoreConstant.LOG_MESSAGE_LEN);
		log.setContent(content);
		if(exceptionInfo.length()>BsCoreConstant.LOG_ERROR_LEN) exceptionInfo=exceptionInfo.substring(0,BsCoreConstant.LOG_ERROR_LEN);
		log.setExceptionInfo(exceptionInfo);
		log.setResult(result);
		log.setCreateTime(new Date());
		
		appLogDao.save(log);
	}
}

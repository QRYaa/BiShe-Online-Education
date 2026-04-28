package cn.tgxy.tgbase.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.tgbase.dao.BsTaskLogDao;
import cn.tgxy.tgbase.dto.BsTaskLogDto;
import cn.tgxy.tgbase.po.BsTaskLog;
import cn.tgxy.tgbase.service.BsTaskLogService;
import jakarta.annotation.PostConstruct;

@Service
public class BsTaskLogServiceImpl extends BsBaseServiceImpl<BsTaskLog, BsTaskLogDto> implements BsTaskLogService{

	@Autowired
	private BsTaskLogDao taskLogDao;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = taskLogDao;
	}
	
	@Override
	public void save(BsTaskLog entity)  {
		taskLogDao.save(entity);
	}
}

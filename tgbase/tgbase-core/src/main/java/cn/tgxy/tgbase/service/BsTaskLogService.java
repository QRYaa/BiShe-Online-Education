package cn.tgxy.tgbase.service;

import org.springframework.scheduling.annotation.Async;

import cn.tgxy.tgbase.dto.BsTaskLogDto;
import cn.tgxy.tgbase.po.BsTaskLog;

public interface BsTaskLogService extends BsBaseService<BsTaskLog, BsTaskLogDto>{

	@Async
	void save(BsTaskLog entity);

}

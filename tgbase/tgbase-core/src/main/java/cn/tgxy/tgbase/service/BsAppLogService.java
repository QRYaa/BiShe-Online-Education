package cn.tgxy.tgbase.service;

import cn.tgxy.tgbase.dto.BsAppLogDto;
import cn.tgxy.tgbase.po.BsAppLog;

public interface BsAppLogService extends BsBaseService<BsAppLog, BsAppLogDto> {

	void saveLog(String trackId, Long appId, Integer type, String content, String exceptionInfo, Integer result);

}

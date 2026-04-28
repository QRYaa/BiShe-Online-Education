package cn.tgxy.oledu.service;

import cn.tgxy.tgbase.service.BsBaseService;
import cn.tgxy.oledu.po.OeBanner;

import java.util.List;

import cn.tgxy.oledu.dto.OeBannerDto;

public interface OeBannerService extends BsBaseService<OeBanner, OeBannerDto>{

	void updateEnableTrueById(Long id);

	void updateEnableFalseById(Long id);

	List<OeBannerDto> findEnabledAndValid() throws Exception;

	void updateStatus(StringBuilder strB);

}

package cn.tgxy.tgbase.service;

import java.util.List;

import cn.tgxy.tgbase.dto.BsApplicationDto;
import cn.tgxy.tgbase.po.BsApplication;

public interface BsApplicationService extends BsBaseService<BsApplication, BsApplicationDto>{

	boolean verify(String code, String secret);

	void changeSecret(Long id, String secret);

	List<BsApplicationDto> findByUserId(Long userId) throws Exception;

}

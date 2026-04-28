package cn.tgxy.tgbase.service;

import cn.tgxy.tgbase.dto.BsDictDto;
import cn.tgxy.tgbase.po.BsDict;

public interface BsDictService extends BsBaseService<BsDict, BsDictDto>{
	public BsDictDto findByCode(String code) throws Exception;
}

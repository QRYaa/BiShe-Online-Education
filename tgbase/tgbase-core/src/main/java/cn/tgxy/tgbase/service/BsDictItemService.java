package cn.tgxy.tgbase.service;

import java.util.List;

import cn.tgxy.tgbase.dto.BsDictItemDto;
import cn.tgxy.tgbase.po.BsDictItem;

public interface BsDictItemService extends BsBaseService<BsDictItem, BsDictItemDto>{
	public List<BsDictItemDto> list(Long dictId) throws Exception;
	public void deleteByDictId(Long dictId) throws Exception;
}

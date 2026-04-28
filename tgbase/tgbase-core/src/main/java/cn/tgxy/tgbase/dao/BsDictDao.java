package cn.tgxy.tgbase.dao;

import cn.tgxy.tgbase.po.BsDict;

public interface BsDictDao extends BsBaseDao<BsDict, Long> {
	BsDict findByCode(String code);

	boolean existsByCode(String code);

}

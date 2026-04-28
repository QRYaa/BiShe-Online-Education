package cn.tgxy.oledu.dao;

import cn.tgxy.oledu.po.OeMemberAuthToken;
import cn.tgxy.tgbase.dao.BsBaseDao;

public interface OeMemberAuthTokenDao extends BsBaseDao<OeMemberAuthToken, Long>{

	OeMemberAuthToken findByMemberId(Long id);

}

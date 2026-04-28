package cn.tgxy.oledu.dao;

import cn.tgxy.oledu.po.OeMember;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.BsExtBaseDao;

public interface OeMemberExtDao extends BsExtBaseDao {
	public PageContent<OeMember> findPageContent(String name,String code,String tel,Integer gender,Integer areaId,Integer enable,String idCardName,String idCardNumber,Integer pageNum, Integer pageSize) throws Exception;
}

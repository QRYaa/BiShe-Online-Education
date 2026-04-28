package cn.tgxy.oledu.dao;

import cn.tgxy.oledu.po.OeMemberIdCardAudit;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.BsExtBaseDao;

public interface OeMemberIdCardAuditExtDao extends BsExtBaseDao {
	public PageContent<OeMemberIdCardAudit> findPageContent(String memberCode,Integer status,String idCardNumber,String idCardRealName,Integer pageNum, Integer pageSize) throws Exception;
}

package cn.tgxy.tgbase.dao;

import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.po.BsUser;

public interface BsUserExtDao extends BsExtBaseDao {
	
	public PageContent<BsUser> findPageContent(String username, String realName, Long departmentId, Integer gender, String email,
			String mobilePhone, Integer enable, Long roleId,Long appId, int pageNum, int pageSize) throws Exception;

}

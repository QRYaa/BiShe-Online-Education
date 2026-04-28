package cn.tgxy.tgbase.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.BsUserExtDao;
import cn.tgxy.tgbase.po.BsUser;

@Repository("userExtDao")
public class BsUserExtDaoImpl extends BsExtBaseDaoImpl implements BsUserExtDao {

	@Override
	public PageContent<BsUser> findPageContent(String username, String realName, Long departmentId, Integer gender,
			String email, String mobilePhone, Integer enable, Long roleId,Long appId, int pageNum, int pageSize) throws Exception {

		String hql = "SELECT u FROM BsUser u ";
		Map<String, Object> paramMap = new HashMap<>();
		if (roleId != null) {
			hql += " LEFT JOIN BsUserRoleMap urm ON u.id = urm.userId ";
		}
		if (appId != null) {
			hql += " LEFT JOIN BsUserAppMap uam ON u.id = uam.userId ";
		}
		if (departmentId != null) {
			if (departmentId.longValue() == -2) {
				hql += " LEFT JOIN BsUserDepartmentMap udm ON u.id = udm.userId ";
			} else if (departmentId.longValue() > 0) {
				hql += " INNER JOIN BsUserDepartmentMap udm ON u.id = udm.userId ";
			}
		}
		hql += " where 1=1 ";

		// conditions
		if (roleId != null && roleId.longValue() > 0) {
			hql += " AND urm.roleId = :roleId ";
			paramMap.put("roleId", roleId);
		}
		if (appId != null && appId.longValue() > 0) {
			hql += " AND uam.appId = :appId ";
			paramMap.put("appId", appId);
		}
		if (departmentId != null) {
			if (departmentId.longValue() == -2) {
				hql += " AND udm.departmentId is null ";
			} else if (departmentId.longValue() > 0) {
				hql += " AND udm.departmentId =  :departmentId";
				paramMap.put("departmentId", departmentId);

			}
		}
		if (StringUtils.isNotBlank(username)) {
			hql += " AND u.username = :username ";
			paramMap.put("username", username);
		}
		if (StringUtils.isNotBlank(realName)) {
			hql += " AND u.realName like :realName ";
			paramMap.put("realName", "%" + realName + "%");
		}
		if (gender != null) {
			hql += " AND u.gender = :gender ";
			paramMap.put("gender", gender);
		}
		if (StringUtils.isNotBlank(email)) {
			hql += " AND u.email = :email ";
			paramMap.put("email", email);
		}
		if (StringUtils.isNotBlank(mobilePhone)) {
			hql += " AND u.mobilePhone = :mobilePhone ";
			paramMap.put("mobilePhone", mobilePhone);
		}
		if (enable != null) {
			hql += " AND u.enable = :enable ";
			paramMap.put("enable", enable);
		}
		hql += " order by u.id asc";

		return super.findPageContent(BsUser.class, hql, pageNum, pageSize, paramMap);
	}

}

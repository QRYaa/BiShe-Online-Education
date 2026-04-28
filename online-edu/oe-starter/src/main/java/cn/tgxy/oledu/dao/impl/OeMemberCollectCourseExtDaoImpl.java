package cn.tgxy.oledu.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.tgxy.oledu.dao.OeMemberCollectCourseExtDao;
import cn.tgxy.oledu.po.OeMemberCollectCourse;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.impl.BsExtBaseDaoImpl;

@Repository("memberCollectCourseExtDao")
public class OeMemberCollectCourseExtDaoImpl extends BsExtBaseDaoImpl implements OeMemberCollectCourseExtDao {

	@Override
	public PageContent<OeMemberCollectCourse> findPageContent(Long memberId, String courseName, int pageNum,
			int pageSize) throws Exception {
		String hql = "SELECT mc FROM OeMemberCollectCourse mc ";
		Map<String, Object> paramMap = new HashMap<>();
		if(StringUtils.isNotEmpty(courseName)) {
			hql += " LEFT JOIN OeCourse c ON mc.courseId = c.id ";
		}
		hql += " where 1=1 ";
		if(memberId!=null) {
			hql += " AND mc.memberId = :memberId ";
			paramMap.put("memberId", memberId);
		}

		if(StringUtils.isNotEmpty(courseName)) {
			hql += " AND c.name like :courseName ";
			paramMap.put("courseName", courseName+"%");
		}
		hql += " order by mc.id desc";

		return super.findPageContent(OeMemberCollectCourse.class, hql, pageNum, pageSize, paramMap);
	}
	
}

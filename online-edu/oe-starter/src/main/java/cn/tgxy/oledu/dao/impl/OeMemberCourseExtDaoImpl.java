package cn.tgxy.oledu.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.tgxy.oledu.dao.OeMemberCourseExtDao;
import cn.tgxy.oledu.po.OeMemberCourse;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.impl.BsExtBaseDaoImpl;

@Repository("memberCourseExtDao")
public class OeMemberCourseExtDaoImpl extends BsExtBaseDaoImpl implements OeMemberCourseExtDao {

	@Override
	public PageContent<OeMemberCourse> findPageContent(Long memberId, String courseName,Boolean coursePublished,Integer noteNum,int pageNum, int pageSize) throws Exception {
		String hql = "SELECT mc FROM OeMemberCourse mc ";
		Map<String, Object> paramMap = new HashMap<>();
		if(StringUtils.isNotEmpty(courseName) || coursePublished!=null) {
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
		if(coursePublished!=null) {
			hql += " AND c.published = :coursePublished ";
			paramMap.put("coursePublished", coursePublished);
		}
		if(noteNum!=null) {
			hql += " AND mc.noteNum > :noteNum ";
			paramMap.put("noteNum", noteNum);
		}
		hql += " order by mc.lastViewTime desc";

		return super.findPageContent(OeMemberCourse.class, hql, pageNum, pageSize, paramMap);
	}
}

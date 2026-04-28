package cn.tgxy.oledu.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.tgxy.oledu.dao.OeCourseExtDao;
import cn.tgxy.oledu.po.OeCourse;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.impl.BsExtBaseDaoImpl;

@Repository("oeCourseExtDao")
public class OeCourseExtDaoImpl extends BsExtBaseDaoImpl implements OeCourseExtDao {

	@Override
	public PageContent<OeCourse> findPageContent(Long courseTypeId,Long teacherId,String name,Boolean published,Integer status,String courseTypeCode,Long memberId,String teacherCode,int pageNum, int pageSize) throws Exception {
		String hql = "SELECT c FROM OeCourse c ";
		Map<String, Object> paramMap = new HashMap<>();
		if(StringUtils.isNotEmpty(courseTypeCode)) {
			hql += " LEFT JOIN OeCourseType ct ON c.courseTypeId = ct.id ";
		}
		if(memberId!=null) {
			hql +=" LEFT JOIN OeMemberCourse mc ON c.id = mc.courseId ";
		}
		if(StringUtils.isNotEmpty(teacherCode)) {
			hql += " LEFT JOIN OeTeacher t ON c.teacherId = t.id ";
		}
		
		hql += " where 1=1 ";
		if(courseTypeId != null) {
			hql += " AND c.courseTypeId = :courseTypeId ";
			paramMap.put("courseTypeId", courseTypeId);
		}
		if(teacherId != null) {
			hql += " AND c.teacherId = :teacherId ";
			paramMap.put("teacherId", teacherId);
		}
		if(StringUtils.isNotEmpty(name)) {
			hql += " AND c.name like :name ";
			paramMap.put("name","%"+name+"%");
		}
		if(published != null) {
			hql+=" AND c.published = :published ";
			paramMap.put("published", published);
		}
		if(status!=null) {
			hql+=" AND c.status = :status ";
			paramMap.put("status", status);
		}
		
		
		if(StringUtils.isNotEmpty(courseTypeCode)) {
			hql += " AND ct.code = :courseTypeCode ";
			paramMap.put("courseTypeCode", courseTypeCode);
		}
		if(memberId != null) {
			hql += " AND mc.memberId = :memberId ";
			paramMap.put("memberId", memberId);
		}
		if(StringUtils.isNotEmpty(teacherCode)) {
			hql+=" AND t.code = :teacherCode ";
			paramMap.put("teacherCode", teacherCode);
		}
		
		hql += " order by c.id desc";

		return super.findPageContent(OeCourse.class, hql, pageNum, pageSize, paramMap);
	}

}

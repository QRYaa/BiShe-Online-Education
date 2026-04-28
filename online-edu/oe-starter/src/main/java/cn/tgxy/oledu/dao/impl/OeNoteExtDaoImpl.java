package cn.tgxy.oledu.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.tgxy.oledu.dao.OeNoteExtDao;
import cn.tgxy.oledu.po.OeNote;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.impl.BsExtBaseDaoImpl;

@Repository("noteExtDao")
public class OeNoteExtDaoImpl extends BsExtBaseDaoImpl implements OeNoteExtDao {

	@Override
	public PageContent<OeNote> findPageContent(Date startTime,Date endTime,Integer status,Boolean enable,String memberCode, String courseName, String lessonName, Long memberId,Long lessonId,Long courseId,Integer pageNum,
			Integer pageSize) throws Exception {
		String hql = "SELECT n FROM OeNote n ";
		Map<String, Object> paramMap = new HashMap<>();
		if (StringUtils.isNotEmpty(memberCode)) {
			hql += " LEFT JOIN OeMember m ON n.memberId = m.id ";
		}
		if (StringUtils.isNotEmpty(lessonName) || StringUtils.isNotEmpty(courseName)||courseId!=null) {
			hql += " LEFT JOIN OeLesson l ON n.lessonId = l.id ";
		}
		if(StringUtils.isNotEmpty(courseName)||courseId!=null) {
			hql += " LEFT JOIN OeChapter c ON l.chapterId = c.id ";
		}
		if(StringUtils.isNotEmpty(courseName)) {
            hql += " LEFT JOIN OeCourse co ON c.courseId = co.id ";
		}
		hql += " WHERE 1=1 ";
		if(startTime!=null) {
			hql+=" AND n.createTime >= :startTime ";
			paramMap.put("startTime", startTime);
		}
		if(endTime!=null) {
			hql+=" AND n.createTime <= :endTime ";
			paramMap.put("endTime", endTime);
		}
		if(status!=null) {
			hql+=" AND n.status = :status ";
			paramMap.put("status", status);
		}
		if(enable!=null) {
			hql+=" AND n.enable = :enable ";
			paramMap.put("enable", enable);
		}
		if(memberId!=null) {
			hql += " AND n.memberId = :memberId ";
			paramMap.put("memberId", memberId);
		}
		if (StringUtils.isNotEmpty(memberCode)) {
			hql += " AND m.code = :memberCode ";
			paramMap.put("memberCode", memberCode);
		}
		if (StringUtils.isNotEmpty(lessonName)) {
			hql += " AND l.name LIKE :lessonName ";
			paramMap.put("lessonName", "%" + lessonName + "%");
		}
		if(lessonId!=null) {
			hql+=" AND n.lessonId = :lessonId ";
			paramMap.put("lessonId", lessonId);
		}
        if(courseId!=null) {
        	hql += " AND c.courseId = :courseId ";
            paramMap.put("courseId", courseId);
        }
        if (StringUtils.isNotEmpty(courseName)) {
            hql += " AND co.name LIKE :courseName ";
            paramMap.put("courseName", "%" + courseName + "%");
        }

		// 默认按创建时间倒序
		hql += "ORDER BY n.id DESC";

		// 调用父类分页方法
		return super.findPageContent(OeNote.class, hql, pageNum, pageSize, paramMap);
	}

}

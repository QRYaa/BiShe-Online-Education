package cn.tgxy.oledu.dao;

import cn.tgxy.oledu.po.OeMemberCourse;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.BsExtBaseDao;

public interface OeMemberCourseExtDao extends BsExtBaseDao {
	public PageContent<OeMemberCourse> findPageContent(Long memberId,String courseName,Boolean coursePublished,Integer noteNum,int pageNum, int pageSize) throws Exception;
}

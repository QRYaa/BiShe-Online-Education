package cn.tgxy.oledu.dao;

import cn.tgxy.oledu.po.OeMemberCollectCourse;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.BsExtBaseDao;

public interface OeMemberCollectCourseExtDao extends BsExtBaseDao {
	public PageContent<OeMemberCollectCourse> findPageContent(Long memberId,String courseName,int pageNum, int pageSize) throws Exception;
}

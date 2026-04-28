package cn.tgxy.oledu.dao;

import cn.tgxy.oledu.po.OeCourse;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.BsExtBaseDao;

public interface OeCourseExtDao extends BsExtBaseDao {
	public PageContent<OeCourse> findPageContent(Long courseTypeId,Long teacherId,String name,Boolean published,Integer status,String courseTypeCode,Long memberId,String teacherCode,int pageNum, int pageSize) throws Exception;

}

package cn.tgxy.oledu.dao;

import java.util.Date;

import cn.tgxy.oledu.po.OeNote;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.BsExtBaseDao;

public interface OeNoteExtDao extends BsExtBaseDao {
	public PageContent<OeNote> findPageContent(Date startTime,Date endTime,Integer status,Boolean enable,String memberCode,String courseName,String lessonName,Long memberId,Long lessonId,Long courseId,Integer pageNum,Integer pageSize) throws Exception;
}

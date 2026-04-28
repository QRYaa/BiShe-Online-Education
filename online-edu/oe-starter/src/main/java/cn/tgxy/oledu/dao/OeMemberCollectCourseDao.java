package cn.tgxy.oledu.dao;

import cn.tgxy.oledu.po.OeMemberCollectCourse;
import cn.tgxy.tgbase.dao.BsBaseDao;
import jakarta.transaction.Transactional;

public interface OeMemberCollectCourseDao extends BsBaseDao<OeMemberCollectCourse, Long>{

	boolean existsByMemberIdAndCourseId(Long id, Long courseId);

	@Transactional
	void deleteByMemberIdAndCourseId(Long id, Long courseId);

}

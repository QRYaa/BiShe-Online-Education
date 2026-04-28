package cn.tgxy.oledu.service;

import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.service.BsBaseService;
import cn.tgxy.oledu.po.OeMemberCollectCourse;
import cn.tgxy.oledu.dto.OeMemberCollectCourseDto;

public interface OeMemberCollectCourseService extends BsBaseService<OeMemberCollectCourse, OeMemberCollectCourseDto>{

	PageContent<OeMemberCollectCourseDto> findPageData(Long memberId, String courseName, int pageNum, int pageSize) throws Exception;

	boolean existsByMemberIdAndCourseId(Long id, Long courseId);

	void addByMemberIdAndCourseId(Long id, Long courseId);

	void deleteByMemberIdAndCourseId(Long id, Long courseId);

}

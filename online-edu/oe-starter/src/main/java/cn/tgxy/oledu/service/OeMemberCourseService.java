package cn.tgxy.oledu.service;

import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.service.BsBaseService;
import cn.tgxy.oledu.po.OeMemberCourse;

import java.util.List;

import cn.tgxy.oledu.dto.OeMemberCourseDto;

public interface OeMemberCourseService extends BsBaseService<OeMemberCourse, OeMemberCourseDto>{

	PageContent<OeMemberCourseDto> findPageData(Long memberId, String courseName,Boolean coursePublished, Integer noteNum,int pageNum, int pageSize)
			throws Exception;

	OeMemberCourseDto findByMemberIdAndCourseCode(Long id, String courseCode) throws Exception;

	OeMemberCourseDto findByMemberIdAndCourseId(Long id, Long courseId) throws Exception;

	boolean existsByMemberIdAndCourseCode(Long memberId, String courseCode) throws Exception;

	boolean existsByMemberIdAndCourseId(Long memberId, Long courseId) throws Exception;

	void checkMemberIdAndLessonId(Long id, Long lessonId) throws Exception;

	void changLessonId(Long lessonId, Long id, Long courseId);

	List<OeMemberCourseDto> findByMemberId(Long memberId) throws Exception;

	void activateCourse(Long memberId, Long courseId);

	void saveByMemberIdAndCourseId(Long memberId, List<Long> courseId);

	void changeLastViewTime(Long id, Long courseId);

	void changeCompletionPercentage(Long id, Long courseId);

	void addNoteNumById(Long memberCourseId);

	void subtractNoteNumById(Long memberCourseId);

}

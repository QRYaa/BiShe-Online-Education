package cn.tgxy.oledu.service;

import cn.tgxy.tgbase.service.BsBaseService;
import cn.tgxy.oledu.po.OeMemberLesson;

import java.util.List;

import cn.tgxy.oledu.dto.OeMemberLessonDto;

public interface OeMemberLessonService extends BsBaseService<OeMemberLesson, OeMemberLessonDto>{

	List<OeMemberLessonDto> findByMemberCourseId(Long memberCourseId) throws Exception;

	void changeWatchedStatus(Integer watchedStatus, Long id, Long lessonId);

	void changeLastViewedTime(Long memberId, Long lessonId);

	void changeProgress(Long id, Long lessonId,Integer progress);

	void changeDuration(Long id, Long lessonId);

}

package cn.tgxy.oledu.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.oledu.constant.OeConstant;
import cn.tgxy.oledu.constant.OeErrorConstant;
import cn.tgxy.oledu.dao.OeLessonDao;
import cn.tgxy.oledu.dao.OeMemberCourseDao;
import cn.tgxy.oledu.dao.OeMemberLessonDao;
import cn.tgxy.oledu.dto.OeMemberLessonDto;
import cn.tgxy.oledu.po.OeLesson;
import cn.tgxy.oledu.po.OeMemberLesson;
import cn.tgxy.oledu.service.OeCourseService;
import cn.tgxy.oledu.service.OeMemberLessonService;
import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;

@Service
public class OeMemberLessonServiceImpl extends BsBaseServiceImpl<OeMemberLesson, OeMemberLessonDto> implements OeMemberLessonService{

	@Autowired
	private OeMemberLessonDao memberLessonDao;
	
	@Autowired
	private OeCourseService courseService;
	
	@Autowired
	private OeMemberCourseDao memberCourseDao;
	
	@Autowired
	private OeLessonDao lessonDao;
	
	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = memberLessonDao;
	}
	
	@Override
	public List<OeMemberLessonDto> findByMemberCourseId(Long memberCourseId) throws Exception{
		List<OeMemberLesson> memberLessons=memberLessonDao.findByMemberCourseId(memberCourseId);
		return transToDtoList(memberLessons);
	}

	@Override
	public void changeWatchedStatus(Integer watchedStatus, Long id, Long lessonId) {
		if(watchedStatus == OeConstant.WATCHING && !memberLessonDao.existsByMemberIdAndLessonId(id, lessonId)) {
			OeMemberLesson entity=new OeMemberLesson();
			
			Long courseId=courseService.getIdByLessonId(lessonId);
			Long memberCourseId=memberCourseDao.getIdByMemberIdAndCourseId(id, courseId);
			
			entity.setMemberId(id);
			entity.setLessonId(lessonId);
			entity.setMemberCourseId(memberCourseId);
			entity.setWatchedStatus(watchedStatus);
			
			memberLessonDao.save(entity);
		}
		else {
			if(watchedStatus == OeConstant.WATCHED) {
				OeLesson dbLesson=lessonDao.findById(lessonId).orElse(null);
				if(dbLesson==null) throw new ServiceException(OeErrorConstant.LESSON_NOT_EXISTS);
				if(dbLesson.getDuration()==null||dbLesson.getDuration()==0) throw new ServiceException(OeErrorConstant.LESSON_NO_VIDEO);
				OeMemberLesson dbMemberLesson=memberLessonDao.findByMemberIdAndLessonId(id,lessonId);
				if(dbMemberLesson==null || dbMemberLesson.getDuration()==null || (double)dbMemberLesson.getDuration()/(double)dbLesson.getDuration()<OeConstant.WATCHED_PERCENTAGE) throw new ServiceException(OeErrorConstant.MEMBER_WATCH_LESSON_TIME_NOT_ENOUGH);
			}
			memberLessonDao.updateWatchedStatusByMemberIdAndLessonId(watchedStatus,id,lessonId);
		}
		
	}
	
	@Override
	public void changeLastViewedTime(Long memberId,Long lessonId) {
		if(!memberLessonDao.existsByMemberIdAndLessonId(memberId, lessonId)) {
			OeMemberLesson entity=new OeMemberLesson();
			
			Long courseId=courseService.getIdByLessonId(lessonId);
			Long memberCourseId=memberCourseDao.getIdByMemberIdAndCourseId(memberId, courseId);
			
			entity.setMemberId(memberId);
			entity.setLessonId(lessonId);
			entity.setMemberCourseId(memberCourseId);
			entity.setLastViewedTime(new Date());
			
			memberLessonDao.save(entity);
		}
		else {
			memberLessonDao.updateLastViewedTimeByMemberIdAndLessonId(new Date(),memberId,lessonId);
		}
	}

	@Override
	public void changeProgress(Long memberId, Long lessonId,Integer progress) {
		if(!memberLessonDao.existsByMemberIdAndLessonId(memberId, lessonId)) {
			OeMemberLesson entity=new OeMemberLesson();
			
			Long courseId=courseService.getIdByLessonId(lessonId);
			Long memberCourseId=memberCourseDao.getIdByMemberIdAndCourseId(memberId, courseId);
			
			entity.setMemberId(memberId);
			entity.setLessonId(lessonId);
			entity.setMemberCourseId(memberCourseId);
			entity.setProgress(progress);
			
			memberLessonDao.save(entity);
		}
		else {
			memberLessonDao.updateProgressByMemberIdAndLessonId(progress,memberId,lessonId);
		}
		
	}

	@Override
	public void changeDuration(Long memberId, Long lessonId) {
		if(!memberLessonDao.existsByMemberIdAndLessonId(memberId, lessonId)) {
			OeMemberLesson entity=new OeMemberLesson();
			
			Long courseId=courseService.getIdByLessonId(lessonId);
			Long memberCourseId=memberCourseDao.getIdByMemberIdAndCourseId(memberId, courseId);
			
			entity.setMemberId(memberId);
			entity.setLessonId(lessonId);
			entity.setMemberCourseId(memberCourseId);
			entity.setDuration(OeConstant.WATCH_DURATION);
			
			memberLessonDao.save(entity);
		}
		else {
			memberLessonDao.updateDurationByMemberIdAndLessonId(OeConstant.WATCH_DURATION,memberId,lessonId);
		}
		
	}

}

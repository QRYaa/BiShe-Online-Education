package cn.tgxy.oledu.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.oledu.constant.OeConstant;
import cn.tgxy.oledu.constant.OeErrorConstant;
import cn.tgxy.oledu.dao.OeLessonDao;
import cn.tgxy.oledu.dao.OeMemberCourseDao;
import cn.tgxy.oledu.dao.OeMemberCourseExtDao;
import cn.tgxy.oledu.dao.OeMemberLessonDao;
import cn.tgxy.oledu.dto.OeCourseDto;
import cn.tgxy.oledu.dto.OeMemberCourseDto;
import cn.tgxy.oledu.dto.OeMemberLessonDto;
import cn.tgxy.oledu.po.OeMemberCourse;
import cn.tgxy.oledu.service.OeCourseService;
import cn.tgxy.oledu.service.OeMemberCourseService;
import cn.tgxy.oledu.service.OeMemberLessonService;
import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;

@Service
public class OeMemberCourseServiceImpl extends BsBaseServiceImpl<OeMemberCourse, OeMemberCourseDto> implements OeMemberCourseService{

	@Autowired
	private OeMemberCourseDao memberCourseDao;
	
	@Autowired
	private OeMemberCourseExtDao memberCourseExtDao;
	
	@Autowired
	private OeLessonDao lessonDao;
	
	@Autowired
	private OeMemberLessonDao memberLessonDao;
	
	@Autowired
	private OeCourseService courseService;
	
	@Autowired
	private OeMemberLessonService memberLessonService;
	
	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = memberCourseDao;
	}
	
	@Override
	public PageContent<OeMemberCourseDto> findPageData(Long memberId, String courseName,Boolean coursePublished,Integer noteNum,int pageNum, int pageSize) throws Exception {
		PageContent<OeMemberCourse> pageContent = memberCourseExtDao.findPageContent(memberId,courseName,coursePublished,noteNum,pageNum, pageSize);
		PageContent<OeMemberCourseDto> page = new PageContent<>(transToDtoList(pageContent.getContent()),
				pageContent.getTotalItems(), pageContent.getPageNum(), pageSize);
		return page;
	}
	
	@Override
	public OeMemberCourseDto transToDto(OeMemberCourse memberCourse) throws Exception {
		OeMemberCourseDto memberCourseDto=super.transToDto(memberCourse);
		if(memberCourseDto==null) return null;
		OeCourseDto courseDto=courseService.get(memberCourse.getCourseId());
		memberCourseDto.setCourseDto(courseDto);
		return memberCourseDto;
	}
	
	@Override
	public OeMemberCourseDto transToDtoDetail(OeMemberCourse memberCourse) throws Exception {
		OeMemberCourseDto memberCourseDto=super.transToDto(memberCourse);
		if(memberCourseDto==null) return null;
		OeCourseDto courseDto=courseService.get(memberCourse.getCourseId());
		memberCourseDto.setCourseDto(courseDto);
		List<OeMemberLessonDto> memberLessonDtos=memberLessonService.findByMemberCourseId(memberCourse.getId());
		memberCourseDto.setMemberLessonDtos(memberLessonDtos);
		return memberCourseDto;
	}
	
	@Override
	public List<OeMemberCourseDto> findByMemberId(Long memberId) throws Exception {
		List<OeMemberCourse> memberCourses=memberCourseDao.findByMemberId(memberId);
		return super.transToDtoList(memberCourses);
	}
	
	@Override
	public void activateCourse(Long memberId, Long courseId) {
		if(memberCourseDao.existsByMemberIdAndCourseId(memberId, courseId)) return;
		OeMemberCourse memberCourse=new OeMemberCourse();
		memberCourse.setMemberId(memberId);
		memberCourse.setCourseId(courseId);
		memberCourseDao.save(memberCourse);
	}
	
	@Override
	public void saveByMemberIdAndCourseId(Long memberId, List<Long> courseIdList) {
		courseIdList.forEach(courseId->{
			if(memberCourseDao.existsByMemberIdAndCourseId(memberId, courseId)) return;
			OeMemberCourse memberCourse=new OeMemberCourse();
			memberCourse.setMemberId(memberId);
			memberCourse.setCourseId(courseId);
			memberCourseDao.save(memberCourse);
		});
		
	}

	@Override
	public OeMemberCourseDto findByMemberIdAndCourseCode(Long id, String courseCode) throws Exception{
		if(!courseService.existsByCodeAndEnable(courseCode)) return null;
		Long courseId=courseService.getIdByCode(courseCode);
		return transToDtoDetail(memberCourseDao.findByMemberIdAndCourseId(id,courseId));
	}

	@Override
	public OeMemberCourseDto findByMemberIdAndCourseId(Long id, Long courseId) throws Exception{
		if(!courseService.existsByIdAndEnable(courseId)) return null;
		return transToDtoDetail(memberCourseDao.findByMemberIdAndCourseId(id,courseId));
	}
	
	@Override
	public boolean existsByMemberIdAndCourseCode(Long memberId,String courseCode) throws Exception{
		Long courseId=courseService.getIdByCode(courseCode);
		return memberCourseDao.existsByMemberIdAndCourseId(memberId,courseId);
	}
	
	@Override
	public boolean existsByMemberIdAndCourseId(Long memberId, Long courseId) throws Exception{
		return memberCourseDao.existsByMemberIdAndCourseId(memberId,courseId);
	}

	@Override
	public void checkMemberIdAndLessonId(Long memberId, Long lessonId) throws Exception {
		Long courseId=courseService.getIdByLessonId(lessonId);
		if(!memberCourseDao.existsByMemberIdAndCourseId(memberId, courseId)) 
			throw new ServiceException(OeErrorConstant.MEMBER_LESSON_NOT_EXIST);
	}

	@Override
	public void changLessonId(Long lessonId, Long id, Long courseId) {
		memberCourseDao.updateLessonIdByMemberIdAndCourseId(lessonId, id, courseId);
	}

	@Override
	public void changeLastViewTime(Long id, Long courseId) {
		memberCourseDao.updateLastViewTimeByMemberIdAndCourseId(new Date(),id,courseId);
	}

	@Override
	public void changeCompletionPercentage(Long memberId, Long courseId) {
		int lessonCount=lessonDao.calculateCountByCourseId(courseId);//课程的课节数
		
		Long id=memberCourseDao.getIdByMemberIdAndCourseId(memberId, courseId);
		int memberLessonCount=memberLessonDao.calculateCountByMemberCourseIdAndWatchedStatus(id, OeConstant.WATCHED);//已学完的课节数
		
		memberCourseDao.updateCompletionPercentageByByMemberIdAndCourseId(memberLessonCount*100/lessonCount,memberId,courseId);
	}

	@Override
	public void addNoteNumById(Long memberCourseId) {
		memberCourseDao.addNoteNumById(memberCourseId);
	}

	@Override
	public void subtractNoteNumById(Long memberCourseId) {
		memberCourseDao.subtractNoteNumById(memberCourseId);
	}

}

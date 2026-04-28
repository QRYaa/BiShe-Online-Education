package cn.tgxy.oledu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.oledu.dao.OeChapterDao;
import cn.tgxy.oledu.dao.OeCourseDao;
import cn.tgxy.oledu.dao.OeCourseExtDao;
import cn.tgxy.oledu.dao.OeLessonDao;
import cn.tgxy.oledu.dao.OeTeacherDao;
import cn.tgxy.oledu.dto.OeCourseDto;
import cn.tgxy.oledu.po.OeCourse;
import cn.tgxy.oledu.po.OeTeacher;
import cn.tgxy.oledu.service.OeChapterService;
import cn.tgxy.oledu.service.OeCourseService;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class OeCourseServiceImpl extends BsBaseServiceImpl<OeCourse, OeCourseDto> implements OeCourseService{

	@Autowired
	private OeCourseDao courseDao;
	
	@Autowired
	private OeCourseExtDao courseExtDao;
	
	@Autowired
	private OeLessonDao lessonDao;
	
	@Autowired
	private OeChapterDao chapterDao;
	
	@Autowired
	private OeTeacherDao teacherDao;
	
	@Autowired
	private OeChapterService chapterService;
		
	@Override
	public PageContent<OeCourseDto> findPageData(Long courseTypeId,Long teacherId,String name,Boolean published,Integer status,String courseTypeCode,Long memberId,String teacherCode,int pageNum, int pageSize) throws Exception{
		PageContent<OeCourse> pageContent=courseExtDao.findPageContent(courseTypeId, teacherId,name, published, status, courseTypeCode, memberId, teacherCode,pageNum, pageSize);
		return new PageContent<OeCourseDto>(transToDtoList(pageContent.getContent()),pageContent.getTotalItems(), pageContent.getPageNum(), pageSize);
	}
	
	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = courseDao;
	}
	
	@Override
	public void save(OeCourseDto dto) throws Exception {
		dto.setActualLearningCount(0);
		super.save(dto);
	}
	
	@Override
	public void update(OeCourseDto courseDto) throws Exception {
		OeCourse course=courseDao.findById(courseDto.getId()).orElse(null);
		if(course==null) return;
		courseDto.setActualLearningCount(course.getActualLearningCount());
		super.update(courseDto);
	}
	
	@Override
	public OeCourseDto transToDto(OeCourse course) throws Exception {
		OeCourseDto courseDto=super.transToDto(course);
		courseDto.setContent(null);
		if(course.getTeacherId() != null) {
			OeTeacher teacher=teacherDao.findById(course.getTeacherId()).orElse(null);
			if(teacher != null) {
				courseDto.setTeacherName(teacher.getName());
				courseDto.setTeacherCode(teacher.getCode());
			}
			
		}
		return courseDto;
	}
	
	@Override
	public OeCourseDto transToDtoDetail(OeCourse course) throws Exception {
		OeCourseDto courseDto=super.transToDtoDetail(course);
		if(courseDto==null) return null;
		courseDto.setChapterDtoList(chapterService.findByCourseId(courseDto.getId()));
		if(course.getTeacherId()!=null) {
			OeTeacher teacher=teacherDao.findById(course.getTeacherId()).orElse(null);
			if(teacher != null) {
				courseDto.setTeacherName(teacher.getName());
				courseDto.setTeacherCode(teacher.getCode());
			}
			
		}
		return courseDto;
	}
	
	@Override
	public OeCourseDto getSimple(Long id) throws Exception {
		OeCourse course=courseDao.findById(id).orElse(null);
		return transToDto(course);
	}
	
	@Override
	@Transactional
	public void changeStatus(OeCourseDto dto) {
		courseDao.updateStatusById(dto.getId(),dto.getStatus());
	}
	@Override
	@Transactional
	public void switchPaid(OeCourseDto dto) {
		courseDao.updatePaidById(dto.getId(),!dto.getPaid());
		
	}
	@Override
	@Transactional
	public void switchPublished(OeCourseDto dto) {
		courseDao.updatePublishedById(dto.getId(),!dto.getPublished());
	}

	//--------------------------------ToC--------------------------------
	
	@Override
	public OeCourseDto findByCodeAndEnable(String code) throws Exception {
		OeCourse course =courseDao.findByCodeAndPublished(code,true);
		return transToDtoDetail(course);
	}
	@Override
	public OeCourseDto findByIdAndEnable(Long id) throws Exception{
		OeCourse course =courseDao.findByIdAndPublished(id,true);
		return transToDtoDetail(course);
	}
	
	@Override
	public boolean existsByCodeAndEnable(String code) throws Exception {
		return courseDao.existsByCodeAndPublished(code,true);
	}
	
	@Override
	public boolean existsByIdAndEnable(Long id) throws Exception {
		return courseDao.existsByIdAndPublished(id,true);
	}
	
	@Override
	public Long getIdByCode(String code) {
		return courseDao.getIdByCode(code);
	}
	
	@Override
	public Long getIdByLessonId(Long lessonId) {
		Long chapterId=lessonDao.getChapterIdById(lessonId);
		return chapterDao.getCourseIdById(chapterId);
	}
}

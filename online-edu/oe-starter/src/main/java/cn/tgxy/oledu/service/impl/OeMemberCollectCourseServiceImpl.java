package cn.tgxy.oledu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.oledu.dao.OeMemberCollectCourseDao;
import cn.tgxy.oledu.dao.OeMemberCollectCourseExtDao;
import cn.tgxy.oledu.dto.OeMemberCollectCourseDto;
import cn.tgxy.oledu.po.OeMemberCollectCourse;
import cn.tgxy.oledu.service.OeCourseService;
import cn.tgxy.oledu.service.OeMemberCollectCourseService;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;

@Service
public class OeMemberCollectCourseServiceImpl extends BsBaseServiceImpl<OeMemberCollectCourse, OeMemberCollectCourseDto> implements OeMemberCollectCourseService{

	@Autowired
	private OeMemberCollectCourseDao memberCollectCourseDao;
	
	@Autowired
	private OeMemberCollectCourseExtDao memberCollectCourseExtDao;
		
	@Autowired
	private OeCourseService courseService;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = memberCollectCourseDao;
	}
	
	@Override
	public PageContent<OeMemberCollectCourseDto> findPageData(Long memberId, String courseName, int pageNum,int pageSize) throws Exception{
		PageContent<OeMemberCollectCourse> pageContent=memberCollectCourseExtDao.findPageContent(memberId, courseName, pageNum, pageSize);
		return new PageContent<OeMemberCollectCourseDto>(transToDtoList(pageContent.getContent()), pageContent.getTotalItems(), pageContent.getPageNum(), pageSize);
	}
	
	@Override
	public OeMemberCollectCourseDto transToDto(OeMemberCollectCourse memberCollectCourse) throws Exception {
		OeMemberCollectCourseDto memberCollectCourseDto=super.transToDto(memberCollectCourse);
		memberCollectCourseDto.setCourseDto(courseService.getSimple(memberCollectCourse.getCourseId()));
		return memberCollectCourseDto;
	}

	@Override
	public boolean existsByMemberIdAndCourseId(Long id, Long courseId) {
		return memberCollectCourseDao.existsByMemberIdAndCourseId(id,courseId);
	}

	@Override
	public void addByMemberIdAndCourseId(Long id, Long courseId) {
		if(!memberCollectCourseDao.existsByMemberIdAndCourseId(id,courseId)) {
			OeMemberCollectCourse memberCollectCourse=new OeMemberCollectCourse();
			memberCollectCourse.setMemberId(id);
			memberCollectCourse.setCourseId(courseId);
			memberCollectCourseDao.save(memberCollectCourse);
		}
	}

	@Override
	public void deleteByMemberIdAndCourseId(Long id, Long courseId) {
		memberCollectCourseDao.deleteByMemberIdAndCourseId(id,courseId);
	}
}

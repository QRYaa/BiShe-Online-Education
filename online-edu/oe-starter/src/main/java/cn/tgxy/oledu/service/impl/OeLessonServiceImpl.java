package cn.tgxy.oledu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.oledu.dao.OeLessonDao;
import cn.tgxy.oledu.dto.OeLessonDto;
import cn.tgxy.oledu.po.OeLesson;
import cn.tgxy.oledu.service.OeLessonService;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;

@Service
public class OeLessonServiceImpl extends BsBaseServiceImpl<OeLesson, OeLessonDto> implements OeLessonService{
	
	@Autowired
	private OeLessonDao lessonDao;
		
	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = lessonDao;
	}
	
	@Override
	public OeLessonDto transToDto(OeLesson lesson) throws Exception {
		OeLessonDto lessonDto=super.transToDto(lesson);
		lessonDto.setContent(null);
		return lessonDto;
	}
	
	@Override
	public List<OeLessonDto> findByChapterId(Long chapterId) throws Exception {
		List<OeLesson> lessonList=lessonDao.findByChapterId(chapterId);
		return transToDtoList(lessonList);
	}
		
}

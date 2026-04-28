package cn.tgxy.oledu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.oledu.dao.OeChapterDao;
import cn.tgxy.oledu.dto.OeChapterDto;
import cn.tgxy.oledu.po.OeChapter;
import cn.tgxy.oledu.service.OeChapterService;
import cn.tgxy.oledu.service.OeLessonService;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;

@Service
public class OeChapterServiceImpl extends BsBaseServiceImpl<OeChapter, OeChapterDto> implements OeChapterService{

	@Autowired
	private OeChapterDao chapterDao;
	
	@Autowired
	private OeLessonService lessonService;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = chapterDao;
	}
	
	@Override
	public OeChapterDto transToDto(OeChapter chapter) throws Exception {
		OeChapterDto chapterDto=super.transToDto(chapter);
		if(chapterDto==null) return null;
		chapterDto.setLessonDtoList(lessonService.findByChapterId(chapterDto.getId()));
		return chapterDto;
	}
	
	@Override
	public List<OeChapterDto> findByCourseId(Long courseId) throws Exception{
		return transToDtoList(chapterDao.findByCourseId(courseId));
	}

}

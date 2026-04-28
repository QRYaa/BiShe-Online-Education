package cn.tgxy.oledu.service;

import cn.tgxy.tgbase.service.BsBaseService;
import cn.tgxy.oledu.po.OeLesson;

import java.util.List;

import cn.tgxy.oledu.dto.OeLessonDto;

public interface OeLessonService extends BsBaseService<OeLesson, OeLessonDto>{

	List<OeLessonDto> findByChapterId(Long chapterId) throws Exception;

}

package cn.tgxy.oledu.service;

import cn.tgxy.tgbase.service.BsBaseService;
import cn.tgxy.oledu.po.OeChapter;

import java.util.List;

import cn.tgxy.oledu.dto.OeChapterDto;

public interface OeChapterService extends BsBaseService<OeChapter, OeChapterDto>{

	List<OeChapterDto> findByCourseId(Long courseId) throws Exception;

}

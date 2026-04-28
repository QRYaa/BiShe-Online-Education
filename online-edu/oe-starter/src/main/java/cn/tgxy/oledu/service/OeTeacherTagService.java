package cn.tgxy.oledu.service;

import cn.tgxy.tgbase.service.BsBaseService;
import cn.tgxy.oledu.po.OeTeacherTag;

import java.util.List;

import cn.tgxy.oledu.dto.OeTeacherTagDto;

public interface OeTeacherTagService extends BsBaseService<OeTeacherTag, OeTeacherTagDto>{

	List<OeTeacherTagDto> findByTeacherId(Long teacherId) throws Exception;

}

package cn.tgxy.oledu.service;

import cn.tgxy.tgbase.service.BsBaseService;
import cn.tgxy.oledu.po.OeTeacher;
import cn.tgxy.oledu.dto.OeTeacherDto;

public interface OeTeacherService extends BsBaseService<OeTeacher, OeTeacherDto>{

	OeTeacherDto findByCode(String code) throws Exception;

}

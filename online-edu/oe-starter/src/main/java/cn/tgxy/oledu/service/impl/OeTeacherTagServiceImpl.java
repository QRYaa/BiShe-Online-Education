package cn.tgxy.oledu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.oledu.dao.OeTeacherTagDao;
import cn.tgxy.oledu.dto.OeTeacherTagDto;
import cn.tgxy.oledu.po.OeTeacherTag;
import cn.tgxy.oledu.service.OeTeacherTagService;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;

@Service
public class OeTeacherTagServiceImpl extends BsBaseServiceImpl<OeTeacherTag, OeTeacherTagDto> implements OeTeacherTagService{

	@Autowired
	private OeTeacherTagDao teacherTagDao;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = teacherTagDao;
	}
	
	@Override
	public List<OeTeacherTagDto> findByTeacherId(Long teacherId) throws Exception{
		List<OeTeacherTag> teacherTagList=teacherTagDao.findByTeacherId(teacherId);
		return transToDtoList(teacherTagList);
	}
}

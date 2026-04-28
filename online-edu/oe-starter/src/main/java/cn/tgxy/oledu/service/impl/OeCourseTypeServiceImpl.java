package cn.tgxy.oledu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import cn.tgxy.oledu.po.OeCourseType;
import cn.tgxy.oledu.dto.OeCourseTypeDto;
import cn.tgxy.oledu.dao.OeCourseTypeDao;
import cn.tgxy.oledu.service.OeCourseTypeService;
import jakarta.annotation.PostConstruct;

@Service
public class OeCourseTypeServiceImpl extends BsBaseServiceImpl<OeCourseType, OeCourseTypeDto> implements OeCourseTypeService{

	@Autowired
	private OeCourseTypeDao oeCourseTypeDao;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = oeCourseTypeDao;
	}
}

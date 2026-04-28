package cn.tgxy.oledu.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.oledu.constant.OeErrorConstant;
import cn.tgxy.oledu.dao.OeTeacherDao;
import cn.tgxy.oledu.dao.OeTeacherTagMapDao;
import cn.tgxy.oledu.dto.OeTeacherDto;
import cn.tgxy.oledu.dto.OeTeacherTagDto;
import cn.tgxy.oledu.po.OeTeacher;
import cn.tgxy.oledu.po.OeTeacherTagMap;
import cn.tgxy.oledu.service.OeTeacherService;
import cn.tgxy.oledu.service.OeTeacherTagService;
import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class OeTeacherServiceImpl extends BsBaseServiceImpl<OeTeacher, OeTeacherDto> implements OeTeacherService{

	@Autowired
	private OeTeacherDao teacherDao;
	
	@Autowired
	private OeTeacherTagMapDao teacherTagMapDao;
	
	@Autowired
	private OeTeacherTagService teacherTagService;
	
	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = teacherDao;
	}
	
	@Override
	public OeTeacherDto transToDto(OeTeacher teacher) throws Exception {
		OeTeacherDto teacherDto=super.transToDto(teacher);
		List<OeTeacherTagMap> teacherTagMapList = teacherTagMapDao.findByTeacherId(teacher.getId());
		List<Long> tagIdList = teacherTagMapList.stream().map(teacherTagMap->teacherTagMap.getTagId()).collect(Collectors.toList());
		teacherDto.setTagIdList(tagIdList);
		return teacherDto;
	}
	
	@Override
	public OeTeacherDto transToDtoDetail(OeTeacher teacher) throws Exception {
		OeTeacherDto teacherDto=super.transToDtoDetail(teacher);
		List<OeTeacherTagDto> teacherTagDtoList=teacherTagService.findByTeacherId(teacher.getId());
		teacherDto.setTagDtoList(teacherTagDtoList);
		List<Long> tagIdList = teacherTagDtoList.stream().map(teacherTagDto->teacherTagDto.getId()).collect(Collectors.toList());
		teacherDto.setTagIdList(tagIdList);
		return teacherDto;
	}
	
	@Override
	@Transactional
	public void save(OeTeacherDto dto) throws Exception {
		String code = dto.getCode();
		if (StringUtils.isNotBlank(code)) {// 判断编码是否有重复
			if (teacherDao.existsByCode(code)) {
				throw new ServiceException(OeErrorConstant.CODE_EXIST);
			}
		}
		super.save(dto);
		
		dto.getTagIdList().forEach(tagId->{
			OeTeacherTagMap teacherTagMap=new OeTeacherTagMap();
			teacherTagMap.setTeacherId(dto.getId());
			teacherTagMap.setTagId(tagId);
			teacherTagMapDao.save(teacherTagMap);
		});
	}
	
	@Override
	@Transactional
	public void update(OeTeacherDto teacherDto) throws Exception {
		OeTeacher teacher = teacherDao.findById(teacherDto.getId()).orElse(null);

		if (teacher == null) {
			return;
		}
		
		String code = teacherDto.getCode();
		if (StringUtils.isNotBlank(code) && !code.equals(teacher.getCode()) && teacherDao.existsByCode(code)) {
			throw new ServiceException(OeErrorConstant.CODE_EXIST);
		}
		
		super.update(teacherDto);
		
		List<OeTeacherTagMap> dbTeacherTagMapList=teacherTagMapDao.findByTeacherId(teacherDto.getId());
		Set<Long> dbTagIdSet=new HashSet<>();
		dbTeacherTagMapList.forEach(ttm->{
			dbTagIdSet.add(ttm.getTagId());
		});
		teacherDto.getTagIdList().forEach(tagId->{
			if(dbTagIdSet.contains(tagId)) {
				dbTagIdSet.remove(tagId);
			}
			else {
				OeTeacherTagMap teacherTagMap=new OeTeacherTagMap();
				teacherTagMap.setTeacherId(teacherDto.getId());
				teacherTagMap.setTagId(tagId);
				teacherTagMapDao.save(teacherTagMap);
			}
		});
		dbTagIdSet.forEach(tagId->{
			teacherTagMapDao.deleteByTeacherIdAndTagId(teacherDto.getId(),tagId);
		});
	}

	@Override
	public OeTeacherDto findByCode(String code) throws Exception {
		OeTeacher teacher=teacherDao.findByCode(code);
		teacher.setTel(null);
		return transToDtoDetail(teacher);
	}
}

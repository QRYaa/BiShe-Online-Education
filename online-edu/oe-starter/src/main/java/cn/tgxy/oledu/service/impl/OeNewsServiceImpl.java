package cn.tgxy.oledu.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.oledu.constant.OeErrorConstant;
import cn.tgxy.oledu.dao.OeNewsDao;
import cn.tgxy.oledu.dto.OeNewsDto;
import cn.tgxy.oledu.po.OeNews;
import cn.tgxy.oledu.service.OeNewsService;
import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;

@Service
public class OeNewsServiceImpl extends BsBaseServiceImpl<OeNews, OeNewsDto> implements OeNewsService{

	@Autowired
	private OeNewsDao newsDao;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = newsDao;
	}
	
	@Override
	public OeNewsDto transToDto(OeNews news) throws Exception {
		OeNewsDto newsDto=super.transToDto(news);
		newsDto.setContent(null);
		return newsDto;
	}
	
	@Override
	public void save(OeNewsDto dto) throws Exception {
		if(StringUtils.isNotEmpty(dto.getCode()) && newsDao.existsByCode(dto.getCode())) {
			throw new ServiceException(OeErrorConstant.CODE_EXIST);
		}
		super.save(dto);
	}
	
	@Override
	public void update(OeNewsDto newsDto) throws Exception {
		OeNews news=newsDao.findById(newsDto.getId()).orElse(null);
		if(StringUtils.isNotEmpty(newsDto.getCode())&&!newsDto.getCode().equals(news.getCode())&&newsDao.existsByCode(newsDto.getCode())) {
			throw new ServiceException(OeErrorConstant.CODE_EXIST);
		}
		super.update(newsDto);
	}
	
	//---------------------------ToC端--------------------------------

	@Override
	public OeNewsDto findByCodeAndEnable(String code) throws Exception {
		OeNews news=newsDao.findByCodeAndPublished(code,true);
		return transToDtoDetail(news);
	}
	
}

package cn.tgxy.oledu.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.oledu.constant.OeConstant;
import cn.tgxy.oledu.dao.OeBannerDao;
import cn.tgxy.oledu.dto.OeBannerDto;
import cn.tgxy.oledu.po.OeBanner;
import cn.tgxy.oledu.service.OeBannerService;
import cn.tgxy.tgbase.constant.BsConstant;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;

@Service
public class OeBannerServiceImpl extends BsBaseServiceImpl<OeBanner, OeBannerDto> implements OeBannerService{

	private static Logger log = LoggerFactory.getLogger(OeBannerServiceImpl.class);
	
	@Autowired
	private OeBannerDao bannerDao;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = bannerDao;
	}
	
	@Override
	public void save(OeBannerDto dto) throws Exception {
		Date now = new Date();
		Date startTime=dto.getStartTime();
		Date endTime=dto.getEndTime();
		if(now.after(endTime)) dto.setStatus(OeConstant.BANNER_OUTDATED);
		else if(now.before(startTime)) dto.setStatus(OeConstant.BANNER_PENDING);
		else dto.setStatus(OeConstant.BANNER_VALID);
		super.save(dto);
	}
	
	@Override
	public void update(OeBannerDto dto) throws Exception {
		Date now = new Date();
		Date startTime=dto.getStartTime();
		Date endTime=dto.getEndTime();
		if(now.after(endTime)) dto.setStatus(OeConstant.BANNER_OUTDATED);
		else if(now.before(startTime)) dto.setStatus(OeConstant.BANNER_PENDING);
		else dto.setStatus(OeConstant.BANNER_VALID);
		super.update(dto);
	}
	
	@Override
	public void updateEnableTrueById(Long id) {
		bannerDao.updateEnableById(BsConstant.VALID,id);
	}
	
	@Override
	public void updateEnableFalseById(Long id) {
		bannerDao.updateEnableById(BsConstant.INVALID,id);
	}
	
	@Override
	public void updateStatus(StringBuilder strB) {
		int outdateNum=0;
		int validNum=0;
		Date now=new Date();
		List<OeBanner> bannerList=bannerDao.findByStatus(OeConstant.BANNER_VALID);
		for(OeBanner b:bannerList) {
			if(now.after(b.getEndTime())) {
				bannerDao.updateStatusById(OeConstant.BANNER_OUTDATED, b.getId());
				log.info("【"+b.getName()+"】横幅已过期");
				outdateNum++;
			}
		}
		
		List<OeBanner> bannerList2=bannerDao.findByStatus(OeConstant.BANNER_PENDING);
		for(OeBanner b:bannerList2) {
			if(now.after(b.getEndTime())) {
				bannerDao.updateStatusById(OeConstant.BANNER_OUTDATED, b.getId());
				log.info("【"+b.getName()+"】横幅已过期");
				outdateNum++;
			}
			else if(now.after(b.getStartTime())) {
				bannerDao.updateStatusById(OeConstant.BANNER_VALID, b.getId());
				log.info("【"+b.getName()+"】横幅到了开始时间");
				validNum++;
			}
		}
		strB.append(String.format("过期条数【%s】;开始条数【%s】", outdateNum,validNum));
	}
	//----------------------------------ToC端api------------------------------------------------
	@Override
	public List<OeBannerDto> findEnabledAndValid() throws Exception {
		List<OeBanner> bannerList = bannerDao.findByEnableAndStatus(BsConstant.VALID,OeConstant.BANNER_VALID);
		return transToDtoList(bannerList);
	}
}

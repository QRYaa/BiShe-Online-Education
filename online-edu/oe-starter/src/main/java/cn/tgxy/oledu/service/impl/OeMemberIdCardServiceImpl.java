package cn.tgxy.oledu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import cn.tgxy.oledu.po.OeMemberIdCard;
import cn.tgxy.oledu.dto.OeMemberIdCardDto;
import cn.tgxy.oledu.dao.OeMemberDao;
import cn.tgxy.oledu.dao.OeMemberIdCardDao;
import cn.tgxy.oledu.service.OeMemberAuthService;
import cn.tgxy.oledu.service.OeMemberIdCardService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class OeMemberIdCardServiceImpl extends BsBaseServiceImpl<OeMemberIdCard, OeMemberIdCardDto> implements OeMemberIdCardService{

	@Autowired
	private OeMemberIdCardDao memberIdCardDao;
	
	@Autowired
	private OeMemberDao memberDao;
	
	@Autowired
	private OeMemberAuthService memberAuthService;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = memberIdCardDao;
	}
	
	@Override
	public OeMemberIdCardDto findByMemberId(Long memberId) throws Exception {
		OeMemberIdCard memberIdCard=memberIdCardDao.findByMemberId(memberId);
		OeMemberIdCardDto memberIdCardDto=transToDtoDetail(memberIdCard);
		return memberIdCardDto;
	}
	
	@Override
	@Transactional
	public void deleteById(Long id) throws Exception {
		OeMemberIdCard memberIdCard=memberIdCardDao.findById(id).orElse(null);
		if(memberIdCard!=null) {
			//更新学员实名状态
			memberDao.updateRealNameAuthenticatedById(false, memberIdCard.getMemberId());
			
			super.deleteById(id);
			
			//更新会话
			memberAuthService.updateSessionByMemberId(memberIdCard.getMemberId());
		}
	}
	
	//--------------------------------ToC--------------------------------
	@Override
	public OeMemberIdCardDto findMaskInfoByMemberId(Long memberId) throws Exception {
		OeMemberIdCard memberIdCard=memberIdCardDao.findByMemberId(memberId);
		OeMemberIdCardDto memberIdCardDto=transToDtoDetail(memberIdCard);
		memberIdCardDto.maskSensitiveInfo();
		return memberIdCardDto;
	}
	
	@Override
	@Transactional
	public void unbindIdCard(Long memberId) throws Exception {
		OeMemberIdCard memberIdCard=memberIdCardDao.findByMemberId(memberId);
		if(memberIdCard!=null) {
			//更新学员实名状态
			memberDao.updateRealNameAuthenticatedById(false, memberId);
			
			memberIdCardDao.deleteById(memberIdCard.getId());
			
			//更新会话
			memberAuthService.updateSessionByMemberId(memberId);
		}
	}

}

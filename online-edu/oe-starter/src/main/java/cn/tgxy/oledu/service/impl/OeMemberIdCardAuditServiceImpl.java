package cn.tgxy.oledu.service.impl;

import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.oledu.constant.OeConstant;
import cn.tgxy.oledu.constant.OeErrorConstant;
import cn.tgxy.oledu.dao.OeMemberDao;
import cn.tgxy.oledu.dao.OeMemberIdCardAuditDao;
import cn.tgxy.oledu.dao.OeMemberIdCardAuditExtDao;
import cn.tgxy.oledu.dao.OeMemberIdCardDao;
import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.dto.OeMemberIdCardAuditDto;
import cn.tgxy.oledu.po.OeMember;
import cn.tgxy.oledu.po.OeMemberIdCard;
import cn.tgxy.oledu.po.OeMemberIdCardAudit;
import cn.tgxy.oledu.service.OeMemberAuthService;
import cn.tgxy.oledu.service.OeMemberIdCardAuditService;
import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.BsUserDao;
import cn.tgxy.tgbase.dto.BsUserDto;
import cn.tgxy.tgbase.po.BsUser;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class OeMemberIdCardAuditServiceImpl extends BsBaseServiceImpl<OeMemberIdCardAudit, OeMemberIdCardAuditDto>
		implements OeMemberIdCardAuditService {
	@Autowired
	private OeMemberIdCardAuditDao memberIdCardAuditDao;

	@Autowired
	private OeMemberIdCardAuditExtDao memberIdCardAuditExtDao;

	@Autowired
	private OeMemberIdCardDao memberIdCardDao;

	@Autowired
	private OeMemberDao memberDao;

	@Autowired
	private BsUserDao userDao;

	@Autowired
	private OeMemberAuthService memberAuthService;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = memberIdCardAuditDao;
	}

	@Override
	public PageContent<OeMemberIdCardAuditDto> findPageData(String memberCode, Integer status, String idCardNumber,
			String idCardRealName, Integer pageNum, Integer pageSize) throws Exception {
		PageContent<OeMemberIdCardAudit> page = memberIdCardAuditExtDao.findPageContent(memberCode, status,
				idCardNumber, idCardRealName, pageNum, pageSize);
		PageContent<OeMemberIdCardAuditDto> pageData = new PageContent<>(transToDtoList(page.getContent()),
				page.getTotalItems(), page.getTotalPages(), pageNum, pageSize);
		return pageData;
	}

	@Override
	public OeMemberIdCardAuditDto transToDto(OeMemberIdCardAudit entity) throws Exception {
		OeMemberIdCardAuditDto memberIdCardAuditDto = super.transToDto(entity);
		if (memberIdCardAuditDto == null)
			return memberIdCardAuditDto;
		OeMember member = memberDao.findById(memberIdCardAuditDto.getMemberId()).orElse(null);
		if (member != null) {
			memberIdCardAuditDto.setMemberCode(member.getCode());
			memberIdCardAuditDto.setMemberName(member.getName());
			memberIdCardAuditDto.setMemberAvatar(member.getAvatar());
		}
		if (memberIdCardAuditDto.getAuditorId() != null) {
			BsUser user = userDao.findById(memberIdCardAuditDto.getAuditorId()).orElse(null);
			if (user != null) {
				memberIdCardAuditDto.setAuditorName(user.getUsername());
			}
		}
		return memberIdCardAuditDto;
	}

	@Override
	public OeMemberIdCardAuditDto transToDtoDetail(OeMemberIdCardAudit entity) throws Exception {
		OeMemberIdCardAuditDto memberIdCardAuditDto = super.transToDto(entity);
		if (memberIdCardAuditDto == null)
			return memberIdCardAuditDto;
		OeMember member = memberDao.findById(memberIdCardAuditDto.getMemberId()).orElse(null);
		if (member != null) {
			memberIdCardAuditDto.setMemberCode(member.getCode());
			memberIdCardAuditDto.setMemberName(member.getName());
			memberIdCardAuditDto.setMemberAvatar(member.getAvatar());
		}
		if (memberIdCardAuditDto.getAuditorId() != null) {
			BsUser user = userDao.findById(memberIdCardAuditDto.getAuditorId()).orElse(null);
			if (user != null) {
				memberIdCardAuditDto.setAuditorName(user.getUsername());
			}
		}
		return memberIdCardAuditDto;
	}

	@Override
	@Transactional
	public void passAudit(BsUserDto currentUseDto, OeMemberIdCardAuditDto auditDto) throws Exception {
		OeMemberIdCardAudit dbMemberIdCardAudit = memberIdCardAuditDao.findById(auditDto.getId()).orElse(null);

		// 身份证号是否为空,是否存在身份证号
		if (dbMemberIdCardAudit.getIdCardNumber() == null) {
			throw new ServiceException(OeErrorConstant.ID_CARD_EXIST);
		}
		if (memberIdCardDao.existsByNumberButMemberId(dbMemberIdCardAudit.getIdCardNumber(),dbMemberIdCardAudit.getMemberId())) {
			throw new ServiceException(OeErrorConstant.ID_CARD_EXIST);
		}

		if (dbMemberIdCardAudit != null && dbMemberIdCardAudit.getStatus() == OeConstant.AUDIT_STATUS_PENDING) {
			// 记录审核人，审核状态等信息
			dbMemberIdCardAudit.setAuditorId(currentUseDto.getId());
			dbMemberIdCardAudit.setAuditTime(new Date());
			dbMemberIdCardAudit.setStatus(OeConstant.AUDIT_STATUS_SUCCESS);
			dbMemberIdCardAudit.setOpinion(auditDto.getOpinion());
			// 修改memberIdCard
			OeMemberIdCard dbMemberIdCard = memberIdCardDao.findByMemberId(dbMemberIdCardAudit.getMemberId());
			if (dbMemberIdCard == null)
				dbMemberIdCard = new OeMemberIdCard();
			dbMemberIdCard.coverFromAudit(dbMemberIdCardAudit);
			memberIdCardDao.save(dbMemberIdCard);
			// 修改member认证状态
			memberDao.updateRealNameAuthenticatedById(true, dbMemberIdCard.getMemberId());
			// 更新会话
			memberAuthService.updateSessionByMemberId(dbMemberIdCardAudit.getMemberId());

			memberIdCardAuditDao.save(dbMemberIdCardAudit);
		}
	}

	@Override
	public void rejectAudit(BsUserDto currentUseDto, OeMemberIdCardAuditDto auditDto) throws Exception {
		OeMemberIdCardAudit dbMemberIdCardAudit = memberIdCardAuditDao.findById(auditDto.getId()).orElse(null);
		if (dbMemberIdCardAudit != null && dbMemberIdCardAudit.getStatus() == OeConstant.AUDIT_STATUS_PENDING) {
			// 记录审核人，审核状态等信息
			dbMemberIdCardAudit.setAuditorId(currentUseDto.getId());
			dbMemberIdCardAudit.setAuditTime(new Date());
			dbMemberIdCardAudit.setStatus(OeConstant.AUDIT_STATUS_FAIL);
			dbMemberIdCardAudit.setOpinion(auditDto.getOpinion());

			memberIdCardAuditDao.save(dbMemberIdCardAudit);
		}
	}

	// ------------------------------------------------------ToC------------------------------------------------------
	@Override
	public void submitAudit(OeMemberDto memberDto, OeMemberIdCardAuditDto memberIdCardAuditDto) throws Exception {
		Integer idCardType = memberIdCardAuditDto.getIdCardType();
		String idCardNumber = memberIdCardAuditDto.getIdCardNumber();
		String idCardRealName = memberIdCardAuditDto.getIdCardRealName();
		String idCardFrontImageUrl = memberIdCardAuditDto.getIdCardFrontImageUrl();
		String idCardBackImageUrl = memberIdCardAuditDto.getIdCardBackImageUrl();
		// 验证参数
		if (StringUtils.isAnyEmpty(idCardNumber, idCardRealName, idCardFrontImageUrl, idCardBackImageUrl)
				|| idCardType == null) {
			throw new ServiceException(OeErrorConstant.PARAM_NOT_EXISTS);
		}
		// 验证身份证
		if (!Pattern.matches(OeConstant.REGEX_IDCARD, idCardNumber)) {
			throw new ServiceException(OeErrorConstant.IDCARD_FORMAT_ERROR);
		}

		if (memberIdCardAuditDto.getId() != null) {
			// 修改资料
			OeMemberIdCardAudit memberIdCardAudit = memberIdCardAuditDao.findById(memberIdCardAuditDto.getId())
					.orElse(null);
			if (memberIdCardAudit == null) {
				return;
			}
			// 验证会话身份
			if (memberIdCardAudit.getMemberId() != memberDto.getId()) {
				return;
			}
			// 驳回状态的审核资料才可以修改
			if (memberIdCardAudit.getStatus() != OeConstant.AUDIT_STATUS_FAIL) {
				return;
			}
			memberIdCardAudit.setStatus(OeConstant.AUDIT_STATUS_PENDING);// 状态变化
			memberIdCardAudit.setIdCardAddress(memberIdCardAuditDto.getIdCardAddress());
			memberIdCardAudit.setIdCardBackImageUrl(idCardBackImageUrl);
			memberIdCardAudit.setIdCardBirthday(memberIdCardAuditDto.getIdCardBirthday());
			memberIdCardAudit.setIdCardExpireDate(memberIdCardAuditDto.getIdCardExpireDate());
			memberIdCardAudit.setIdCardFrontImageUrl(idCardFrontImageUrl);
			memberIdCardAudit.setIdCardGender(memberIdCardAuditDto.getIdCardGender());
			memberIdCardAudit.setIdCardIssueDate(memberIdCardAuditDto.getIdCardIssueDate());
			memberIdCardAudit.setIdCardNumber(idCardNumber);
			memberIdCardAudit.setIdCardRealName(idCardRealName);
			memberIdCardAudit.setIdCardType(idCardType);

			memberIdCardAudit.setOpinion(null);// 清空审批意见
			memberIdCardAuditDao.save(memberIdCardAudit);
		}

		else {
			// 创建资料
			// 已存在待审核或驳回的资料不允许再创建
			if (memberIdCardAuditDao.existsByMemberIdAndStatus(memberDto.getId(), OeConstant.AUDIT_STATUS_PENDING)) {
				throw new ServiceException(OeErrorConstant.CONTAINS_PENDING_AUDIT);
			}
			;
			if (memberIdCardAuditDao.existsByMemberIdAndStatus(memberDto.getId(), OeConstant.AUDIT_STATUS_FAIL)) {
				throw new ServiceException(OeErrorConstant.CONTAINS_PENDING_AUDIT);
			}
			;
			memberIdCardAuditDto.setId(null);
			memberIdCardAuditDto.setMemberId(memberDto.getId());
			memberIdCardAuditDto.setStatus(OeConstant.AUDIT_STATUS_PENDING);
			memberIdCardAuditDto.setOpinion(null);
			memberIdCardAuditDto.setAuditorId(null);
			memberIdCardAuditDto.setCreateTime(new Date());
			memberIdCardAuditDto.setAuditTime(null);

			memberIdCardAuditDto.setOpinion(null);// 清空审批意见
			super.save(memberIdCardAuditDto);
		}
	}

	@Override
	public OeMemberIdCardAuditDto findByMemberIdAndNotSuccess(Long memberId) throws Exception {
		return transToDtoDetail(memberIdCardAuditDao.findByMemberIdAndNotSuccess(memberId));
	}

}

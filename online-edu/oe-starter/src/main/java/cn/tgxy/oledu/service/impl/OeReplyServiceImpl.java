package cn.tgxy.oledu.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;

import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import cn.tgxy.oledu.po.OeMember;
import cn.tgxy.oledu.po.OeReply;
import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.dto.OeReplyDto;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.tgxy.oledu.constant.OeErrorConstant;
import cn.tgxy.oledu.dao.OeMemberDao;
import cn.tgxy.oledu.dao.OeNoteDao;
import cn.tgxy.oledu.dao.OeReplyDao;
import cn.tgxy.oledu.dao.OeReplyExtDao;
import cn.tgxy.oledu.service.OeReplyService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class OeReplyServiceImpl extends BsBaseServiceImpl<OeReply, OeReplyDto> implements OeReplyService {

	@Autowired
	private OeReplyDao replyDao;

	@Autowired
	private OeReplyExtDao replyExtDao;

	@Autowired
	private OeMemberDao memberDao;
	
	@Autowired
	private OeNoteDao noteDao;
	
	@Autowired
	private SensitiveWordBs sensitiveWordBs;
	
	private Snowflake snowflake = IdUtil.getSnowflake();

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = replyDao;
	}

	@Override
	public PageContent<OeReplyDto> findPageData(Long noteId, String trackId, String senderCode, Boolean enable,
			Integer pageNum, Integer pageSize) throws Exception {
		PageContent<OeReply> pageContent = replyExtDao.findPageContent(noteId, trackId, senderCode, enable, pageNum,
				pageSize);
		return new PageContent<OeReplyDto>(transToDtoList(pageContent.getContent()), pageContent.getTotalItems(),
				pageNum, pageSize);
	}

	@Override
	public OeReplyDto transToDto(OeReply reply) throws Exception {
		OeReplyDto replyDto = super.transToDto(reply);
		OeMember sender = memberDao.findById(replyDto.getSenderId()).orElse(null);
		if (sender != null) {
			replyDto.setSenderName(sender.getName());
			replyDto.setSenderAvatar(sender.getAvatar());
		}
		if(reply.getReceiverId()!=null) {
			OeMember receiver = memberDao.findById(replyDto.getReceiverId()).orElse(null);
			if (receiver != null) {
				replyDto.setReceiverName(receiver.getName());
				replyDto.setReceiverAvatar(receiver.getAvatar());
			}
		}
		return replyDto;
	}

	@Override
	public OeReplyDto transToDtoDetail(OeReply reply) throws Exception {
		OeReplyDto replyDto = super.transToDto(reply);
		OeMember sender = memberDao.findById(replyDto.getSenderId()).orElse(null);
		OeMember receiver = memberDao.findById(replyDto.getReceiverId()).orElse(null);
		if (sender != null) {
			replyDto.setSenderName(sender.getName());
			replyDto.setSenderAvatar(sender.getAvatar());
		}
		if (receiver != null) {
			replyDto.setReceiverName(receiver.getName());
			replyDto.setReceiverAvatar(receiver.getAvatar());
		}
		return replyDto;
	}

	@Override
	public void switchEnable(OeReplyDto replyDto) {
		replyDao.updateEnableById(!replyDto.getEnable(), replyDto.getId());
	}

	// ----------------------------------------------------ToC----------------------------------------------------

	@Transactional
	@Override
	public OeReplyDto createReply(OeMemberDto memberDto, OeReplyDto replyDto) throws Exception {
		// 检测有无敏感词
		if (sensitiveWordBs.contains(replyDto.getContent())) {
			throw new ServiceException(OeErrorConstant.CONTAINS_SENSITIVE_WORDS);
		}
		if(!noteDao.existsById(replyDto.getNoteId())) throw new ServiceException(OeErrorConstant.NOTE_NOT_EXISTS);
		
		OeReply reply=new OeReply();
		reply.setNoteId(replyDto.getNoteId());
		
		if(replyDto.getRoot()) {
			reply.setTrackId(snowflake.nextIdStr());
		}
		else {
			String trackId=replyDto.getTrackId();
			if(!replyDao.existsByTrackIdAndEnable(trackId,true)) {
				throw new ServiceException(OeErrorConstant.REPLY_TRACK_NOT_EXISTS);
			}
			reply.setTrackId(trackId);
		}
		
		reply.setSenderId(memberDto.getId());
		
		if(!replyDto.getRoot()&&!memberDao.existsById(replyDto.getReceiverId())) {
			throw new ServiceException(OeErrorConstant.MEMBER_NOT_EXISTS);
		}
		
		if(!replyDto.getRoot()) {
			reply.setReceiverId(replyDto.getReceiverId());	
		}
		
		if(StringUtils.isEmpty(replyDto.getContent())) {
			throw new ServiceException(OeErrorConstant.CONTENT_EMPTY);
		}
		reply.setContent(replyDto.getContent().trim());
		
		reply.setCreateTime(new Date());
		reply.setRoot(replyDto.getRoot());
		reply.setEnable(true);

		replyDao.save(reply);
		
		//更新note的回复数
		noteDao.addReplyNumById(replyDto.getNoteId());
		
		return transToDto(reply);
	}

	@Override
	public void enableNotByIdAndSenderId(Long id, Long senderId) throws Exception {
		replyDao.enableNotByIdAndSenderId(id,senderId);
	}

}

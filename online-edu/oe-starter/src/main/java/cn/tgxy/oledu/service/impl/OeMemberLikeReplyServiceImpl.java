package cn.tgxy.oledu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.oledu.dao.OeMemberLikeReplyDao;
import cn.tgxy.oledu.dao.OeReplyDao;
import cn.tgxy.oledu.dto.OeMemberLikeReplyDto;
import cn.tgxy.oledu.po.OeMemberLikeReply;
import cn.tgxy.oledu.service.OeMemberLikeReplyService;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class OeMemberLikeReplyServiceImpl extends BsBaseServiceImpl<OeMemberLikeReply, OeMemberLikeReplyDto> implements OeMemberLikeReplyService{

	@Autowired
	private OeMemberLikeReplyDao memberLikeReplyDao;
	
	@Autowired
	private OeReplyDao replyDao;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = memberLikeReplyDao;
	}

	@Transactional
	@Override
	public void saveByMemberIdAndReplyId(Long id, Long replyId) {
		if(!memberLikeReplyDao.existsByMemberIdAndReplyId(id,replyId)) {
			Long noteId=replyDao.getNoteIdById(replyId);
			OeMemberLikeReply memberLikeReply=new OeMemberLikeReply();
			memberLikeReply.setMemberId(id);
			memberLikeReply.setReplyId(replyId);
			memberLikeReply.setNoteId(noteId);
			memberLikeReplyDao.save(memberLikeReply);
			
			replyDao.addLikeNumById(replyId);
		}
	}

	@Transactional
	@Override
	public void deleteByMemberIdAndReplyId(Long id, Long replyId) {
		if(memberLikeReplyDao.existsByMemberIdAndReplyId(id,replyId)) {
			memberLikeReplyDao.deleteByMemberIdAndReplyId(id,replyId);
			replyDao.subtractLikeNumById(replyId);
		}
	}
}

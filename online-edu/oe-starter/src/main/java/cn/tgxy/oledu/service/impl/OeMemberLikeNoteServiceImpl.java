package cn.tgxy.oledu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.oledu.dao.OeMemberLikeNoteDao;
import cn.tgxy.oledu.dao.OeNoteDao;
import cn.tgxy.oledu.dto.OeMemberLikeNoteDto;
import cn.tgxy.oledu.po.OeMemberLikeNote;
import cn.tgxy.oledu.service.OeMemberLikeNoteService;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.transaction.Transactional;

@Service
public class OeMemberLikeNoteServiceImpl extends BsBaseServiceImpl<OeMemberLikeNote, OeMemberLikeNoteDto> implements OeMemberLikeNoteService {

	@Autowired
	private OeMemberLikeNoteDao memberLikeNoteDao;
	
	@Autowired
	private OeNoteDao noteDao;
	
	@Override
	public void initBaseDao() {
		this.baseDao=memberLikeNoteDao;	
	}

	@Transactional
	@Override
	public void saveByMemberIdAndNoteId(Long id, Long noteId) {
		if(!memberLikeNoteDao.existsByMemberIdAndNoteId(id,noteId)) {
			Long lessonId=noteDao.getLessonIdById(noteId);
			OeMemberLikeNote memberLikeNote=new OeMemberLikeNote();
			memberLikeNote.setMemberId(id);
			memberLikeNote.setNoteId(noteId);
			memberLikeNote.setLessonId(lessonId);
			memberLikeNoteDao.save(memberLikeNote);
			
			//更改note记录的likeNum
			noteDao.addLikeNumById(noteId);
		}
	}

	@Transactional
	@Override
	public void deleteByMemberIdAndNoteId(Long id, Long noteId) {
		if(memberLikeNoteDao.existsByMemberIdAndNoteId(id,noteId)) {
			memberLikeNoteDao.deleteByMemberIdAndNoteId(id,noteId);
			
			//更改note记录的likeNum
			noteDao.subtractLikeNumById(noteId);
		}
	}

	@Override
	public boolean existsByMemberIdAndNoteId(Long id, Long noteId) {
		return memberLikeNoteDao.existsByMemberIdAndNoteId(id, noteId);
	}
	
}

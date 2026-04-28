package cn.tgxy.oledu.dao;

import cn.tgxy.tgbase.dao.BsBaseDao;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.tgxy.oledu.po.OeReply;

public interface OeReplyDao extends BsBaseDao<OeReply, Long>{
	@Transactional
	@Modifying
	@Query("UPDATE OeReply r SET r.enable = :b WHERE r.id = :id")
	void updateEnableById(boolean b, Long id);

	boolean existsByTrackIdAndEnable(String trackId, boolean b);

	@Query("SELECT r.noteId FROM OeReply r WHERE r.id = :replyId")
	Long getNoteIdById(Long replyId);

	@Transactional
	@Modifying
	@Query("UPDATE OeReply r SET r.likeNum = IFNULL(r.likeNum,0) + 1 WHERE r.id = :replyId")
	void addLikeNumById(Long replyId);

	@Transactional
	@Modifying
	@Query("UPDATE OeReply r SET r.likeNum = IFNULL(r.likeNum,1) - 1 WHERE r.id = :replyId")
	void subtractLikeNumById(Long replyId);

	@Transactional
	@Modifying
	@Query("UPDATE OeReply r SET r.enable = false WHERE r.id = :id AND r.senderId = :senderId")
	void enableNotByIdAndSenderId(Long id, Long senderId);
}

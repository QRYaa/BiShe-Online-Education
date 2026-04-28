package cn.tgxy.oledu.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import cn.tgxy.oledu.po.OeMemberLikeReply;
import cn.tgxy.tgbase.dao.BsBaseDao;

public interface OeMemberLikeReplyDao extends BsBaseDao<OeMemberLikeReply, Long> {

	@Query("SELECT mlr.replyId FROM OeMemberLikeReply mlr WHERE mlr.memberId = :memberId AND mlr.noteId = :noteId")
	List<String> getReplyIdsByMemberIdAndNoteId(Long memberId, Long noteId);

	boolean existsByMemberIdAndReplyId(Long id, Long replyId);

	void deleteByMemberIdAndReplyId(Long id, Long replyId);

}

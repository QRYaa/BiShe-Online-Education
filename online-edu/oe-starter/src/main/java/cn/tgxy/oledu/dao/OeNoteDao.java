package cn.tgxy.oledu.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.tgxy.oledu.po.OeNote;
import cn.tgxy.tgbase.dao.BsBaseDao;
import jakarta.transaction.Transactional;

public interface OeNoteDao extends BsBaseDao<OeNote, Long>{
	@Transactional
	@Modifying
	@Query("UPDATE OeNote n SET n.enable = :b WHERE n.id = :id")
	void updateEnableById(boolean b, Long id);

	@Query("SELECT n.lessonId FROM OeNote n WHERE n.id = :id")
	Long getLessonIdById(Long id);

	@Transactional
	@Modifying
	@Query("UPDATE OeNote n SET n.likeNum = IFNULL(n.likeNum,0) + 1 WHERE n.id = :noteId")
	void addLikeNumById(Long noteId);

	@Transactional
	@Modifying
	@Query("UPDATE OeNote n SET n.likeNum = IFNULL(n.likeNum,1) - 1 WHERE n.id = :noteId")
	void subtractLikeNumById(Long noteId);
	
	@Transactional
	@Modifying
	@Query("UPDATE OeNote n SET n.replyNum = IFNULL(n.replyNum,0) + 1 WHERE n.id = :noteId")
	void addReplyNumById(Long noteId);
	
	@Transactional
	@Modifying
	@Query("UPDATE OeNote n SET n.replyNum = IFNULL(n.replyNum,1) - 1 WHERE n.id = :noteId")
	void subtractReplyNumById(Long noteId);

	@Transactional
	@Modifying
	@Query("UPDATE OeNote n SET n.enable = false WHERE n.id = :id AND n.memberId = :memberId")
	void enableNotByIdAndMemberId(Long id, Long memberId);
}

package cn.tgxy.oledu.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import cn.tgxy.oledu.po.OeMemberLikeNote;
import cn.tgxy.tgbase.dao.BsBaseDao;

public interface OeMemberLikeNoteDao extends BsBaseDao<OeMemberLikeNote, Long> {

	@Query("SELECT mln.noteId FROM OeMemberLikeNote mln WHERE mln.memberId = :memberId AND mln.lessonId = :lessonId")
	List<String> getNoteIdsByMemberIdAndLessonId(Long memberId,Long lessonId);

	boolean existsByMemberIdAndNoteId(Long id, Long noteId);

	void deleteByMemberIdAndNoteId(Long id, Long noteId);
	
}

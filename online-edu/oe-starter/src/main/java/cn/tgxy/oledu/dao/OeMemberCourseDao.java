package cn.tgxy.oledu.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.tgxy.oledu.po.OeMemberCourse;
import cn.tgxy.tgbase.dao.BsBaseDao;
import jakarta.transaction.Transactional;

public interface OeMemberCourseDao extends BsBaseDao<OeMemberCourse, Long>{

	OeMemberCourse findByMemberIdAndCourseId(Long id, Long courseId);

	boolean existsByMemberIdAndCourseId(Long memberId, Long courseId);
	
	@Transactional
	@Modifying
	@Query("UPDATE OeMemberCourse mc SET mc.lessonId = :lessonId WHERE mc.memberId = :memberId AND mc.courseId = :courseId")
	void updateLessonIdByMemberIdAndCourseId(Long lessonId,Long memberId, Long courseId);
	
	@Query("SELECT mc.id FROM OeMemberCourse mc WHERE mc.memberId = :memberId AND mc.courseId = :courseId")
	Long getIdByMemberIdAndCourseId(Long memberId,Long courseId);

	List<OeMemberCourse> findByMemberId(Long memberId);

	@Transactional
	@Modifying
	@Query("UPDATE OeMemberCourse mc SET mc.lastViewTime = :date WHERE mc.memberId = :memberId AND mc.courseId = :courseId")
	void updateLastViewTimeByMemberIdAndCourseId(Date date, Long memberId, Long courseId);

	@Transactional
	@Modifying
	@Query("UPDATE OeMemberCourse mc SET mc.completionPercentage = :i WHERE mc.memberId = :memberId AND mc.courseId = :courseId")
	void updateCompletionPercentageByByMemberIdAndCourseId(int i, Long memberId, Long courseId);

	@Transactional
	@Modifying
	@Query("UPDATE OeMemberCourse mc SET mc.noteNum = IFNULL(mc.noteNum,0) + 1 WHERE mc.id = :memberCourseId")
	void addNoteNumById(Long memberCourseId);

	@Transactional
	@Modifying
	@Query("UPDATE OeMemberCourse mc SET mc.noteNum = IFNULL(mc.noteNum,1) - 1 WHERE mc.id = :memberCourseId")
	void subtractNoteNumById(Long memberCourseId);

}

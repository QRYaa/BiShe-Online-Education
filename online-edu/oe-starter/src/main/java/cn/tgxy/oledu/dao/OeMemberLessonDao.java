package cn.tgxy.oledu.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.tgxy.oledu.po.OeMemberLesson;
import cn.tgxy.tgbase.dao.BsBaseDao;
import jakarta.transaction.Transactional;

public interface OeMemberLessonDao extends BsBaseDao<OeMemberLesson, Long>{
	List<OeMemberLesson> findByMemberCourseId(Long memberCourseId);

	boolean existsByMemberIdAndLessonId(Long memberId, Long lessonId);

	@Transactional
	@Modifying
	@Query("UPDATE OeMemberLesson ml SET ml.watchedStatus = :watchedStatus WHERE ml.memberId = :id AND ml.lessonId = :lessonId")
	void updateWatchedStatusByMemberIdAndLessonId(Integer watchedStatus, Long id, Long lessonId);

	@Transactional
	@Modifying
	@Query("UPDATE OeMemberLesson ml SET ml.lastViewedTime = :date WHERE ml.memberId = :memberId AND ml.lessonId = :lessonId")
	void updateLastViewedTimeByMemberIdAndLessonId(Date date, Long memberId, Long lessonId);

	OeMemberLesson findByMemberIdAndLessonId(Long id, Long lessonId);

	@Transactional
	@Modifying
	@Query("UPDATE OeMemberLesson ml SET ml.progress = :progress WHERE ml.memberId = :memberId AND ml.lessonId = :lessonId")
	void updateProgressByMemberIdAndLessonId(Integer progress, Long memberId, Long lessonId);

	@Transactional
	@Modifying
	@Query("UPDATE OeMemberLesson ml SET ml.duration = IFNULL(ml.duration,0) + :watchDuration WHERE ml.memberId = :memberId AND ml.lessonId = :lessonId")
	void updateDurationByMemberIdAndLessonId(int watchDuration, Long memberId, Long lessonId);
	
	@Query("SELECT COUNT(*) FROM OeMemberLesson ml WHERE ml.memberCourseId = :memberCourseId AND ml.watchedStatus = :watchedStatus")
	int calculateCountByMemberCourseIdAndWatchedStatus(Long memberCourseId,int watchedStatus);

	@Query("SELECT ml.memberCourseId FROM OeMemberLesson ml WHERE ml.lessonId = :lessonId")
	Long getMemberCourseIdByLessonId(Long lessonId);
}

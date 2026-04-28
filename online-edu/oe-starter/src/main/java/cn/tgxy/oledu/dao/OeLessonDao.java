package cn.tgxy.oledu.dao;

import cn.tgxy.tgbase.dao.BsBaseDao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import cn.tgxy.oledu.po.OeLesson;

public interface OeLessonDao extends BsBaseDao<OeLesson, Long>{

	List<OeLesson> findByChapterId(Long chapterId);

	@Query("SELECT l.mediaUrl FROM OeLesson l WHERE l.id = :id")
	String getMediaUrlById(Long id);

	@Query("SELECT l.chapterId FROM OeLesson l WHERE l.id = :lessonId")
	Long getChapterIdById(Long lessonId);

	@Query("SELECT l.mediaUrl FROM OeLesson l WHERE l.id = :lessonId AND l.previewAble = true")
	String getMediaUrlByIdAndPreviewAble(Long lessonId);
	
	@Query("SELECT COUNT(*) FROM OeLesson l WHERE l.chapterId IN (SELECT ct.id FROM OeChapter ct WHERE ct.courseId = :courseId)")
	int calculateCountByCourseId(Long courseId);

	@Query("SELECT l.id FROM OeLesson l WHERE l.mediaUrl = :path")
	Long getIdByMediaUrl(String path);

	OeLesson findByMediaUrl(String path);
}

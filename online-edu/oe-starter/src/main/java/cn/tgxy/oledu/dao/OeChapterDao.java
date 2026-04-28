package cn.tgxy.oledu.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import cn.tgxy.oledu.po.OeChapter;
import cn.tgxy.tgbase.dao.BsBaseDao;

public interface OeChapterDao extends BsBaseDao<OeChapter, Long>{
	
	List<OeChapter> findByCourseId(Long courseId);

	@Query("SELECT c.courseId FROM OeChapter c WHERE c.id = :chapterId")
	Long getCourseIdById(Long chapterId);
	
}

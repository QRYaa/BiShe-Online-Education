package cn.tgxy.oledu.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import cn.tgxy.oledu.po.OeTeacherTag;
import cn.tgxy.tgbase.dao.BsBaseDao;

public interface OeTeacherTagDao extends BsBaseDao<OeTeacherTag, Long>{

	@Query("SELECT tt FROM OeTeacherTag tt INNER JOIN OeTeacherTagMap ttm ON tt.id = ttm.tagId WHERE ttm.teacherId = :teacherId")
	List<OeTeacherTag> findByTeacherId(Long teacherId);

}

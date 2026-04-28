package cn.tgxy.oledu.dao;

import java.util.List;

import cn.tgxy.oledu.po.OeTeacherTagMap;
import cn.tgxy.tgbase.dao.BsBaseDao;

public interface OeTeacherTagMapDao extends BsBaseDao<OeTeacherTagMap,Long> {

	List<OeTeacherTagMap> findByTeacherId(Long id);

	void deleteByTeacherIdAndTagId(Long id, Long tagId);

}

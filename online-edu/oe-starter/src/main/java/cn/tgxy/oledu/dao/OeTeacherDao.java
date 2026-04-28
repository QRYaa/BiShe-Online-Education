package cn.tgxy.oledu.dao;

import cn.tgxy.tgbase.dao.BsBaseDao;
import cn.tgxy.oledu.po.OeTeacher;

public interface OeTeacherDao extends BsBaseDao<OeTeacher, Long>{

	boolean existsByCode(String code);

	OeTeacher findByCode(String code);

}

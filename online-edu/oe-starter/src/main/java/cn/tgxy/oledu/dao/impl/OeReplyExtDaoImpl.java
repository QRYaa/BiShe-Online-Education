package cn.tgxy.oledu.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.tgxy.oledu.dao.OeReplyExtDao;
import cn.tgxy.oledu.po.OeReply;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.impl.BsExtBaseDaoImpl;

@Repository("replyExtDao")
public class OeReplyExtDaoImpl extends BsExtBaseDaoImpl implements OeReplyExtDao {

	@Override
	public PageContent<OeReply> findPageContent(Long noteId, String trackId, String senderCode, Boolean enable,
			Integer pageNum, Integer pageSize) throws Exception {
		String hql="SELECT r FROM OeReply r ";
		Map<String,Object> paramMap=new HashMap<>();
		
		if(StringUtils.isNotEmpty(senderCode)) {
			hql+=" LEFT JOIN OeMember m ON r.senderId = m.id ";
		}
		hql+=" WHERE 1=1 ";
		if(noteId!=null) {
			hql+=" AND r.noteId = :noteId ";
			paramMap.put("noteId", noteId);
		}
		if(StringUtils.isNotEmpty(trackId)) {
			hql+=" AND r.trackId = :trackId ";
			paramMap.put("trackId", trackId);
		}
		if(StringUtils.isNotEmpty(senderCode)){
			hql+=" AND m.code = :senderCode ";
			paramMap.put("senderCode", senderCode);
		}
		if(enable!=null) {
			hql+=" AND r.enable = :enable ";
			paramMap.put("enable", enable);
		}
		hql+=" ORDER BY r.id DESC ";
		// 调用父类分页方法
		return super.findPageContent(OeReply.class, hql, pageNum, pageSize, paramMap);
	}
	

}

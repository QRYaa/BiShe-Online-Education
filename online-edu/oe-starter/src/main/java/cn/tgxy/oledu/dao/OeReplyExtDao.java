package cn.tgxy.oledu.dao;

import cn.tgxy.oledu.po.OeReply;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.BsExtBaseDao;

public interface OeReplyExtDao extends BsExtBaseDao {
	public PageContent<OeReply> findPageContent(Long noteId,String trackId,String senderCode,Boolean enable,Integer pageNum,Integer pageSize) throws Exception;

}

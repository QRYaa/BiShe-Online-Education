package cn.tgxy.oledu.dao;

import java.util.Date;

import cn.tgxy.oledu.po.OeOrder;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.BsExtBaseDao;

public interface OeOrderExtDao extends BsExtBaseDao {

	public PageContent<OeOrder> findPageContent(String code,String transactionId,Date startTime,Date endTime,Integer paymentStatus,String memberCode,String orderItemName,int pageNum, int pageSize) throws Exception;

	PageContent<OeOrder> findPageContentOnH5(String memberCode, Integer paymentStatus, String codeOrItemName,
			int pageNum, int pageSize) throws Exception;
}

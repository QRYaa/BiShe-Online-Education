package cn.tgxy.oledu.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.tgxy.oledu.dao.OeOrderExtDao;
import cn.tgxy.oledu.po.OeOrder;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.impl.BsExtBaseDaoImpl;

@Repository("oeOrderExtDao")
public class OeOrderExtDaoImpl extends BsExtBaseDaoImpl implements OeOrderExtDao {

	@Override
	public PageContent<OeOrder> findPageContent(String code, String transactionId, Date startTime, Date endTime,
			Integer paymentStatus, String memberCode, String orderItemName, int pageNum, int pageSize)
			throws Exception {
		String hql = "SELECT o FROM OeOrder o ";
		Map<String, Object> paramMap = new HashMap<>();

		boolean joinOrderItem = StringUtils.isNotEmpty(orderItemName);
		if (joinOrderItem) {
			hql += " LEFT JOIN OeOrderItem oi ON o.id = oi.orderId ";
		}
		if (StringUtils.isNotEmpty(memberCode)) {
			hql += " LEFT JOIN OeMember m ON o.memberId = m.id ";
		}

		hql += " WHERE 1=1 ";

		if (StringUtils.isNotEmpty(code)) {
			hql += " AND o.code = :code ";
			paramMap.put("code", code);
		}

		if (StringUtils.isNotEmpty(transactionId)) {
			hql += " AND o.transactionId = :transactionId ";
			paramMap.put("transactionId", transactionId);
		}

		if (startTime != null) {
			hql += " AND o.createdTime >= :startTime ";
			paramMap.put("startTime", startTime);
		}

		if (endTime != null) {
			hql += " AND o.createdTime <= :endTime ";
			paramMap.put("endTime", endTime);
		}

		if (paymentStatus != null) {
			hql += " AND o.paymentStatus = :paymentStatus ";
			paramMap.put("paymentStatus", paymentStatus);
		}

		if (StringUtils.isNotEmpty(memberCode)) {
			hql += " AND m.code = :memberCode ";
			paramMap.put("memberCode", memberCode);
		}

		if (joinOrderItem) {
			hql += " AND oi.name LIKE :orderItemName "; // 订单项名称模糊查询
			paramMap.put("orderItemName", "%" + orderItemName + "%");
		}

		// 默认按创建时间倒序
		hql += "ORDER BY o.createdTime DESC";

		// 调用父类分页方法
		return super.findPageContent(OeOrder.class, hql, pageNum, pageSize, paramMap);
	}

	@Override
	public PageContent<OeOrder> findPageContentOnH5(String memberCode, Integer paymentStatus, String codeOrItemName,int pageNum, int pageSize) throws Exception {
		String hql = "SELECT o FROM OeOrder o ";
		Map<String, Object> paramMap = new HashMap<>();
		if (StringUtils.isNotEmpty(codeOrItemName)) {
			hql += " LEFT JOIN OeOrderItem oi ON o.id = oi.orderId ";
		}
		if (StringUtils.isNotEmpty(memberCode)) {
			hql += " LEFT JOIN OeMember m ON o.memberId = m.id ";
		}
		hql += " WHERE 1=1 ";

		if (StringUtils.isNotEmpty(memberCode)) {
			hql += " AND m.code = :memberCode ";
			paramMap.put("memberCode", memberCode);
		}
		if (paymentStatus != null) {
			hql += " AND o.paymentStatus = :paymentStatus ";
			paramMap.put("paymentStatus", paymentStatus);
		}
		if (StringUtils.isNotEmpty(codeOrItemName)) {
			hql += " AND (oi.name LIKE :orderItemName OR o.code = :code) "; // 订单项名称模糊查询
			paramMap.put("orderItemName", "%" + codeOrItemName + "%");
			paramMap.put("code", codeOrItemName);
		}

		// 默认按创建时间倒序
		hql += "ORDER BY o.createdTime DESC";

		// 调用父类分页方法
		return super.findPageContent(OeOrder.class, hql, pageNum, pageSize, paramMap);
	}
}

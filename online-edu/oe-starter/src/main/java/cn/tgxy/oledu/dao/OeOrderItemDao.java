package cn.tgxy.oledu.dao;

import cn.tgxy.tgbase.dao.BsBaseDao;

import java.util.List;

import cn.tgxy.oledu.po.OeOrderItem;

public interface OeOrderItemDao extends BsBaseDao<OeOrderItem, Long>{

	List<OeOrderItem> findByOrderId(Long orderId);

}

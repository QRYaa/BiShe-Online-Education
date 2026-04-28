package cn.tgxy.oledu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.oledu.constant.OeConstant;
import cn.tgxy.oledu.dao.OeCourseDao;
import cn.tgxy.oledu.dao.OeOrderItemDao;
import cn.tgxy.oledu.dto.OeOrderItemDto;
import cn.tgxy.oledu.po.OeCourse;
import cn.tgxy.oledu.po.OeOrderItem;
import cn.tgxy.oledu.service.OeOrderItemService;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;

@Service
public class OeOrderItemServiceImpl extends BsBaseServiceImpl<OeOrderItem, OeOrderItemDto> implements OeOrderItemService{

	@Autowired
	private OeOrderItemDao orderItemDao;
	
	@Autowired
	private OeCourseDao courseDao;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = orderItemDao;
	}
	
	@Override
	public List<OeOrderItemDto> findByOrderId(Long orderId) throws Exception{
		List<OeOrderItem> orderItems=orderItemDao.findByOrderId(orderId);
		return transToDtoList(orderItems);
	}
	
	@Override
	public OeOrderItemDto generateByReferenceIdAndType(Long referenceId,Integer type) throws Exception {
		if(type.equals(OeConstant.ORDER_ITEM_OLCOURSE)) {
			OeCourse course=courseDao.findById(referenceId).orElse(null);
			return transToDto(new OeOrderItem(course));
		}
		return null;
	}
}

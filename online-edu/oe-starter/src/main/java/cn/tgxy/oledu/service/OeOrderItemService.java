package cn.tgxy.oledu.service;

import cn.tgxy.tgbase.service.BsBaseService;
import cn.tgxy.oledu.po.OeOrderItem;

import java.util.List;

import cn.tgxy.oledu.dto.OeOrderItemDto;

public interface OeOrderItemService extends BsBaseService<OeOrderItem, OeOrderItemDto>{

	List<OeOrderItemDto> findByOrderId(Long orderId) throws Exception;

	OeOrderItemDto generateByReferenceIdAndType(Long referenceId, Integer type) throws Exception;

}

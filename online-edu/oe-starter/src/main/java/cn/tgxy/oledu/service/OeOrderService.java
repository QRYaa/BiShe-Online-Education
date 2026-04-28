package cn.tgxy.oledu.service;

import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.service.BsBaseService;
import cn.tgxy.oledu.po.OeOrder;

import java.util.Date;

import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.dto.OeOrderDto;
import cn.tgxy.oledu.dto.response.OeOrderPriceDto;

public interface OeOrderService extends BsBaseService<OeOrder, OeOrderDto>{

	void createOrder(OeMemberDto currentMemberDto,OeOrderDto orderDto) throws Exception;

	PageContent<OeOrderDto> findPageData(String code, String transactionId, Date startTime, Date endTime,
			Integer paymentStatus, String memberCode, String orderItemName, int pageNum, int pageSize) throws Exception;

	PageContent<OeOrderDto> findPageDataOnH5(String memberCode, Integer paymentStatus, String codeOrItemName,
			int pageNum, int pageSize) throws Exception;

	OeOrderDto findByCodeAndMemberId(String code,Long memberId) throws Exception;

	void changePaymentStatus(Long id, Integer paymentStatus);

	OeOrderPriceDto calculatePrice(OeOrderDto orderDto);

	void cancelOrder(StringBuilder strB);

	/**
	 * 设置订单状态为完成，然后判断订单类型做对应的操作
	 * @param
	 * @return
	 * @throws
	 */
	void completeOrder(String code);

}

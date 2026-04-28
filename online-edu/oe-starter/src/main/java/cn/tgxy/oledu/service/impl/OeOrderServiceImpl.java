package cn.tgxy.oledu.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.binarywang.wxpay.service.WxPayService;

import cn.tgxy.oledu.constant.OeConstant;
import cn.tgxy.oledu.dao.OeMemberDao;
import cn.tgxy.oledu.dao.OeOrderDao;
import cn.tgxy.oledu.dao.OeOrderExtDao;
import cn.tgxy.oledu.dto.OeCourseDto;
import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.dto.OeOrderDto;
import cn.tgxy.oledu.dto.OeOrderItemDto;
import cn.tgxy.oledu.dto.response.OeOrderPriceDto;
import cn.tgxy.oledu.po.OeMember;
import cn.tgxy.oledu.po.OeOrder;
import cn.tgxy.oledu.service.OeCourseService;
import cn.tgxy.oledu.service.OeOrderItemService;
import cn.tgxy.oledu.service.OeOrderService;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;

@Service
public class OeOrderServiceImpl extends BsBaseServiceImpl<OeOrder, OeOrderDto> implements OeOrderService {

	private static Logger log = LoggerFactory.getLogger(OeOrderServiceImpl.class);

	private String sandboxSignKey;

	@Autowired
	private OeOrderDao orderDao;

	@Autowired
	private OeOrderExtDao orderExtDao;

	@Autowired
	private OeMemberDao memberDao;

	@Autowired
	private OeOrderItemService orderItemService;

	@Autowired
	private OeCourseService courseService;

	@Autowired
	private WxPayService wxPayService;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = orderDao;
	}

	@Override
	public PageContent<OeOrderDto> findPageData(String code, String transactionId, Date startTime, Date endTime,
			Integer paymentStatus, String memberCode, String orderItemName, int pageNum, int pageSize)
			throws Exception {
		PageContent<OeOrder> pageContent = orderExtDao.findPageContent(code, transactionId, startTime, endTime,
				paymentStatus, memberCode, orderItemName, pageNum, pageSize);
		PageContent<OeOrderDto> page = new PageContent<>(transToDtoList(pageContent.getContent()),
				pageContent.getTotalItems(), pageContent.getPageNum(), pageSize);
		return page;
	}

	@Override
	public OeOrderDto transToDto(OeOrder order) throws Exception {
		OeOrderDto orderDto = super.transToDto(order);
		if (orderDto == null)
			return null;
		OeMember member = memberDao.findById(orderDto.getMemberId()).orElse(null);
		if (member != null) {
			orderDto.setMemberAvatar(member.getAvatar());
			orderDto.setMemberName(member.getName());
		}
		orderDto.setOrderItemDtoList(orderItemService.findByOrderId(orderDto.getId()));
		return orderDto;
	}

	@Override
	public OeOrderDto transToDtoDetail(OeOrder order) throws Exception {
		OeOrderDto orderDto = super.transToDto(order);
		if (orderDto == null)
			return null;
		OeMember member = memberDao.findById(orderDto.getMemberId()).orElse(null);
		if (member != null) {
			orderDto.setMemberAvatar(member.getAvatar());
			orderDto.setMemberName(member.getName());
		}
		orderDto.setOrderItemDtoList(orderItemService.findByOrderId(orderDto.getId()));
		return orderDto;
	}

	@Override
	public void changePaymentStatus(Long id, Integer paymentStatus) {
		orderDao.changePaymentStatusById(id, paymentStatus);
	}

	@Override
	public void cancelOrder(StringBuilder strB) {
		// 计算半小时前的时间
		Date time = new Date(System.currentTimeMillis() - OeConstant.ORDER_TIME_OUT);
		int num = orderDao.updatePaymentStatusByPaymentStatusAndCreatedTime(OeConstant.ORDER_CANCELLED,
				OeConstant.ORDER_PENDING, time);
		strB.append(String.format("取消订单【%d】条", num));
	}

//----------------------ToC端方法----------------------------------

	@Override
	public PageContent<OeOrderDto> findPageDataOnH5(String memberCode, Integer paymentStatus, String codeOrItemName,
			int pageNum, int pageSize) throws Exception {
		PageContent<OeOrder> pageContent = orderExtDao.findPageContentOnH5(memberCode, paymentStatus, codeOrItemName,
				pageNum, pageSize);
		PageContent<OeOrderDto> page = new PageContent<>(transToDtoList(pageContent.getContent()),
				pageContent.getTotalItems(), pageContent.getPageNum(), pageSize);
		return page;
	}

	/**
	 * 需要先保存order得到id，再保存orderitem
	 */

	@Override
	public void createOrder(OeMemberDto currentMemberDto, OeOrderDto orderDto) throws Exception {
		BigDecimal price = new BigDecimal("0.0");
		
		List<OeOrderItemDto> orderItemDtoList = orderDto.getOrderItemDtoList();
		for (OeOrderItemDto orderItemDto : orderItemDtoList) {
			BigDecimal itemPrice = new BigDecimal("0.0");
			BigDecimal itemTotalPrice = new BigDecimal("0.0");
			if (orderItemDto.getType().equals(OeConstant.ORDER_ITEM_OLCOURSE)) {
				// 查表，保证订单项数据最新
				OeCourseDto courseDto = courseService.get(orderItemDto.getReferenceId());
				itemPrice = courseDto.getPrice();
				itemTotalPrice=itemPrice.multiply(BigDecimal.valueOf(orderItemDto.getQuantity()));
				orderItemDto.setPrice(itemPrice);
				orderItemDto.setTotalPrice(itemTotalPrice);
				orderItemDto.setName(courseDto.getName());
				orderItemDto.setImage(courseDto.getSquareImage());
				orderItemDto.setDescription(courseDto.getDescription());
			}
			price = price.add(itemTotalPrice);
			
		}
		BigDecimal discount=new BigDecimal("0.0");
		orderDto.setPrice(price);
		orderDto.setDiscount(discount);// 减免优惠尚未实现
		orderDto.setFinalPrice(price.subtract(discount));

		orderDto.setMemberId(currentMemberDto.getId());
		String code = genCode();
		orderDto.setCode(code);
		orderDto.setCreatedTime(new Date());
		orderDto.setPaymentStatus(OeConstant.ORDER_PENDING);
		save(orderDto);

		for (OeOrderItemDto orderItemDto : orderItemDtoList) {
			orderItemDto.setOrderId(orderDto.getId());
			orderItemService.save(orderItemDto);
		}

		// 告诉微信服务器，并设置为待确认支付状态
		// TODO: 调用微信支付api
//		WxPayConfig config=wxPayService.getConfig();
//		WxPayUnifiedOrderRequest request=new WxPayUnifiedOrderRequest();
//		request.setAppid(config.getAppId());
//		request.setMchId(config.getMchId());
//		request.setBody(orderItemDtoList.get(0).getDescription());
//		request.setOutTradeNo(code);
//		request.setNotifyUrl(config.getNotifyUrl());
//		request.setTotalFee(orderDto.getFinalPrice().multiply(BigDecimal.valueOf(100)).intValueExact());
//		request.setOpenid(currentMemberDto.getWeixinOpenId());
//		request.setTradeType("JSAPI");
//		request.setSpbillCreateIp("123.12.12.123");//随便设置的
//		request.setNonceStr(String.valueOf(System.currentTimeMillis()));
//		request.setSign(SignUtils.createSign(request, null, config.getMchKey(), null));		
//		wxPayService.unifiedOrder(request);
		
//		WxPayConfig config=wxPayService.getConfig();
//		WxPayUnifiedOrderV3Request request=new WxPayUnifiedOrderV3Request();
//		request.setAppid(config.getAppId());
//		request.setMchid(config.getMchId());
//		request.setDescription(orderItemDtoList.get(0).getDescription());
//		request.setOutTradeNo(code);
//		request.setNotifyUrl(config.getNotifyUrl());
//		Amount amount=new Amount();
//		amount.setCurrency("CNY");
//		amount.setTotal(orderDto.getFinalPrice().multiply(BigDecimal.valueOf(100)).intValueExact());
//		request.setAmount(amount);
//		Payer payer=new Payer();
//		payer.setOpenid(currentMemberDto.getWeixinOpenId());
//		request.setPayer(payer);
//		wxPayService.createOrderV3(TradeTypeEnum.JSAPI,request);
		
		//orderDao.changePaymentStatusById(orderDto.getId(), OeConstant.ORDER_PENDING_CONFIRM_PAYMENT);
	}

	@Override
	public OeOrderDto findByCodeAndMemberId(String code, Long memberId) throws Exception {
		OeOrder order = orderDao.findByCodeAndMemberId(code, memberId);
		return transToDtoDetail(order);
	}

	@Override
	public OeOrderPriceDto calculatePrice(OeOrderDto orderDto) {
		OeOrderPriceDto orderPriceDto = new OeOrderPriceDto();
		BigDecimal price = new BigDecimal("0.0");
		for (OeOrderItemDto orderItemDto : orderDto.getOrderItemDtoList()) {
			price = price.add(orderItemDto.getTotalPrice());
		}
		orderPriceDto.setPrice(price);
		orderPriceDto.setDiscount(new BigDecimal("0.0"));
		orderPriceDto.setFinalPrice(price);
		return orderPriceDto;
	}

	@Override
	public void completeOrder(String code) {

	}

//TODO: 支付逻辑

	/**
	 * 时间+随机数+用户id
	 * 
	 * @param
	 * @return
	 * @throws
	 */
	private String genCode() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String timeCode = dateFormat.format(now);
		String randomNumeric = RandomStringUtils.randomNumeric(3);
		return timeCode + randomNumeric;
	}

}

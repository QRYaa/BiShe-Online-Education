package cn.tgxy.oledu.controller.fe.mp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.oledu.dao.OeOrderDao;
import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.dto.OeOrderDto;
import cn.tgxy.oledu.dto.OeOrderItemDto;
import cn.tgxy.oledu.dto.response.OeOrderPriceDto;
import cn.tgxy.oledu.service.OeMemberAuthService;
import cn.tgxy.oledu.service.OeOrderItemService;
import cn.tgxy.oledu.service.OeOrderService;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/fe/mp/member/order")
public class OeFeMpOrderController {
	
	@Autowired
	private OeOrderDao orderDao;
	
	@Autowired
	private OeMemberAuthService memberAuthService;
	
	@Autowired
	private OeOrderService orderService;
	
	@Autowired
	private OeOrderItemService orderItemService;
	
	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(Integer paymentStatus, String codeOrItemName,@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize)
			throws Exception {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		PageContent<OeOrderDto> page=orderService.findPageDataOnH5(memberDto.getCode(), paymentStatus, codeOrItemName, pageNum, pageSize);
		return BsResponse.success(page);
	}
	
	@GetMapping("detail")
	@Operation(summary = "订单详情")
	public BsResponse orderDetail(String code) throws Exception {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		OeOrderDto orderDto=orderService.findByCodeAndMemberId(code,memberDto.getId());
		return BsResponse.success(orderDto);
	}
	
	@GetMapping("generateOrderItemByReferenceIdAndType")
	@Operation(summary = "通过参照id和类型生成订单项")
	public BsResponse generateOrderItemByReferenceIdAndType(Long referenceId,Integer type) throws Exception{
		OeOrderItemDto orderItemDto=orderItemService.generateByReferenceIdAndType(referenceId, type);
		return BsResponse.success(orderItemDto);
	}
	
	@PostMapping("calculatePrice")
	@Operation(summary="计算价格")
	public BsResponse calculatePrice(@RequestBody OeOrderDto orderDto) throws Exception {
		OeOrderPriceDto orderPriceDto=orderService.calculatePrice(orderDto);
		return BsResponse.success(orderPriceDto);
	}
	
	@PostMapping("createOrder")
	@Operation(summary = "创建订单")
	public BsResponse createOrder(@RequestBody OeOrderDto orderDto) throws Exception {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		orderService.createOrder(memberDto, orderDto);
		return BsResponse.success(orderDto.getCode());
	}
	
	@GetMapping("getStatusByCode")
	@Operation(summary = "通过订单code获取订单状态")
	public BsResponse getStatusByCode(String code) throws Exception {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		return BsResponse.success(orderDao.getStatusByMemberIdAndCode(memberDto.getId(),code));
	}
}

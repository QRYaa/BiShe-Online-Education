package cn.tgxy.oledu.controller.admin;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.oledu.dto.OeOrderDto;
import cn.tgxy.oledu.service.OeOrderService;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/oeOrder")
@Tag(name = "oeOrder")
public class OeOrderController  {

	@Autowired
	private OeOrderService oeOrderService;
	
	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(String code,String transactionId,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date startTime,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date endTime,Integer paymentStatus,String memberCode,String orderItemName,@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize)
			throws Exception {
		PageContent<OeOrderDto> page=oeOrderService.findPageData(code, transactionId, startTime, endTime, paymentStatus, memberCode, orderItemName, pageNum, pageSize);
		return BsResponse.success(page);
	}

	@GetMapping("/get/{id}")
	@Operation(summary = "明细")
	public BsResponse get(@PathVariable("id") Long id) throws Exception {
		OeOrderDto d = oeOrderService.get(id);
		return BsResponse.success(d);
	}

	@PostMapping(value = "/save")
	@Operation(summary = "新增")
	public BsResponse save(@RequestBody OeOrderDto dto) throws Exception {
		dto.setId(null);
		oeOrderService.save(dto);
		return BsResponse.success();
	}

	@PutMapping(value = "/update")
	@Operation(summary = "修改")
	public BsResponse update(@RequestBody OeOrderDto dto) throws Exception {
		oeOrderService.update(dto);
		return BsResponse.success();
	}

	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除")
	public BsResponse delete(@PathVariable("id") Long id) throws Exception {
		oeOrderService.deleteById(id);
		return BsResponse.success();
	}

	@PostMapping("delete")
	@Operation(summary = "批量删除")
	public BsResponse delete(@RequestBody List<Long> idList) throws Exception {
		oeOrderService.deleteByIdInBatch(idList);
		return BsResponse.success();
	}
	
	@PostMapping("changePaymentStatus")
	@Operation(summary = "修改订单状态")
	public BsResponse changePaymentStatus(Long id, Integer paymentStatus) throws Exception {
		oeOrderService.changePaymentStatus(id, paymentStatus);
		return BsResponse.success();
	}
}

package cn.tgxy.oledu.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.oledu.dto.OeMemberIdCardAuditDto;
import cn.tgxy.oledu.service.OeMemberIdCardAuditService;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import cn.tgxy.tgbase.service.BsAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/oeMemberIdCardAudit")
@Tag(name = "oeMemberIdCardAudit")
public class OeMemberIdCardAuditController  {

	@Autowired
	private OeMemberIdCardAuditService memberIdCardAuditService;
	
	@Autowired
	private BsAuthService authService;
	
	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(String memberCode, Integer status, String idCardNumber,
            String idCardRealName, @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize)
			throws Exception {
		PageContent<OeMemberIdCardAuditDto> pageContent=memberIdCardAuditService.findPageData(memberCode, status, idCardNumber, idCardRealName, pageNum, pageSize);
		return BsResponse.success(pageContent);
	}

	@GetMapping("/get/{id}")
	@Operation(summary = "明细")
	public BsResponse get(@PathVariable("id") Long id) throws Exception {
		OeMemberIdCardAuditDto d = memberIdCardAuditService.get(id);
		return BsResponse.success(d);
	}

	@PostMapping(value = "/save")
	@Operation(summary = "新增")
	public BsResponse save(@RequestBody OeMemberIdCardAuditDto dto) throws Exception {
		dto.setId(null);
		memberIdCardAuditService.save(dto);
		return BsResponse.success();
	}

	@PutMapping(value = "/update")
	@Operation(summary = "修改")
	public BsResponse update(@RequestBody OeMemberIdCardAuditDto dto) throws Exception {
		memberIdCardAuditService.update(dto);
		return BsResponse.success();
	}

	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除")
	public BsResponse delete(@PathVariable("id") Long id) throws Exception {
		memberIdCardAuditService.deleteById(id);
		return BsResponse.success();
	}

	@PostMapping("delete")
	@Operation(summary = "批量删除")
	public BsResponse delete(@RequestBody List<Long> idList) throws Exception {
		memberIdCardAuditService.deleteByIdInBatch(idList);
		return BsResponse.success();
	}
	
	@PostMapping("passAudit")
	@Operation(summary = "通过审核")
	public BsResponse passAudit(@RequestBody OeMemberIdCardAuditDto dto) throws Exception {
		memberIdCardAuditService.passAudit(authService.getCurrentUser(),dto);
		return BsResponse.success();
	}
	
	@PostMapping("rejectAudit")
	@Operation(summary = "驳回审核")
	public BsResponse rejectAudit(@RequestBody OeMemberIdCardAuditDto dto) throws Exception {
		memberIdCardAuditService.rejectAudit(authService.getCurrentUser(),dto);
		return BsResponse.success();
	}
}

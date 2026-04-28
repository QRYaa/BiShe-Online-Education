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

import cn.tgxy.oledu.dto.OeReplyDto;
import cn.tgxy.oledu.service.OeReplyService;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/oeReply")
@Tag(name = "oeReply")
public class OeReplyController  {

	@Autowired
	private OeReplyService replyService;
	
	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(Long noteId, String trackId, String senderCode, Boolean enable,@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize)
			throws Exception {
		PageContent<OeReplyDto> page=replyService.findPageData(noteId, trackId, senderCode, enable, pageNum, pageSize);
		return BsResponse.success(page);
	}

	@GetMapping("/get/{id}")
	@Operation(summary = "明细")
	public BsResponse get(@PathVariable("id") Long id) throws Exception {
		OeReplyDto d = replyService.get(id);
		return BsResponse.success(d);
	}

	@PostMapping(value = "/save")
	@Operation(summary = "新增")
	public BsResponse save(@RequestBody OeReplyDto dto) throws Exception {
		dto.setId(null);
		replyService.save(dto);
		return BsResponse.success();
	}

	@PutMapping(value = "/update")
	@Operation(summary = "修改")
	public BsResponse update(@RequestBody OeReplyDto dto) throws Exception {
		replyService.update(dto);
		return BsResponse.success();
	}

	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除")
	public BsResponse delete(@PathVariable("id") Long id) throws Exception {
		replyService.deleteById(id);
		return BsResponse.success();
	}

	@PostMapping("delete")
	@Operation(summary = "批量删除")
	public BsResponse delete(@RequestBody List<Long> idList) throws Exception {
		replyService.deleteByIdInBatch(idList);
		return BsResponse.success();
	}
	
	@PutMapping("switchEnable")
	@Operation(summary = "切换启用状态")
	public BsResponse switchEnable(@RequestBody OeReplyDto replyDto) throws Exception {
		replyService.switchEnable(replyDto);
		return BsResponse.success();
	}
}

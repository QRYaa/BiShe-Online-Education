package cn.tgxy.oledu.controller.fe.mp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.dto.OeReplyDto;
import cn.tgxy.oledu.service.OeMemberAuthService;
import cn.tgxy.oledu.service.OeReplyService;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/fe/mp/member/reply")
public class OeFeMpReplyController {

	@Autowired
	private OeMemberAuthService memberAuthService;

	@Autowired
	private OeReplyService replyService;

	@PostMapping("createReply")
	@Operation(summary = "创建回复")
	public BsResponse createReply(@RequestBody OeReplyDto replyDto) throws Exception {
		OeMemberDto memberDto = memberAuthService.getCurrentMember();
		OeReplyDto replyDto2=replyService.createReply(memberDto, replyDto);
		return BsResponse.success(replyDto2);
	}

	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(Long noteId,@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize)
			throws Exception {
		if(noteId==null) return BsResponse.success();
		PageContent<OeReplyDto> page=replyService.findPageData(noteId, null, null, true, pageNum, pageSize);
		return BsResponse.success(page);
	}

	@PostMapping("/delete")
	@Operation(summary = "删除")
	public BsResponse delete(Long id) throws Exception {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		replyService.enableNotByIdAndSenderId(id, memberDto.getId());
		return BsResponse.success();
	}
	
}

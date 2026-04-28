package cn.tgxy.oledu.controller.fe.mp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.dto.OeNoteDto;
import cn.tgxy.oledu.service.OeMemberAuthService;
import cn.tgxy.oledu.service.OeNoteService;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/fe/mp/member/note")
public class OeFeMpNoteController {

	@Autowired
	private OeMemberAuthService memberAuthService;

	@Autowired
	private OeNoteService noteService;

	@PostMapping("createNote")
	@Operation(summary = "创建笔记")
	public BsResponse createNote(@RequestBody OeNoteDto noteDto) throws Exception {
		OeMemberDto memberDto = memberAuthService.getCurrentMember();
		String id=noteService.createNote(memberDto, noteDto);
		return BsResponse.success(id);
	}
	
	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(Long lessonId,Integer status,Long courseId,@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize)
			throws Exception {
		OeMemberDto memberDto = memberAuthService.getCurrentMember();
		PageContent<OeNoteDto> page=noteService.findPageData(null,null,status,true,null,null,null,memberDto.getId(),lessonId,courseId,pageNum,pageSize);
		return BsResponse.success(page);
	}
	
	@GetMapping("/getDetail")
	@Operation(summary = "笔记详情")
	public BsResponse noteDetail(Long id) throws Exception{
		OeMemberDto memberDto = memberAuthService.getCurrentMember();
		return BsResponse.success(noteService.getDetail(id, memberDto));
	}
	
	@PostMapping("/delete")
	@Operation(summary = "删除")
	public BsResponse delete(Long id) throws Exception {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		noteService.enableNotByIdAndMemberId(id, memberDto.getId());
		return BsResponse.success();
	}
}

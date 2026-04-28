package cn.tgxy.oledu.controller.fe.mp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.oledu.dao.OeMemberLikeNoteDao;
import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.service.OeMemberAuthService;
import cn.tgxy.oledu.service.OeMemberLikeNoteService;
import cn.tgxy.tgbase.common.response.BsResponse;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/fe/mp/member/memberLikeNote")
public class OeFeMpMemberLikeNoteController {

	@Autowired
	private OeMemberLikeNoteDao memberLikeNoteDao;
	
	@Autowired
	private OeMemberAuthService memberAuthService;
	
	@Autowired
	private OeMemberLikeNoteService memberLikeNoteService;

	@GetMapping("/noteIdList")
	@Operation(summary = "noteId列表")
	public BsResponse noteIdList(Long lessonId) {
		OeMemberDto memberDto = memberAuthService.getCurrentMember();
		List<String> noteIdList=memberLikeNoteDao.getNoteIdsByMemberIdAndLessonId(memberDto.getId(),lessonId);
		return BsResponse.success(noteIdList);
	}
	
	@PostMapping("/likeNote")
	@Operation(summary = "喜欢笔记")
	public BsResponse likeNote(Long noteId) {
		OeMemberDto memberDto = memberAuthService.getCurrentMember();
		memberLikeNoteService.saveByMemberIdAndNoteId(memberDto.getId(),noteId);
		return BsResponse.success();
	}
	
	@PostMapping("/deleteLikeNote")
	@Operation(summary = "取消喜欢笔记")
	public BsResponse deleteLikeNote(Long noteId) {
		OeMemberDto memberDto = memberAuthService.getCurrentMember();
		memberLikeNoteService.deleteByMemberIdAndNoteId(memberDto.getId(),noteId);
		return BsResponse.success();
	}
	
	@GetMapping("/existsOrNot")
	@Operation(summary="根据noteId，判断成员是否点赞该笔记")
	public BsResponse existsOrNot(Long noteId) {
		boolean b=false;
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		b=memberLikeNoteService.existsByMemberIdAndNoteId(memberDto.getId(),noteId);
		if(!b) return BsResponse.success("false");
		return BsResponse.success("true");
	}
}

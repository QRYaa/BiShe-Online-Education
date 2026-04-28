package cn.tgxy.oledu.controller.fe.mp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.oledu.dao.OeMemberLikeReplyDao;
import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.service.OeMemberAuthService;
import cn.tgxy.oledu.service.OeMemberLikeReplyService;
import cn.tgxy.tgbase.common.response.BsResponse;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/fe/mp/member/memberLikeReply")
public class OeFeMpMemberLikeReplyController {

	@Autowired
	private OeMemberAuthService memberAuthService;
	
	@Autowired
	private OeMemberLikeReplyDao memberLikeReplyDao;
	
	@Autowired
	private OeMemberLikeReplyService memberLikeReplyService;
	
	@GetMapping("/replyIdList")
	@Operation(summary = "replyId列表")
	public BsResponse replyIdList(Long noteId) {
		OeMemberDto memberDto = memberAuthService.getCurrentMember();
		List<String> replyIdList=memberLikeReplyDao.getReplyIdsByMemberIdAndNoteId(memberDto.getId(),noteId);
		return BsResponse.success(replyIdList);
	}
	
	@PostMapping("/likeReply")
	@Operation(summary = "喜欢回复")
	public BsResponse likeReply(Long replyId) {
		OeMemberDto memberDto = memberAuthService.getCurrentMember();
		memberLikeReplyService.saveByMemberIdAndReplyId(memberDto.getId(),replyId);
		return BsResponse.success();
	}
	
	@PostMapping("/deleteLikeReply")
	@Operation(summary = "取消喜欢回复")
	public BsResponse deleteLikeReply(Long replyId) {
		OeMemberDto memberDto = memberAuthService.getCurrentMember();
		memberLikeReplyService.deleteByMemberIdAndReplyId(memberDto.getId(),replyId);
		return BsResponse.success();
	}
	
}

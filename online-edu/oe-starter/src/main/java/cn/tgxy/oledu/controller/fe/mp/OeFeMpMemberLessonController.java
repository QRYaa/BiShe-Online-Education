package cn.tgxy.oledu.controller.fe.mp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.service.OeMemberAuthService;
import cn.tgxy.oledu.service.OeMemberLessonService;
import cn.tgxy.tgbase.common.response.BsResponse;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/fe/mp/member/memberLesson")
public class OeFeMpMemberLessonController {
	
	@Autowired
	private OeMemberAuthService memberAuthService;
	
	@Autowired
	private OeMemberLessonService memberLessonService;

	@PostMapping("/changeWatchedStatus")
	@Operation(summary="修改观看状态")
	public BsResponse changeWatchedStatus(Integer watchedStatus,Long lessonId) {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		memberLessonService.changeWatchedStatus(watchedStatus,memberDto.getId(),lessonId);
		return BsResponse.success();
	}
	
	@PostMapping("/changeLastViewedTime")
	@Operation(summary="修改上次观看时间")
	public BsResponse changeLastViewedTime(Long lessonId) {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		memberLessonService.changeLastViewedTime(memberDto.getId(),lessonId);
		return BsResponse.success();
	}
	
	@PostMapping("/changeProgress")
	@Operation(summary="修改观看进度")
	public BsResponse changeProgress(Long lessonId,Integer progress) {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		memberLessonService.changeProgress(memberDto.getId(),lessonId,progress);
		return BsResponse.success();
	}
	
	@PostMapping("/changeDuration")
	@Operation(summary="修改观看时间")
	public BsResponse changeDuration(Long lessonId) {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		memberLessonService.changeDuration(memberDto.getId(),lessonId);
		return BsResponse.success();
	}
	
}

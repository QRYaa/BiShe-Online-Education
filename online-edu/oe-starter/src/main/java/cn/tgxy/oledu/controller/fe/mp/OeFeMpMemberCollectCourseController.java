package cn.tgxy.oledu.controller.fe.mp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.oledu.dto.OeMemberCollectCourseDto;
import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.service.OeMemberAuthService;
import cn.tgxy.oledu.service.OeMemberCollectCourseService;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/fe/mp/member/memberCollectCourse")
public class OeFeMpMemberCollectCourseController {
	
	@Autowired
	private OeMemberCollectCourseService memberCollectCourseService;
	
	@Autowired
	private OeMemberAuthService memberAuthService;
	
	@GetMapping("/existsOrNot")
	@Operation(summary="根据courseId，判断授权成员是否收藏该课程")
	public BsResponse existsOrNot(Long courseId) {
		boolean b=false;
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		b=memberCollectCourseService.existsByMemberIdAndCourseId(memberDto.getId(),courseId);
		if(!b) return BsResponse.success("false");
		return BsResponse.success("true");
	}
	
	@PostMapping("/collectCourse")
	@Operation(summary = "收藏课程")
	public BsResponse collectCourse(Long courseId) {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		memberCollectCourseService.addByMemberIdAndCourseId(memberDto.getId(),courseId);
		return BsResponse.success();
	}
	
	@PostMapping("/deleteCollectCourse")
	@Operation(summary = "取消收藏课程")
	public BsResponse deleteCollectCourse(Long courseId) {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		memberCollectCourseService.deleteByMemberIdAndCourseId(memberDto.getId(),courseId);
		return BsResponse.success();
	}
	
	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(String courseName,@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize)
			throws Exception {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		PageContent<OeMemberCollectCourseDto> pageContent=memberCollectCourseService.findPageData(memberDto.getId(), courseName, pageNum, pageSize);
		return BsResponse.success(pageContent);
	}

}

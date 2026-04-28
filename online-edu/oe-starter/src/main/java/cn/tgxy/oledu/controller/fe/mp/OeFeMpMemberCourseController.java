package cn.tgxy.oledu.controller.fe.mp;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.oledu.dto.OeMemberCourseDto;
import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.service.OeMemberAuthService;
import cn.tgxy.oledu.service.OeMemberCourseService;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/fe/mp/member/memberCourse")
public class OeFeMpMemberCourseController {
		
	@Autowired
	private OeMemberCourseService memberCourseService;
		
	@Autowired
	private OeMemberAuthService memberAuthService;
	
	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(String courseName,Integer noteNum,
			@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize)
			throws Exception {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		PageContent<OeMemberCourseDto> pageContent = memberCourseService.findPageData(memberDto.getId(), 
				courseName, null,noteNum,pageNum, pageSize);
		return BsResponse.success(pageContent);
	}
	
	/**
	 * 
	 * @param
	 * @return alicdn的url鉴权的临时地址
	 * @throws
	 */
	
//	@GetMapping("aliCdnLessonVideo")
//	@Operation(summary = "阿里CDN的课节视频")
//	public String aliCdnLessonVideo(Long lessonId) {
//		OeMemberDto memberDto=memberAuthService.getCurrentMemberByToken();
//		memberCourseService.checkMemberIdAndLessonId(memberDto.getId(),lessonId);
//		String path=lessonDao.getMediaUrlById(lessonId);
//		if(path!=null) {
//			Long timestamp=System.currentTimeMillis();
//			String rand=snowflake.nextIdStr();
//			long uid=0L;
//			
//			String sstring=String.format("%s-%d-%s-%d-%s", path,timestamp,rand,uid,privateKey);
//			String md5hash=DigestUtils.md5Hex(sstring);
//			
//			return String.format("https://%s/%s?auth_key={%d-%s-%d-%s}",videoBucketDomainName,path,timestamp,rand,uid,md5hash);
//		}
//		return null;
//	}
	
	@GetMapping("/getDetail")
	@Operation(summary = "学习课程详情")
	public BsResponse memberCourseDetail(@RequestParam(value = "courseCode", required = false) String courseCode,
		    @RequestParam(value = "courseId", required = false) Long courseId) throws Exception {
		OeMemberCourseDto memberCourseDto=null;
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		if(StringUtils.isNotEmpty(courseCode)) memberCourseDto=memberCourseService.findByMemberIdAndCourseCode(memberDto.getId(),courseCode);
		else if(courseId!=null) memberCourseDto=memberCourseService.findByMemberIdAndCourseId(memberDto.getId(),courseId);
		return BsResponse.success(memberCourseDto);
	}
	
	@GetMapping("/existsOrNot")
	@Operation(summary = "是否存在学习记录")
	public BsResponse existsMemberCourse(@RequestParam(value = "courseCode", required = false) String courseCode,
		    @RequestParam(value = "courseId", required = false) Long courseId) throws Exception {
		boolean b=false;
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		if(StringUtils.isNotEmpty(courseCode)) b=memberCourseService.existsByMemberIdAndCourseCode(memberDto.getId(), courseCode);
		else if(courseId!=null) b=memberCourseService.existsByMemberIdAndCourseId(memberDto.getId(), courseId);
		if(!b) return BsResponse.success("false");
		return BsResponse.success("true");
	}
	
	@PostMapping("/changeLessonId")
	@Operation(summary="修改上次学习的课节Id")
	public BsResponse changeLessonId(Long lessonId,Long courseId) {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		memberCourseService.changLessonId(lessonId,memberDto.getId(),courseId);
		return BsResponse.success();
	}
	
	@PostMapping("/changeLastViewTime")
	@Operation(summary="修改上次学习时间")
	public BsResponse changeLastViewTime(Long courseId) {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		memberCourseService.changeLastViewTime(memberDto.getId(),courseId);
		return BsResponse.success();
	}
	
	@PostMapping("/changeCompletionPercentage")
	@Operation(summary="修改完成百分比")
	public BsResponse changeCompletionPercentage(Long courseId) {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		memberCourseService.changeCompletionPercentage(memberDto.getId(),courseId);
		return BsResponse.success();
	}
		
}

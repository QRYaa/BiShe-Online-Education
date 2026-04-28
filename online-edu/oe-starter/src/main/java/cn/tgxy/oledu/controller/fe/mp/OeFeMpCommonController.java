package cn.tgxy.oledu.controller.fe.mp;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;

import cn.tgxy.oledu.constant.OeConstant;
import cn.tgxy.oledu.dao.OeLessonDao;
import cn.tgxy.oledu.dao.OeMemberIdCardAuditDao;
import cn.tgxy.oledu.dto.OeBannerDto;
import cn.tgxy.oledu.dto.OeBindTelDto;
import cn.tgxy.oledu.dto.OeCourseDto;
import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.dto.OeMemberTokenDto;
import cn.tgxy.oledu.dto.OeNewsDto;
import cn.tgxy.oledu.dto.OeTeacherDto;
import cn.tgxy.oledu.po.OeMemberIdCardAudit;
import cn.tgxy.oledu.service.OeBannerService;
import cn.tgxy.oledu.service.OeCourseService;
import cn.tgxy.oledu.service.OeMemberAuthService;
import cn.tgxy.oledu.service.OeMemberCourseService;
import cn.tgxy.oledu.service.OeMemberService;
import cn.tgxy.oledu.service.OeNewsService;
import cn.tgxy.oledu.service.OeTeacherService;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import cn.tgxy.tgbase.service.BsFileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 此路径提供所有人，包括游客和未绑定手机号的会员
 * 
 * @Description:
 */
@RestController
@RequestMapping("/fe/mp")
public class OeFeMpCommonController {

	@Autowired
	private OeLessonDao lessonDao;

	@Autowired
	private OeMemberIdCardAuditDao memberIdCardAuditDao;

	@Autowired
	private OeMemberAuthService memberAuthService;

	@Autowired
	private OeMemberCourseService memberCourseService;

	@Autowired
	private OeMemberService memberService;

	@Autowired
	private OeBannerService bannerService;

	@Autowired
	private OeNewsService newsService;

	@Autowired
	private OeCourseService courseService;

	@Autowired
	private OeTeacherService teacherService;

	@Autowired
	private BsFileService fileService;

	@GetMapping("/memberInfo")
	@Operation(summary = "当前会员信息")
	public BsResponse memberInfo() throws Exception {
		OeMemberDto dto = memberAuthService.getCurrentMember();
		return BsResponse.success(dto);
	}

	// 开发环境接口
	@PostMapping("loginTest")
	@Operation(summary = "登录测试")
	public BsResponse loginTest(String code, boolean userInfo) throws Exception {
		OeMemberTokenDto tokenDto = memberAuthService.memberLoginByWxTest(code, userInfo);
		return BsResponse.success(tokenDto);
	}

	@PostMapping("login")
	@Operation(summary = "登录")
	public BsResponse login(String code, boolean userInfo) throws Exception {
		OeMemberTokenDto tokenDto = memberAuthService.memberLoginByWx(code, userInfo);
		return BsResponse.success(tokenDto);
	}

	@GetMapping("/logout")
	@Operation(summary = "登出")
	public BsResponse logout() throws Exception {
		memberAuthService.memberLogout();
		return BsResponse.success();
	}

	@PostMapping("sendMobileSmsCode")
	@Operation(summary = "发送手机短信验证码")
	public BsResponse sendMobileSmsCode(@RequestBody OeBindTelDto bindPhoneDto) {
		memberService.sendMobileSmsCode(bindPhoneDto);
		return BsResponse.success();
	}

	@PostMapping("bindTel")
	@Operation(summary = "绑定手机号")
	public BsResponse bindTel(@RequestBody OeBindTelDto bindPhoneDto) throws Exception {
		OeMemberDto memberDto = memberAuthService.getCurrentMember();
		memberService.bindLoginMemTel(memberDto, bindPhoneDto);
		return BsResponse.success();
	}

	@GetMapping("bannerList")
	@Operation(summary = "横幅列表")
	public BsResponse bannerList() throws Exception {
		List<OeBannerDto> bannerDtos = bannerService.findEnabledAndValid();
		return BsResponse.success(bannerDtos);
	}

	@GetMapping("newsDetail")
	@Operation(summary = "新闻详情")
	public BsResponse newsDetail(String code) throws Exception {
		OeNewsDto newsDto = newsService.findByCodeAndEnable(code);
		return BsResponse.success(newsDto);
	}

	@GetMapping("courseDetail")
	@Operation(summary = "课程详情")
	public BsResponse courseDetail(@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "id", required = false) Long id) throws Exception {
		OeCourseDto courseDto = null;
		if (StringUtils.isNotEmpty(code))
			courseDto = courseService.findByCodeAndEnable(code);
		else if (id != null)
			courseDto = courseService.findByIdAndEnable(id);
		return BsResponse.success(courseDto);
	}

	@GetMapping("/coursePage")
	@Operation(summary = "课程分页")
	public BsResponse page(String courseTypeCode, String teacherCode,
			@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize)
			throws Exception {
		PageContent<OeCourseDto> pageContent = courseService.findPageData(null, null, null, true, null, courseTypeCode,
				null, teacherCode, pageNum, pageSize);
		return BsResponse.success(pageContent);
	}

	@GetMapping("/teacherDetail")
	@Operation(summary = "讲师的基本信息")
	public BsResponse teacherDetail(String code) throws Exception {
		OeTeacherDto teacherDto = teacherService.findByCode(code);
		return BsResponse.success(teacherDto);
	}

	@GetMapping("/getLessonPathById")
	@Operation(summary = "根据lessonId获取路径")
	public BsResponse getLessonPathById(Long lessonId) {
		return BsResponse.success(lessonDao.getMediaUrlById(lessonId));
	}

	@GetMapping("/lessonVideo")
	@Operation(summary = "试看课节视频")
	public void lessonVideo(HttpServletRequest request, HttpServletResponse response, Long lessonId) throws Exception {
		String path = lessonDao.getMediaUrlByIdAndPreviewAble(lessonId);
		if (path != null) {
			try {
				File file = fileService.getFile(OeConstant.BUCKET_LESSON, path, null);
				if (file != null && file.exists()) {
					// 新增范围请求处理
					long fileLength = file.length();
					String rangeHeader = request.getHeader("Range");

					// 声明支持范围请求
					response.setHeader("Accept-Ranges", "bytes");
					response.setHeader("Content-Length", String.valueOf(fileLength));

					if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
						// 解析Range参数:处理单个范围请求
						String rangeValue = rangeHeader.replace("bytes=", "");
						String[] ranges = rangeValue.split("-");
						long start = Long.parseLong(ranges[0]);
						long end = ranges.length > 1 ? Long.parseLong(ranges[1]) : fileLength - 1;

						// 设置部分内容响应头
						response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
						response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
						response.setContentLength((int) (end - start + 1));

						// 分块传输
						try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
							raf.seek(start);
							byte[] buffer = new byte[8192]; // 优化缓冲区大小
							int bytesRead;
							while ((bytesRead = raf.read(buffer)) != -1) {
								// 控制传输范围
								if (start + bytesRead > end) {
									bytesRead = (int) (end - start + 1);
								}
								response.getOutputStream().write(buffer, 0, bytesRead);
								start += bytesRead;
								if (start > end)
									break;
							}
						}
					} else {
						// 完整文件传输
						Files.copy(file.toPath(), response.getOutputStream());
					}
				}
			} catch (AsyncRequestNotUsableException ae) {

			} catch (Exception e) {
				// 异常处理...
			}
		}
	}

	/**
	 * 根据OeMemberCourse来判断是否响应视频资源
	 * 
	 * @param
	 * @return
	 * @throws
	 */
	@GetMapping("/mcLessonVideo")
	@Operation(summary = "课节视频")
	public void mcLessonVideo(HttpServletRequest request, HttpServletResponse response, Long lessonId)
			throws Exception {
		OeMemberDto memberDto = memberAuthService.getCurrentMemberByToken();
		memberCourseService.checkMemberIdAndLessonId(memberDto.getId(), lessonId);
		String path = lessonDao.getMediaUrlById(lessonId);

		if (path != null) {
			try {
				File file = fileService.getFile(OeConstant.BUCKET_LESSON, path, null);
				if (file != null && file.exists()) {
					// 新增范围请求处理
					long fileLength = file.length();
					String rangeHeader = request.getHeader("Range");

					// 声明支持范围请求
					response.setHeader("Accept-Ranges", "bytes");
					response.setHeader("Content-Length", String.valueOf(fileLength));

					if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
						// 解析Range参数:处理单个范围请求
						String rangeValue = rangeHeader.replace("bytes=", "");
						String[] ranges = rangeValue.split("-");
						long start = Long.parseLong(ranges[0]);
						long end = ranges.length > 1 ? Long.parseLong(ranges[1]) : fileLength - 1;

						// 设置部分内容响应头
						response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
						response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
						response.setContentLength((int) (end - start + 1));

						// 分块传输
						try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
							raf.seek(start);
							byte[] buffer = new byte[8192]; // 优化缓冲区大小
							int bytesRead;
							while ((bytesRead = raf.read(buffer)) != -1) {
								// 控制传输范围
								if (start + bytesRead > end) {
									bytesRead = (int) (end - start + 1);
								}
								response.getOutputStream().write(buffer, 0, bytesRead);
								start += bytesRead;
								if (start > end)
									break;
							}
						}
					} else {
						// 完整文件传输
						Files.copy(file.toPath(), response.getOutputStream());
					}
				}
			} catch (AsyncRequestNotUsableException ae) {

			} catch (Exception e) {
				// 异常处理...
			}
		}
	}

	@GetMapping("/memberIdCardAuditIdCardImg")
	public void idCardImg(HttpServletRequest request, HttpServletResponse response, Long memberIdCardAuditId,
			String type) throws Exception {
		OeMemberDto memberDto = memberAuthService.getCurrentMemberByToken();
		OeMemberIdCardAudit memberIdCardAudit = memberIdCardAuditDao.findById(memberIdCardAuditId).orElse(null);
		if (memberIdCardAudit == null || !memberIdCardAudit.getMemberId().equals(memberDto.getId())) {
			return;
		}
		String path = null;
		if (type.equals("front")) {
			path = memberIdCardAudit.getIdCardFrontImageUrl();
		} else if (type.equals("back")) {
			path = memberIdCardAudit.getIdCardBackImageUrl();
		}
		if (path != null) {
			try {
				File file = fileService.getFile(OeConstant.BUCKET_PRI, path, null);
				if (file != null && file.exists()) {
					// 完整文件传输
					Files.copy(file.toPath(), response.getOutputStream());
				}
			} catch (AsyncRequestNotUsableException ae) {

			} catch (Exception e) {
				// 异常处理...
			}
		}
	}

	/**
	 * 
	 * @param
	 * @return alicdn的url鉴权的临时地址
	 * @throws
	 */
//	@GetMapping("/aliCdnLessonVideo")
//	@Operation(summary = "试看cdn的课节视频")
//	public String aliCdnLessonVideo(Long lessonId) {
//		String path=lessonDao.getMediaUrlByIdAndPreviewAble(lessonId);
//		if (path != null) {
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
}

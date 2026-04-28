package cn.tgxy.oledu.controller.api;

import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.oledu.constant.OeConstant;
import cn.tgxy.oledu.dao.OeLessonDao;
import cn.tgxy.oledu.dao.OeMemberIdCardAuditDao;
import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.po.OeLesson;
import cn.tgxy.oledu.po.OeMemberIdCardAudit;
import cn.tgxy.oledu.service.OeMemberAuthService;
import cn.tgxy.oledu.service.OeMemberCourseService;
import cn.tgxy.tgbase.common.exception.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/aliCdn")
public class BsAliCdnController {
	
	private static Logger log = LoggerFactory.getLogger(BsAliCdnController.class);
	
	@Autowired
	private OeLessonDao lessonDao;
	
	@Autowired
	private OeMemberIdCardAuditDao memberIdCardAuditDao;

	@Autowired
	private OeMemberCourseService memberCourseService;
		
	@Autowired
	private OeMemberAuthService memberAuthService;
	
	@GetMapping("/lesson/checkToken")
	@Operation(summary = "对lessonBucket检查token")
	public ResponseEntity<Void> checkTokenInLessonBucket(HttpServletRequest request,String token) throws Exception {
		log.info("/lesson/checkToken接口被调用");
		String url=request.getHeader("ali-origin-real-url");
		if(url!=null) {
			String urlPathAndToken=url.replace("https://test-online-video.tgxy.com.cn/","");
			String urlPath=urlPathAndToken.replace("?token="+token,"");
			String path=URLDecoder.decode(urlPath,"UTF-8");
			
			OeLesson lesson=lessonDao.findByMediaUrl(path);
			if(lesson.getPreviewAble()==true) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
			
			Long lessonId=lesson.getId();	
			OeMemberDto memberDto=memberAuthService.getCurrentMemberByToken();
			try {
				memberCourseService.checkMemberIdAndLessonId(memberDto.getId(),lessonId);
			}
			catch(ServiceException e) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
	
	@GetMapping("/pri/checkToken")
	@Operation(summary = "对priBucket检查token")
	public ResponseEntity<Void> checkTokenInPriBucket(HttpServletRequest request,String token) throws Exception {
		log.info("/pri/checkToken接口被调用");
		String url=request.getHeader("ali-origin-real-url");
		if(url!=null) {
			String urlPathAndToken=url.replace("https://test-pri.tgxy.com.cn/","");
			String urlPath=urlPathAndToken.replace("?token="+token,"");
			String path=URLDecoder.decode(urlPath,"UTF-8");
			OeMemberDto memberDto=memberAuthService.getCurrentMemberByToken();
			
			if(path.contains(OeConstant.ID_CARD_FRONT_IMG_FOLDER+'/')) {
				OeMemberIdCardAudit memberIdCardAudit=memberIdCardAuditDao.findByIdCardFrontImageUrl(path);
				if(memberIdCardAudit.getMemberId().equals(memberDto.getId())) {
					return new ResponseEntity<>(HttpStatus.OK);
				}
			}
			
			else if(path.contains(OeConstant.ID_CARD_BACK_IMG_FOLDER+'/')) {
				OeMemberIdCardAudit memberIdCardAudit=memberIdCardAuditDao.findByIdCardBackImageUrl(path);
				if(memberIdCardAudit.getMemberId().equals(memberDto.getId())) {
					return new ResponseEntity<>(HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
	
}

package cn.tgxy.oledu.controller.fe.mp;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.tgxy.oledu.constant.OeConstant;
import cn.tgxy.oledu.dao.OeMemberIdCardAuditDao;
import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.dto.OeMemberIdCardAuditDto;
import cn.tgxy.oledu.dto.OeMemberIdCardDto;
import cn.tgxy.oledu.service.OeMemberAuthService;
import cn.tgxy.oledu.service.OeMemberIdCardAuditService;
import cn.tgxy.oledu.service.OeMemberIdCardService;
import cn.tgxy.oledu.service.OeMemberService;
import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import cn.tgxy.tgbase.constant.BsErrorConstant;
import cn.tgxy.tgbase.service.BsFileService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/fe/mp/member")
public class OeFeMpMemberController {

	@Autowired
	private OeMemberIdCardAuditDao memberIdCardAuditDao;
	
	@Autowired
	private BsFileService fileService;
	
	@Autowired
	private OeMemberAuthService memberAuthService;
	
	@Autowired
	private OeMemberService memberService;
	
	@Autowired
	private OeMemberIdCardService memberIdCardService;
	
	@Autowired
	private OeMemberIdCardAuditService memberIdCardAuditService;
	
	@Value("${uniappEditorImgPre}")
	private String uniappEditorImgPre = null;
	
	@PostMapping(value = "/uploadImg")
	@Operation(summary = "上传头像")
	public BsResponse uploadImg(MultipartFile file) throws Exception {
		String path = "";
		if (file != null) {
			String originalFilename = file.getOriginalFilename();
			if (StringUtils.isNotEmpty(originalFilename)) {
				boolean isImg = fileService.checkFileType(file, BsCoreConstant.IMG);
				if (isImg) {
					path = fileService.autoRenameUploadFile(OeConstant.BUCKET_PUB, file, OeConstant.MEM_AVATAR_FOLDER);
				} else {
					throw new ServiceException(BsErrorConstant.FILE_TYPE_ERROR);
				}
			}
		}
		return BsResponse.success(path);
	}
	
	@PostMapping(value = "/uploadIdCardFrontImage")
	@Operation(summary = "上传身份证正面")
	public BsResponse uploadIdCardFrontImage(MultipartFile file) throws Exception {
		String path = "";
		if (file != null) {
			String originalFilename = file.getOriginalFilename();
			if (StringUtils.isNotEmpty(originalFilename)) {
				boolean isImg = fileService.checkFileType(file, BsCoreConstant.IMG);
				if (isImg) {
					path = fileService.autoRenameUploadFile(OeConstant.BUCKET_PRI, file, OeConstant.ID_CARD_FRONT_IMG_FOLDER);
				} else {
					throw new ServiceException(BsErrorConstant.FILE_TYPE_ERROR);
				}
			}
		}
		return BsResponse.success(path);
	}
	
	@PostMapping(value = "/uploadIdCardBackImage")
	@Operation(summary = "上传身份证背面")
	public BsResponse uploadIdCardBackImage(MultipartFile file) throws Exception {
		String path = "";
		if (file != null) {
			String originalFilename = file.getOriginalFilename();
			if (StringUtils.isNotEmpty(originalFilename)) {
				boolean isImg = fileService.checkFileType(file, BsCoreConstant.IMG);
				if (isImg) {
					path = fileService.autoRenameUploadFile(OeConstant.BUCKET_PRI, file, OeConstant.ID_CARD_BACK_IMG_FOLDER);
				} else {
					throw new ServiceException(BsErrorConstant.FILE_TYPE_ERROR);
				}
			}
		}
		return BsResponse.success(path);
	}
	
	/**
	 * 
	 * @param
	 * @return 返回的是一个全路径
	 * @throws
	 */
	@PostMapping(value = "/uploadNoteImg")
	@Operation(summary = "上传笔记图片")
	public BsResponse uploadNoteImg(MultipartFile file) throws Exception {
		String path = "";
		if (file != null) {
			String originalFilename = file.getOriginalFilename();
			if (StringUtils.isNotEmpty(originalFilename)) {
				boolean isImg = fileService.checkFileType(file, BsCoreConstant.IMG);
				if (isImg) {
					path = fileService.autoRenameUploadFile(OeConstant.BUCKET_PUB, file, OeConstant.NOTE_IMG_FOLDER);
				} else {
					throw new ServiceException(BsErrorConstant.FILE_TYPE_ERROR);
				}
			}
		}
		return BsResponse.success(uniappEditorImgPre+path);
	}
	
	@PostMapping(value="/updateAvatar")
	@Operation(summary = "更新头像")
	public BsResponse updateAvatar(String avatar) throws Exception{
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		memberService.updateLoginMemAvatar(memberDto, avatar);
		return BsResponse.success();
	}
	
	@PutMapping(value = "/update")
	@Operation(summary = "修改")
	public BsResponse update(@RequestBody OeMemberDto memberDto) throws Exception {
		OeMemberDto loginMemberDto=memberAuthService.getCurrentMember();
		memberService.updateLoginMem(memberDto, loginMemberDto);
		return BsResponse.success();
	}
	
	@GetMapping(value="/idCardInfo")
	@Operation(summary = "身份证信息")
	public BsResponse idCardInfo() throws Exception {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		OeMemberIdCardDto memberIdCardDto=memberIdCardService.findMaskInfoByMemberId(memberDto.getId());
		return BsResponse.success(memberIdCardDto);
	}
	
	@PostMapping(value="/submitIdCardAudit")
	@Operation(summary = "提交身份证审核资料")
	public BsResponse submitIdCardAudit(@RequestBody OeMemberIdCardAuditDto memberIdCardAuditDto) throws Exception {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		memberIdCardAuditService.submitAudit(memberDto,memberIdCardAuditDto);
		return BsResponse.success();
	}
	
	@GetMapping(value="/getAudit")
	@Operation(summary = "获取最近审核非成功的资料")
	public BsResponse getAudit() throws Exception {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		return BsResponse.success(memberIdCardAuditService.findByMemberIdAndNotSuccess(memberDto.getId()));
	}
	
	@GetMapping(value="/getAuditStatus")
	@Operation(summary = "获取最近审核资料状态")
	public BsResponse getAuditStatus() throws Exception {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		return BsResponse.success(memberIdCardAuditDao.getStatusByMemberId(memberDto.getId()));
	}
	
	@PostMapping(value="/unbindIdCard")
	@Operation(summary = "解绑身份证")
	public BsResponse unbindIdCard() throws Exception {
		OeMemberDto memberDto=memberAuthService.getCurrentMember();
		memberIdCardService.unbindIdCard(memberDto.getId());
		return BsResponse.success();
	}
		
}

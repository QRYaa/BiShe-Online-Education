package cn.tgxy.oledu.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.oledu.constant.OeConstant;
import cn.tgxy.oledu.constant.OeErrorConstant;
import cn.tgxy.oledu.dao.OeMemberDao;
import cn.tgxy.oledu.dao.OeMemberExtDao;
import cn.tgxy.oledu.dto.OeBindTelDto;
import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.po.OeMember;
import cn.tgxy.oledu.service.OeMemberService;
import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.util.ServletUtils;
import cn.tgxy.tgbase.service.RedisService;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class OeMemberServiceImpl extends BsBaseServiceImpl<OeMember, OeMemberDto> implements OeMemberService {

	@Autowired
	private OeMemberDao memberDao;
	
	@Autowired
	private OeMemberExtDao memberExtDao;
	
	@Autowired
	private RedisService redisService;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = memberDao;
	}

	@Override
	public PageContent<OeMemberDto> findPageData(String name, String code, String tel, Integer gender, Integer areaId,
			Integer enable, String idCardName, String idCardNumber, Integer pageNum, Integer pageSize)
			throws Exception {
		PageContent<OeMember> page=memberExtDao.findPageContent(name, code, tel, gender, areaId, enable, idCardName, idCardNumber, pageNum, pageSize);
		PageContent<OeMemberDto> pageData=new PageContent<>(transToDtoList(page.getContent()), page.getTotalItems(), page.getTotalPages(), pageNum, pageSize);
		return pageData;
	}
	
	@Override
	public void save(OeMemberDto dto) throws Exception {
		String code = dto.getCode();
		if (StringUtils.isNotBlank(code)) {// 判断编码是否有重复
			if (memberDao.existsByCode(code)) {
				throw new ServiceException(OeErrorConstant.CODE_EXIST);
			}
		}
		String tel = dto.getTel();
		if (StringUtils.isNotBlank(tel) && memberDao.existsByTel(tel)) {
			throw new ServiceException(OeErrorConstant.MEM_TEL_EXIST);
		}

		if (dto.getRegisterDate() == null) {
			dto.setRegisterDate(new Date());
		}

		super.save(dto);
	}

	@Override
	public void update(OeMemberDto memberDto) throws Exception {
		OeMember member = memberDao.findById(memberDto.getId()).orElse(null);

		if (member == null) {
			return;
		}

		String code = memberDto.getCode();
		if (StringUtils.isBlank(code)) {// 修改时允许修改成其他学号，不能修改为空,因为code可能作为外键
			code = member.getCode();
			memberDto.setCode(code);
		} else {
			if (!code.equals(member.getCode()) && memberDao.existsByCode(code)) {
				throw new ServiceException(OeErrorConstant.CODE_EXIST);
			}
		}

		String tel = memberDto.getTel();
		if (StringUtils.isNotBlank(tel) && !tel.equals(member.getTel()) && memberDao.existsByTel(tel)) {
			throw new ServiceException(OeErrorConstant.MEM_TEL_EXIST);
		}

		super.update(memberDto);
	}

	@Override
	public OeMemberDto findByWeixinOpenId(String openId) throws Exception {
		OeMember member = memberDao.findByWeixinOpenId(openId);
		return transToDto(member);
	}

//------------------------------ToC端接口------------------------------------
	// TODO: smsService的实现
	@Override
	public void sendMobileSmsCode(OeBindTelDto bindPhoneDto) {
		if (memberDao.existsByTel(bindPhoneDto.getTel())) {
			throw new ServiceException(OeErrorConstant.MEM_TEL_EXIST);
		}
		// TODO: 待完成
	}

	@Override
	public void bindLoginMemTel(OeMemberDto memberDto, OeBindTelDto bindPhoneDto) throws Exception {
		// TODO: 短信验证码的判断
		// TODO: 更新会话（save，update，delte也需要更新会话），需要引用authservice，且要解决所带来的循环依赖问题
		memberDao.updateTelById(bindPhoneDto.getTel(), memberDto.getId());

	}
	
	@Override
	public void updateLoginMemAvatar(OeMemberDto memberDto,String avatar) throws Exception {
		memberDao.updateAvatarById(avatar,memberDto.getId());
		OeMember member=memberDao.findById(memberDto.getId()).orElse(null);
		redisService.set(OeConstant.MEM_TOKEN_PREFIX + getToken(), transToDto(member), OeConstant.EXPIRE_TIME);
	}
	
	@Override
	public void updateLoginMem(OeMemberDto memberDto,OeMemberDto loginMemberDto) throws Exception {
		if(!memberDto.getId().equals(loginMemberDto.getId())) return;
		update(memberDto);
		redisService.set(OeConstant.MEM_TOKEN_PREFIX + getToken(), memberDto, OeConstant.EXPIRE_TIME);
	}
	
	private String getToken() {
		String token = null;
		HttpServletRequest request = ServletUtils.getRequest();
		if (request != null) {
			token = request.getHeader(OeConstant.TOKEN_HEADER);
		}
		return token;
	}

}

package cn.tgxy.oledu.service;

import cn.tgxy.oledu.dto.OeBindTelDto;
import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.po.OeMember;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.service.BsBaseService;

public interface OeMemberService extends BsBaseService<OeMember, OeMemberDto>{

	OeMemberDto findByWeixinOpenId(String openId) throws Exception;

	void sendMobileSmsCode(OeBindTelDto bindPhoneDto);

	void bindLoginMemTel(OeMemberDto memberDto,OeBindTelDto bindPhoneDto) throws Exception;

	void updateLoginMemAvatar(OeMemberDto memberDto, String avatar) throws Exception;

	void updateLoginMem(OeMemberDto memberDto,OeMemberDto loginMemberDto) throws Exception;

	PageContent<OeMemberDto> findPageData(String name, String code, String tel, Integer gender, Integer areaId,
            Integer enable, String idCardName, String idCardNumber, Integer pageNum, Integer pageSize)
            throws Exception;
}

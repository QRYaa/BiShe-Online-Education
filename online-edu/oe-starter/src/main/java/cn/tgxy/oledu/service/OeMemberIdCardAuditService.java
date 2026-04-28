package cn.tgxy.oledu.service;

import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.dto.OeMemberIdCardAuditDto;
import cn.tgxy.oledu.po.OeMemberIdCardAudit;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dto.BsUserDto;
import cn.tgxy.tgbase.service.BsBaseService;

public interface OeMemberIdCardAuditService extends BsBaseService<OeMemberIdCardAudit, OeMemberIdCardAuditDto>{

	void submitAudit(OeMemberDto memberDto,OeMemberIdCardAuditDto memberIdCardAuditDto) throws Exception;
	
	OeMemberIdCardAuditDto findByMemberIdAndNotSuccess(Long memberId) throws Exception;
	
	PageContent<OeMemberIdCardAuditDto> findPageData(String memberCode, Integer status, String idCardNumber,
            String idCardRealName, Integer pageNum, Integer pageSize) throws Exception;

	void passAudit(BsUserDto currentUseDto,OeMemberIdCardAuditDto auditDto) throws Exception;

	void rejectAudit(BsUserDto currentUseDto,OeMemberIdCardAuditDto auditDto) throws Exception;

}

package cn.tgxy.oledu.service;

import cn.tgxy.tgbase.service.BsBaseService;
import cn.tgxy.oledu.po.OeMemberIdCard;
import cn.tgxy.oledu.dto.OeMemberIdCardDto;

public interface OeMemberIdCardService extends BsBaseService<OeMemberIdCard, OeMemberIdCardDto>{

	OeMemberIdCardDto findMaskInfoByMemberId(Long memberId) throws Exception;

	OeMemberIdCardDto findByMemberId(Long memberId) throws Exception;

	void unbindIdCard(Long memberId) throws Exception;

}

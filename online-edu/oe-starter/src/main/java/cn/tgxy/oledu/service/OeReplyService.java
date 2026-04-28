package cn.tgxy.oledu.service;

import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.service.BsBaseService;
import cn.tgxy.oledu.po.OeReply;
import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.dto.OeReplyDto;

public interface OeReplyService extends BsBaseService<OeReply, OeReplyDto>{

	void switchEnable(OeReplyDto replyDto);

	PageContent<OeReplyDto> findPageData(Long noteId, String trackId, String senderCode, Boolean enable, Integer pageNum,
			Integer pageSize) throws Exception;

	OeReplyDto createReply(OeMemberDto memberDto, OeReplyDto replyDto) throws Exception;

	void enableNotByIdAndSenderId(Long id, Long senderId) throws Exception;

}

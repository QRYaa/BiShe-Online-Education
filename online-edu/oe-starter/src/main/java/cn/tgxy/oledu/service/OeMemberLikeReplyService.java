package cn.tgxy.oledu.service;

import cn.tgxy.tgbase.service.BsBaseService;
import cn.tgxy.oledu.po.OeMemberLikeReply;
import cn.tgxy.oledu.dto.OeMemberLikeReplyDto;

public interface OeMemberLikeReplyService extends BsBaseService<OeMemberLikeReply, OeMemberLikeReplyDto>{

	void saveByMemberIdAndReplyId(Long id, Long replyId);

	void deleteByMemberIdAndReplyId(Long id, Long replyId);

}

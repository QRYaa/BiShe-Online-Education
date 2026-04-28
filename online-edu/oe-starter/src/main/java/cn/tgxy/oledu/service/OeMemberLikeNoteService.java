package cn.tgxy.oledu.service;

import cn.tgxy.oledu.dto.OeMemberLikeNoteDto;
import cn.tgxy.oledu.po.OeMemberLikeNote;
import cn.tgxy.tgbase.service.BsBaseService;

public interface OeMemberLikeNoteService extends BsBaseService<OeMemberLikeNote,OeMemberLikeNoteDto> {

	void saveByMemberIdAndNoteId(Long id, Long noteId);

	void deleteByMemberIdAndNoteId(Long id, Long noteId);

	boolean existsByMemberIdAndNoteId(Long id, Long noteId);

}

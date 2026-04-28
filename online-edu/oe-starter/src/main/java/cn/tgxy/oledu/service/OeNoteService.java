package cn.tgxy.oledu.service;

import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.service.BsBaseService;
import cn.tgxy.oledu.po.OeNote;

import java.util.Date;

import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.dto.OeNoteDto;

public interface OeNoteService extends BsBaseService<OeNote, OeNoteDto>{

	void switchEnable(OeNoteDto noteDto);

	PageContent<OeNoteDto> findPageData(Date startTime,Date endTime,Integer status,Boolean enable,String memberCode, String courseName, String lessonName, Long memberId,Long lessonId,Long courseId,Integer pageNum,
			Integer pageSize) throws Exception;

	String createNote(OeMemberDto memberDto, OeNoteDto noteDto) throws Exception;

	OeNoteDto getDetail(Long noteId, OeMemberDto currentMemberDto) throws Exception;

	void enableNotByIdAndMemberId(Long id, Long memberId) throws Exception;

}

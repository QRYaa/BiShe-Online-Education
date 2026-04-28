package cn.tgxy.oledu.dto;

import java.io.Serializable;

public class OeMemberLikeReplyDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private Long memberId;

	private Long replyId;

	private Long noteId;


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getMemberId() {
		return this.memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	
	public Long getReplyId() {
		return this.replyId;
	}

	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}
	
	public Long getNoteId() {
		return this.noteId;
	}

	public void setNoteId(Long noteId) {
		this.noteId = noteId;
	}
	
}

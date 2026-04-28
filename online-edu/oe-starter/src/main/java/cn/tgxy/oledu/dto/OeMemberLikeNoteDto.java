package cn.tgxy.oledu.dto;

import java.io.Serializable;

public class OeMemberLikeNoteDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long memberId;
	private Long noteId;
	private Long lessonId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Long getNoteId() {
		return noteId;
	}
	public void setNoteId(Long noteId) {
		this.noteId = noteId;
	}
	public Long getLessonId() {
		return lessonId;
	}
	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}
}

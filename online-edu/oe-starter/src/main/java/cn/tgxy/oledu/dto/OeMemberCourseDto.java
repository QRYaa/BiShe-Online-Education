package cn.tgxy.oledu.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OeMemberCourseDto {
	private Long id;

	private Long memberId;

	private Long courseId;

	private Long lessonId;
	
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastViewTime;

	private Integer completionPercentage;
	
	private Integer noteNum;
	
	private OeCourseDto courseDto;
	
	private List<OeMemberLessonDto> memberLessonDtos;

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
	
	public Long getCourseId() {
		return this.courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	
	public Long getLessonId() {
		return this.lessonId;
	}

	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}
	
	public Date getLastViewTime() {
		return lastViewTime;
	}

	public void setLastViewTime(Date lastViewTime) {
		this.lastViewTime = lastViewTime;
	}

	public Integer getCompletionPercentage() {
		return this.completionPercentage;
	}

	public void setCompletionPercentage(Integer completionPercentage) {
		this.completionPercentage = completionPercentage;
	}
	
	public Integer getNoteNum() {
		return noteNum;
	}

	public void setNoteNum(Integer noteNum) {
		this.noteNum = noteNum;
	}

	public OeCourseDto getCourseDto() {
		return courseDto;
	}

	public void setCourseDto(OeCourseDto courseDto) {
		this.courseDto = courseDto;
	}

	public List<OeMemberLessonDto> getMemberLessonDtos() {
		return memberLessonDtos;
	}

	public void setMemberLessonDtos(List<OeMemberLessonDto> memberLessonDtos) {
		this.memberLessonDtos = memberLessonDtos;
	}

}

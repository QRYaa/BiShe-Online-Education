package cn.tgxy.oledu.dto;

import java.io.Serializable;

public class OeMemberCollectCourseDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private Long memberId;

	private Long courseId;

	private OeCourseDto courseDto;

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

	public OeCourseDto getCourseDto() {
		return courseDto;
	}

	public void setCourseDto(OeCourseDto courseDto) {
		this.courseDto = courseDto;
	}
	
}

package cn.tgxy.oledu.dto;

import java.util.List;

public class OeChapterDto {
	private Long id;

	private Long courseId;

	private Integer sort;

	private String name;

	private String description;

	private String remark;

	private List<OeLessonDto> lessonDtoList;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getCourseId() {
		return this.courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	
	public Integer getSort() {
		return this.sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<OeLessonDto> getLessonDtoList() {
		return lessonDtoList;
	}

	public void setLessonDtoList(List<OeLessonDto> lessonDtoList) {
		this.lessonDtoList = lessonDtoList;
	}
	
}

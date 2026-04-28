package cn.tgxy.oledu.dto;

import java.io.Serializable;
import java.util.List;

public class OeTeacherDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String code;

	private String avatar;

	private String name;

	private Integer gender;

	private String tel;

	private String description;

	private String content;

	private List<Long> tagIdList;
	
	private List<OeTeacherTagDto> tagDtoList;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getGender() {
		return this.gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}
	
	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Long> getTagIdList() {
		return tagIdList;
	}

	public void setTagIdList(List<Long> tagIdList) {
		this.tagIdList = tagIdList;
	}

	public List<OeTeacherTagDto> getTagDtoList() {
		return tagDtoList;
	}

	public void setTagDtoList(List<OeTeacherTagDto> tagDtoList) {
		this.tagDtoList = tagDtoList;
	}
	
}

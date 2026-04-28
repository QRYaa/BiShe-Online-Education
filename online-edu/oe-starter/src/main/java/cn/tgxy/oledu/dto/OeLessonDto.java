package cn.tgxy.oledu.dto;

public class OeLessonDto {
	private Long id;

	private Long chapterId;

	private String name;

	private Integer type;

	private String content;

	private String mediaUrl;
	
	private String mediaName;
	
	private Integer duration;

	private Integer sort;
	
	private Boolean previewAble;

	private String remark;
	
	private Integer noteNum;
	
	private Integer replyNum;

	private Integer enable;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getChapterId() {
		return this.chapterId;
	}

	public void setChapterId(Long chapterId) {
		this.chapterId = chapterId;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getMediaUrl() {
		return this.mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	
	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getSort() {
		return this.sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Boolean getPreviewAble() {
		return previewAble;
	}

	public void setPreviewAble(Boolean previewAble) {
		this.previewAble = previewAble;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Integer getNoteNum() {
		return noteNum;
	}

	public void setNoteNum(Integer noteNum) {
		this.noteNum = noteNum;
	}

	public Integer getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(Integer replyNum) {
		this.replyNum = replyNum;
	}

	public Integer getEnable() {
		return this.enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	
}

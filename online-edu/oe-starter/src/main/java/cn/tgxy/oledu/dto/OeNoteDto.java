package cn.tgxy.oledu.dto;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OeNoteDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private Long memberId;
	
	private String memberName;
	
	private String memberAvatar;

	private Long lessonId;
	
	private String courseName;
	
	private String chapterName;
	
	private Integer chapterSort;
	
	private String lessonName;
	
	private Integer lessonSort;
	
	private String content;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	private Integer likeNum;

	private Integer replyNum;

	private Integer status;

	private Boolean enable;


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
	
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberAvatar() {
		return memberAvatar;
	}

	public void setMemberAvatar(String memberAvatar) {
		this.memberAvatar = memberAvatar;
	}

	public Long getLessonId() {
		return this.lessonId;
	}

	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}
	
	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public Integer getChapterSort() {
		return chapterSort;
	}

	public void setChapterSort(Integer chapterSort) {
		this.chapterSort = chapterSort;
	}

	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

	public Integer getLessonSort() {
		return lessonSort;
	}

	public void setLessonSort(Integer lessonSort) {
		this.lessonSort = lessonSort;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Integer getLikeNum() {
		return this.likeNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}
	
	public Integer getReplyNum() {
		return this.replyNum;
	}

	public void setReplyNum(Integer replyNum) {
		this.replyNum = replyNum;
	}
	
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Boolean getEnable() {
		return this.enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	
}

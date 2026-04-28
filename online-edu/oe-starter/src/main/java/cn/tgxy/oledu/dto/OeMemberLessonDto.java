package cn.tgxy.oledu.dto;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class OeMemberLessonDto {
	private Long id;

	private Long memberId;

	private Long lessonId;
	
	private Long memberCourseId;

	private Integer progress;
	
	private Integer duration;

	private Integer watchedStatus;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastViewedTime;

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
	
	public Long getLessonId() {
		return this.lessonId;
	}

	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}
	
	public Long getMemberCourseId() {
		return memberCourseId;
	}

	public void setMemberCourseId(Long memberCourseId) {
		this.memberCourseId = memberCourseId;
	}

	public Integer getProgress() {
		return this.progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}
	
	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getWatchedStatus() {
		return this.watchedStatus;
	}

	public void setWatchedStatus(Integer watchedStatus) {
		this.watchedStatus = watchedStatus;
	}
	
	public Date getLastViewedTime() {
		return this.lastViewedTime;
	}

	public void setLastViewedTime(Date lastViewedTime) {
		this.lastViewedTime = lastViewedTime;
	}
	
}

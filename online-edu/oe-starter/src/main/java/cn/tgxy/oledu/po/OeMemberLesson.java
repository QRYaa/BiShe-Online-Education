package cn.tgxy.oledu.po;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class OeMemberLesson implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long memberId;
	private Long lessonId;
	//添加mc和ml的一对多关系
	private Long memberCourseId;
	private Integer progress;
	private Integer duration;
	private Integer watchedStatus;
	private Date lastViewedTime;
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
	public Long getLessonId() {
		return lessonId;
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
		return progress;
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
		return watchedStatus;
	}
	public void setWatchedStatus(Integer watchedStatus) {
		this.watchedStatus = watchedStatus;
	}
	public Date getLastViewedTime() {
		return lastViewedTime;
	}
	public void setLastViewedTime(Date lastViewedTime) {
		this.lastViewedTime = lastViewedTime;
	}
	
	
	
	

}


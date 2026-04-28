package cn.tgxy.oledu.po;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class OeMemberCourse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long memberId;
	private Long courseId;
	private Long lessonId;//上次观看的课节
	private Date lastViewTime;
	private Integer completionPercentage;
	
	private Integer noteNum;
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
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	public Long getLessonId() {
		return lessonId;
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
		return completionPercentage;
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

}

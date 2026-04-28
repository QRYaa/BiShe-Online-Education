package cn.tgxy.tgbase.dto;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class BsTaskLogDto {
	private Long id;

	private Long taskId;
	
	@JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private Date startTime;

	@JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private Date stopTime;
	
	private Integer executionDuration;
    
    private String message;
    
    private String exceptionInfo;
    
    private Integer result;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
		if (this.startTime != null && this.stopTime != null) {
	        long durationInMillis = this.stopTime.getTime() - this.startTime.getTime();
	        this.executionDuration = (int)durationInMillis;
	    }
	}

	public Integer getExecutionDuration() {
		return executionDuration;
	}

	public void setExecutionDuration(Integer executionDuration) {
		this.executionDuration = executionDuration;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExceptionInfo() {
		return exceptionInfo;
	}

	public void setExceptionInfo(String exceptionInfo) {
		this.exceptionInfo = exceptionInfo;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}
}

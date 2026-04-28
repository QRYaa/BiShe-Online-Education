package cn.tgxy.tgbase.dto;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class BsAppLogDto {
	private Long id;

	private String trackId;

	private Long appId;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	private Integer type;

	private String content;

	private String exceptionInfo;

	private Integer result;


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTrackId() {
		return this.trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}
	
	public Long getAppId() {
		return this.appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}
	
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	
	public String getExceptionInfo() {
		return this.exceptionInfo;
	}

	public void setExceptionInfo(String exceptionInfo) {
		this.exceptionInfo = exceptionInfo;
	}
	
	public Integer getResult() {
		return this.result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}
	
}

package cn.tgxy.oledu.dto;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class OeBannerDto {
	private Long id;

	private String name;

	private String image;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
	private Date startTime;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
	private Date endTime;

	private String url;

	private String remark;

	private Integer enable;
	
	private Integer status;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Integer getEnable() {
		return this.enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}

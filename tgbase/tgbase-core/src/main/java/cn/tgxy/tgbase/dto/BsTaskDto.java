package cn.tgxy.tgbase.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.tgxy.tgbase.task.BsCronExpressionUtils;

public class BsTaskDto {
	private Long id;
	
	private String name;
	
	private String handlerName;
	
	private String param;
	
	private Integer lastResult;
	
	@JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastTime;
	
	private String cron;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getNextValidTime()
    {
        if (cron != null)
        {
            return BsCronExpressionUtils.getNextExecution(cron);
        }
        return null;
    }
	
	private Integer enable;
	
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getLastResult() {
		return lastResult;
	}

	public void setLastResult(Integer lastResult) {
		this.lastResult = lastResult;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	
}

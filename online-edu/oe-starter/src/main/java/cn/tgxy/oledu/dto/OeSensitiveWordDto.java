package cn.tgxy.oledu.dto;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OeSensitiveWordDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String sensitiveWord;
	
	private String replaceWord;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSensitiveWord() {
		return sensitiveWord;
	}

	public void setSensitiveWord(String sensitiveWord) {
		this.sensitiveWord = sensitiveWord;
	}

	public String getReplaceWord() {
		return replaceWord;
	}

	public void setReplaceWord(String replaceWord) {
		this.replaceWord = replaceWord;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}

package cn.tgxy.oledu.dto;

import java.io.Serializable;
import java.util.Date;

import org.bouncycastle.util.Arrays;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OeMemberIdCardDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private Long memberId;

	private Integer type;

	private String number;

	private String realName;

	private Integer gender;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date birthday;

	private String address;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date issueDate;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date expireDate;

	private String frontImageUrl;

	private String backImageUrl;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
	private Date createTime;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
	private Date updateTime;

	public void maskSensitiveInfo() {
		int realNameLen = this.realName.length();
		char[] maskNameArray = new char[realNameLen];
		Arrays.fill(maskNameArray, '*');
		if (realNameLen >= 2) {
			maskNameArray[realNameLen - 1] = this.realName.charAt(realNameLen - 1);
			if (realNameLen >= 3) {
				maskNameArray[0] = this.realName.charAt(0);
			}
		}
		this.realName = new String(maskNameArray);

		int numberLen = this.number.length();
		char[] maskNumberArray = new char[numberLen];
		Arrays.fill(maskNumberArray, '*');
		maskNumberArray[numberLen - 1] = this.number.charAt(numberLen - 1);
		maskNumberArray[0] = this.number.charAt(0);
		this.number = new String(maskNumberArray);
		
		this.frontImageUrl="";
		this.backImageUrl="";
	}

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getGender() {
		return this.gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getIssueDate() {
		return this.issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpireDate() {
		return this.expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getFrontImageUrl() {
		return this.frontImageUrl;
	}

	public void setFrontImageUrl(String frontImageUrl) {
		this.frontImageUrl = frontImageUrl;
	}

	public String getBackImageUrl() {
		return this.backImageUrl;
	}

	public void setBackImageUrl(String backImageUrl) {
		this.backImageUrl = backImageUrl;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}

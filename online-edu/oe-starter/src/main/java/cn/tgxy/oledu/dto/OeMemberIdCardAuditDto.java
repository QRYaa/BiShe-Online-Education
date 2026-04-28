package cn.tgxy.oledu.dto;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OeMemberIdCardAuditDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private Long memberId;
	
	private String memberCode;
	
	private String memberName;
	
	private String memberAvatar;

	private Integer status;

	private String opinion;

	private Long auditorId;
	
	private String auditorName;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
	private Date createTime;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
	private Date auditTime;

	private Integer idCardType;
	
	private String idCardNumber;

	private String idCardRealName;

	private Integer idCardGender;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	private Date idCardBirthday;

	private String idCardAddress;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	private Date idCardIssueDate;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	private Date idCardExpireDate;

	private String idCardFrontImageUrl;

	private String idCardBackImageUrl;


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
	
	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
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

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getOpinion() {
		return this.opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	
	public Long getAuditorId() {
		return this.auditorId;
	}

	public void setAuditorId(Long auditorId) {
		this.auditorId = auditorId;
	}
	
	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Date getAuditTime() {
		return this.auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	
	public Integer getIdCardType() {
		return idCardType;
	}

	public void setIdCardType(Integer idCardType) {
		this.idCardType = idCardType;
	}

	public String getIdCardNumber() {
		return this.idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}
	
	public String getIdCardRealName() {
		return this.idCardRealName;
	}

	public void setIdCardRealName(String idCardRealName) {
		this.idCardRealName = idCardRealName;
	}
	
	public Integer getIdCardGender() {
		return this.idCardGender;
	}

	public void setIdCardGender(Integer idCardGender) {
		this.idCardGender = idCardGender;
	}
	
	public Date getIdCardBirthday() {
		return this.idCardBirthday;
	}

	public void setIdCardBirthday(Date idCardBirthday) {
		this.idCardBirthday = idCardBirthday;
	}
	
	public String getIdCardAddress() {
		return this.idCardAddress;
	}

	public void setIdCardAddress(String idCardAddress) {
		this.idCardAddress = idCardAddress;
	}
	
	public Date getIdCardIssueDate() {
		return this.idCardIssueDate;
	}

	public void setIdCardIssueDate(Date idCardIssueDate) {
		this.idCardIssueDate = idCardIssueDate;
	}
	
	public Date getIdCardExpireDate() {
		return this.idCardExpireDate;
	}

	public void setIdCardExpireDate(Date idCardExpireDate) {
		this.idCardExpireDate = idCardExpireDate;
	}
	
	public String getIdCardFrontImageUrl() {
		return this.idCardFrontImageUrl;
	}

	public void setIdCardFrontImageUrl(String idCardFrontImageUrl) {
		this.idCardFrontImageUrl = idCardFrontImageUrl;
	}
	
	public String getIdCardBackImageUrl() {
		return this.idCardBackImageUrl;
	}

	public void setIdCardBackImageUrl(String idCardBackImageUrl) {
		this.idCardBackImageUrl = idCardBackImageUrl;
	}
	
}

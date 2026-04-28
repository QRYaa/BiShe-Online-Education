package cn.tgxy.oledu.po;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(indexes = { @Index(name = "idx_member_id", columnList = "member_id"),
		@Index(name = "idx_status", columnList = "status") })
public class OeMemberIdCardAudit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long memberId;

	private Integer status;

	private String opinion;

	private Long auditorId;

	private Date createTime;

	private Date auditTime;
	
	private Integer idCardType;

	@Column(length = 18)
	private String idCardNumber;

	@Column(length = 50)
	private String idCardRealName;

	private Integer idCardGender;

	private Date idCardBirthday;

	@Column(length = 200)
	private String idCardAddress;

	private Date idCardIssueDate;

	private Date idCardExpireDate;

	@Column(length = 255)
	private String idCardFrontImageUrl;

	@Column(length = 255)
	private String idCardBackImageUrl;

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public Long getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Long auditorId) {
		this.auditorId = auditorId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getAuditTime() {
		return auditTime;
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
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public String getIdCardRealName() {
		return idCardRealName;
	}

	public void setIdCardRealName(String idCardRealName) {
		this.idCardRealName = idCardRealName;
	}

	public Integer getIdCardGender() {
		return idCardGender;
	}

	public void setIdCardGender(Integer idCardGender) {
		this.idCardGender = idCardGender;
	}

	public Date getIdCardBirthday() {
		return idCardBirthday;
	}

	public void setIdCardBirthday(Date idCardBirthday) {
		this.idCardBirthday = idCardBirthday;
	}

	public String getIdCardAddress() {
		return idCardAddress;
	}

	public void setIdCardAddress(String idCardAddress) {
		this.idCardAddress = idCardAddress;
	}

	public Date getIdCardIssueDate() {
		return idCardIssueDate;
	}

	public void setIdCardIssueDate(Date idCardIssueDate) {
		this.idCardIssueDate = idCardIssueDate;
	}

	public Date getIdCardExpireDate() {
		return idCardExpireDate;
	}

	public void setIdCardExpireDate(Date idCardExpireDate) {
		this.idCardExpireDate = idCardExpireDate;
	}

	public String getIdCardFrontImageUrl() {
		return idCardFrontImageUrl;
	}

	public void setIdCardFrontImageUrl(String idCardFrontImageUrl) {
		this.idCardFrontImageUrl = idCardFrontImageUrl;
	}

	public String getIdCardBackImageUrl() {
		return idCardBackImageUrl;
	}

	public void setIdCardBackImageUrl(String idCardBackImageUrl) {
		this.idCardBackImageUrl = idCardBackImageUrl;
	}

}

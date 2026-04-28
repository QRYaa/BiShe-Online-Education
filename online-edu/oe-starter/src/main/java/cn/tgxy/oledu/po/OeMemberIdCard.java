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
		@Index(name = "idx_number", columnList = "number") })
public class OeMemberIdCard implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long memberId;

	private Integer type;
	
	@Column(length = 18)
	private String number;

	@Column(length = 50)
	private String realName;

	private Integer gender;

	private Date birthday;

	@Column(length = 200)
	private String address;

	private Date issueDate;

	private Date expireDate;

	@Column(length = 255)
	private String frontImageUrl;

	@Column(length = 255)
	private String backImageUrl;

	@Column(updatable = false)
	private Date createTime;

	private Date updateTime;

	public void coverFromAudit(OeMemberIdCardAudit audit) {
		this.memberId=audit.getMemberId();
		this.type=audit.getIdCardType();
		this.number=audit.getIdCardNumber();
		this.realName=audit.getIdCardRealName();
		this.gender=audit.getIdCardGender();
		this.birthday=audit.getIdCardBirthday();
		this.address=audit.getIdCardAddress();
		this.issueDate=audit.getIdCardIssueDate();
		this.expireDate=audit.getIdCardExpireDate();
		this.frontImageUrl=audit.getIdCardFrontImageUrl();
		this.backImageUrl=audit.getIdCardBackImageUrl();
		this.updateTime=new Date();
	}
	
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getFrontImageUrl() {
		return frontImageUrl;
	}

	public void setFrontImageUrl(String frontImageUrl) {
		this.frontImageUrl = frontImageUrl;
	}

	public String getBackImageUrl() {
		return backImageUrl;
	}

	public void setBackImageUrl(String backImageUrl) {
		this.backImageUrl = backImageUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
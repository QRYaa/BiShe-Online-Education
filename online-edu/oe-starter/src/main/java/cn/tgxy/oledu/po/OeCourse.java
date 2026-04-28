package cn.tgxy.oledu.po;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.tgxy.tgbase.po.BsAuditingPo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class OeCourse extends BsAuditingPo implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long courseTypeId;
	private Long teacherId;
	private String code;
	private String name;
	private String verticalImage;
	private String horizontalImage;
	private String squareImage;
	private String description;
	@Lob
	@Column(columnDefinition = "LONGTEXT")
	private String content;//富文本
	private String remark;
	private Integer status;
	private Boolean paid;
	@Column(precision = 13, scale = 2)
	private BigDecimal originalPrice;
	@Column(precision = 13, scale = 2)
	private BigDecimal price;
	private Boolean pupular; 
	private Boolean published;
	private Integer learningCount;//可设置学习人数
	private Integer actualLearningCount;//实际学习人数
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCourseTypeId() {
		return courseTypeId;
	}
	public void setCourseTypeId(Long courseTypeId) {
		this.courseTypeId = courseTypeId;
	}
	public Long getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVerticalImage() {
		return verticalImage;
	}
	public void setVerticalImage(String verticalImage) {
		this.verticalImage = verticalImage;
	}
	public String getHorizontalImage() {
		return horizontalImage;
	}
	public void setHorizontalImage(String horizontalImage) {
		this.horizontalImage = horizontalImage;
	}
	public String getSquareImage() {
		return squareImage;
	}
	public void setSquareImage(String squareImage) {
		this.squareImage = squareImage;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Boolean getPaid() {
		return paid;
	}
	public void setPaid(Boolean paid) {
		this.paid = paid;
	}
	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Boolean getPupular() {
		return pupular;
	}
	public void setPupular(Boolean pupular) {
		this.pupular = pupular;
	}
	public Boolean getPublished() {
		return published;
	}
	public void setPublished(Boolean published) {
		this.published = published;
	}

	public Integer getLearningCount() {
		return learningCount;
	}
	public void setLearningCount(Integer learningCount) {
		this.learningCount = learningCount;
	}
	public Integer getActualLearningCount() {
		return actualLearningCount;
	}
	public void setActualLearningCount(Integer actualLearningCount) {
		this.actualLearningCount = actualLearningCount;
	}
	
}

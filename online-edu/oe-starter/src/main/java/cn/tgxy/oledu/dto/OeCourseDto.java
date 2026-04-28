package cn.tgxy.oledu.dto;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OeCourseDto {
	private Long id;

	private Long courseTypeId;
	
	private Long teacherId;
	
	private String teacherName;
	
	private String teacherCode;
		
	private String code;

	private String name;

	private String verticalImage;
	
	private String horizontalImage;
	
	private String squareImage;

	private String description;
	
	private String content;

	private String remark;

	private Integer status;

	private Boolean paid;

	private BigDecimal originalPrice;

	private BigDecimal price;

	private Boolean pupular;

	private Boolean published;
	
	private Integer learningCount;
	
	private Integer actualLearningCount;
	
	private Integer vlearningCount;//显示给前端的学习人数
	
	private List<OeChapterDto> chapterDtoList;
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getCourseTypeId() {
		return this.courseTypeId;
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

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeacherCode() {
		return teacherCode;
	}

	public void setTeacherCode(String teacherCode) {
		this.teacherCode = teacherCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
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
		return this.description;
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
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Boolean getPaid() {
		return this.paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}
	
	public BigDecimal getOriginalPrice() {
		return this.originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}
	
	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public Boolean getPupular() {
		return this.pupular;
	}

	public void setPupular(Boolean pupular) {
		this.pupular = pupular;
	}
	
	public Boolean getPublished() {
		return this.published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}

	public List<OeChapterDto> getChapterDtoList() {
		return chapterDtoList;
	}

	public void setChapterDtoList(List<OeChapterDto> chapterDtoList) {
		this.chapterDtoList = chapterDtoList;
	}

	public Integer getLearningCount() {
		return learningCount;
	}

	public void setLearningCount(Integer learningCount) {
		this.learningCount = learningCount;
	}

	@JsonIgnore
	public Integer getActualLearningCount() {
		return actualLearningCount;
	}

	public void setActualLearningCount(Integer actualLearningCount) {
		this.actualLearningCount = actualLearningCount;
	}

	public Integer getVlearningCount() {
		if(learningCount!=null&&actualLearningCount!=null) return learningCount+actualLearningCount;
		else if(learningCount!=null) return learningCount;
		else if(actualLearningCount!=null) return actualLearningCount;
		else return 0;
	}

	public void setVlearningCount(Integer vlearningCount) {
		this.vlearningCount = vlearningCount;
	}

}

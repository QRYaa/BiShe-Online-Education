package cn.tgxy.oledu.po;

import java.io.Serializable;
import java.util.Date;

import cn.tgxy.tgbase.po.BsAuditingPo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class OeNews extends BsAuditingPo implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String code;
	private String title;
	private String image;
	private Date publishedTime;
	private String digest;
	@Lob
	@Column(columnDefinition = "LONGTEXT")
	private String content;
	private String author;
	private Integer viewCount;
	private Boolean pinned; 
	private Boolean published; 

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Date getPublishedTime() {
		return publishedTime;
	}
	public void setPublishedTime(Date publishedTime) {
		this.publishedTime = publishedTime;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Integer getViewCount() {
		return viewCount;
	}
	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}
	public Boolean getPinned() {
		return pinned;
	}
	public void setPinned(Boolean pinned) {
		this.pinned = pinned;
	}
	public Boolean getPublished() {
		return published;
	}
	public void setPublished(Boolean published) {
		this.published = published;
	}
	
	
	

}

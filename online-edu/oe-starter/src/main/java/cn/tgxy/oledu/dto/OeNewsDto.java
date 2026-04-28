package cn.tgxy.oledu.dto;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class OeNewsDto {
	private Long id;

	private String code;

	private String title;

	private String image;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date publishedTime;

	private String digest;

	private String content;

	private String author;

	private Integer viewCount;

	private Boolean pinned;

	private Boolean published;


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public Date getPublishedTime() {
		return this.publishedTime;
	}

	public void setPublishedTime(Date publishedTime) {
		this.publishedTime = publishedTime;
	}
	
	public String getDigest() {
		return this.digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}
	
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getAuthor() {
		return this.author;
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
		return this.pinned;
	}

	public void setPinned(Boolean pinned) {
		this.pinned = pinned;
	}
	
	public Boolean getPublished() {
		return this.published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}
	
}

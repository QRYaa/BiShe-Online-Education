package cn.tgxy.oledu.dto;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OeReplyDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private Long noteId;

	private String trackId;

	private Long senderId;
	
	private String senderName;
	
	private String senderAvatar;

	private Long receiverId;
	
	private String receiverName;
	
	private String receiverAvatar;

	private String content;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	private Integer likeNum;

	private Boolean root;

	private Boolean enable;


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getNoteId() {
		return this.noteId;
	}

	public void setNoteId(Long noteId) {
		this.noteId = noteId;
	}
		
	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public Long getSenderId() {
		return this.senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	
	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderAvatar() {
		return senderAvatar;
	}

	public void setSenderAvatar(String senderAvatar) {
		this.senderAvatar = senderAvatar;
	}

	public Long getReceiverId() {
		return this.receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}
	
	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverAvatar() {
		return receiverAvatar;
	}

	public void setReceiverAvatar(String receiverAvatar) {
		this.receiverAvatar = receiverAvatar;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Integer getLikeNum() {
		return this.likeNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}
	
	public Boolean getRoot() {
		return this.root;
	}

	public void setRoot(Boolean root) {
		this.root = root;
	}
	
	public Boolean getEnable() {
		return this.enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	
}

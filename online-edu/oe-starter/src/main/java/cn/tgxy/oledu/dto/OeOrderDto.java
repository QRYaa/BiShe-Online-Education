package cn.tgxy.oledu.dto;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OeOrderDto {
	private Long id;

	private String code;

	private Long memberId;
	
	private String memberAvatar;
	
	private String memberName;

	private BigDecimal price;

	private BigDecimal discount;

	private BigDecimal finalPrice;

	private Integer paymentMethod;

	private String transactionId;

	private String remark;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date paymentTime;

	private Integer paymentStatus;

	private Integer afterSaleStatus;

	private List<OeOrderItemDto> orderItemDtoList;

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
	
	public Long getMemberId() {
		return this.memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMemberAvatar() {
		return memberAvatar;
	}

	public void setMemberAvatar(String memberAvatar) {
		this.memberAvatar = memberAvatar;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getDiscount() {
		return this.discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	
	public BigDecimal getFinalPrice() {
		return this.finalPrice;
	}

	public void setFinalPrice(BigDecimal finalPrice) {
		this.finalPrice = finalPrice;
	}
	
	public Integer getPaymentMethod() {
		return this.paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	public Date getPaymentTime() {
		return this.paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}
	
	public Integer getPaymentStatus() {
		return this.paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	
	public Integer getAfterSaleStatus() {
		return this.afterSaleStatus;
	}

	public void setAfterSaleStatus(Integer afterSaleStatus) {
		this.afterSaleStatus = afterSaleStatus;
	}

	public List<OeOrderItemDto> getOrderItemDtoList() {
		return orderItemDtoList;
	}

	public void setOrderItemDtoList(List<OeOrderItemDto> orderItemDtoList) {
		this.orderItemDtoList = orderItemDtoList;
	}
	
}

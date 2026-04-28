package cn.tgxy.oledu.po;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.tgxy.oledu.constant.OeConstant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class OeOrderItem implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long orderId;
	private Integer type;
	private Long referenceId;
	private Integer quantity;
	//购买时的商品信息
	@Column(precision = 13, scale = 2)
	private BigDecimal price;//价格
	@Column(precision = 13, scale = 2)
	private BigDecimal totalPrice;
	private String name;//商品名称
	private String image;//商品图片
	private String description; //商品描述
	
	public OeOrderItem() {
		
	}
	
	public OeOrderItem(OeCourse course) {
		this.type=OeConstant.ORDER_ITEM_OLCOURSE;
		this.referenceId=course.getId();
		this.quantity=OeConstant.ORDER_ITEM_QUANTITY;
		this.price=course.getPrice();
		this.totalPrice=course.getPrice();
		this.name=course.getName();
		this.image=course.getSquareImage();
		this.description=course.getDescription();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}


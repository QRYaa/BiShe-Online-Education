package cn.tgxy.oledu.dto;
import java.math.BigDecimal;

public class OeOrderItemDto {
	private Long id;

	private Long orderId;
	
	private Integer type;

	private Long referenceId;

	private Integer quantity;
	
	private BigDecimal price;
	
	private BigDecimal totalPrice;

	private String name;

	private String image;

	private String description;


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public Long getReferenceId() {
		return this.referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}
	
	public Integer getQuantity() {
		return this.quantity;
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
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}

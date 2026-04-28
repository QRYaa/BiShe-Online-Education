package cn.tgxy.oledu.dto.response;

import java.math.BigDecimal;

public class OeOrderPriceDto {
	private BigDecimal price;
	private BigDecimal discount;
	private BigDecimal finalPrice;
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public BigDecimal getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(BigDecimal finalPrice) {
		this.finalPrice = finalPrice;
	}
}

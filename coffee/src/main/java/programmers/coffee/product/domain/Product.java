package programmers.coffee.product.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import programmers.coffee.constant.Category;
import programmers.coffee.constant.ProductStatus;
import programmers.coffee.product.dto.NewProductDTO;
import programmers.coffee.product.dto.ProductDTO;

@Entity
@Builder
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Product {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	private String productName;

	private Category category;

	private Integer stock;

	private ProductStatus productStatus;

	private Long price;

	private String description;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public static Product from(NewProductDTO newProductDTO) {
		return Product.builder()
			.productName(newProductDTO.getProductName())
			.category(newProductDTO.getCategory())
			.price(newProductDTO.getPrice())
			.description(newProductDTO.getDescription())
			.productStatus(newProductDTO.getProductStatus())
			.build();
	}

	public void updateProduct(ProductDTO productDTO) {
		this.productName = productDTO.getProductName();
		this.category = productDTO.getCategory();
		this.price = productDTO.getPrice();
		this.description = productDTO.getDescription();
	}

	public void deleteProduct() {
		// this.category = "판매중지";
		// ProductStatus 변경
	}
}

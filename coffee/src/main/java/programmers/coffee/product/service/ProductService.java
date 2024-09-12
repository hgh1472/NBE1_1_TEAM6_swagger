package programmers.coffee.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import programmers.coffee.product.domain.Product;
import programmers.coffee.product.dto.NewProductDTO;
import programmers.coffee.product.dto.ProductDTO;
import programmers.coffee.product.repository.ProductRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public ProductDTO save(NewProductDTO productDTO) {
		Product product = Product.from(productDTO);
		Product save = productRepository.save(product);
		log.info("Save : {}", save);
		return ProductDTO.from(save);
	}

	@Transactional
	public ProductDTO update(ProductDTO productDTO, Long productId) {
		Product original = productRepository.findById(productId)
			.orElseThrow(() -> new NoSuchElementException("존재하는 상품이 없습니다."));

		log.info("Original : {}", original);
		log.info("Will be Update : {}", productDTO);

		original.updateProduct(productDTO);
		ProductDTO updated = ProductDTO.from(original);
		return updated;
	}

	public Page<ProductDTO> getProducts(int page) {
		// 한 페이지에 제품 10개로 지정
		// 생성일자순 정렬 옵션 적용
		Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").ascending());
		Page<Product> findAll = productRepository.findAll(pageable);
		return findAll.map(ProductDTO::from);
	}

	/**
	 * 제품 삭제 => 이 제품 주문 내역들은? 지우는 것은 비즈니스상 옳지 않음
	 * 카테고리를 변경
	 */
	@Transactional
	public void deleteProduct(Long productId) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new NoSuchElementException("존재하지 않는 제품입니다."));
		product.deleteProduct();
	}

    public List<ProductDTO> searchByProductName(String productName) {
		List<Product> products = productRepository.findByProductNameContaining(productName);
		return products.stream().map(ProductDTO::from).toList();
    }

	public List<ProductDTO> searchByPrice(Long minPrice, Long maxPrice) {
		List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
		return products.stream().map(ProductDTO::from).toList();
	}

	public List<ProductDTO> getNonSoldoutProducts() {
		List<Product> products = productRepository.findNonSoldOut();
		return products.stream().map(ProductDTO::from).toList();
	}
}

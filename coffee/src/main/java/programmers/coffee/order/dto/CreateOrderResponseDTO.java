package programmers.coffee.order.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import programmers.coffee.constant.OrderStatus;
import programmers.coffee.order.domain.Order;

@Data
@Builder
public class CreateOrderResponseDTO {
	private UUID orderId;
	private OrderStatus status;

	public static CreateOrderResponseDTO from(Order order) {
		return CreateOrderResponseDTO.builder()
			.orderId(order.getOrderId())
			.status(order.getOrderStatus())
			.build();
	}
}

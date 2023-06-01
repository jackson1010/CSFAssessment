package ibf2022.batch3.assessment.csf.orderbackend.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;
import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrderSimple;
import ibf2022.batch3.assessment.csf.orderbackend.services.OrderingService;

@Controller
@RequestMapping()
public class OrderController {

	@Autowired
	private OrderingService orderingService;

	// TODO: Task 3 - POST /api/order
	@PostMapping(path = "api/order", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> PostOrder(@RequestBody PizzaOrder order) throws JsonProcessingException {

		System.out.println(order);
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			orderingService.add(order);
			PizzaOrderSimple ps = new PizzaOrderSimple();
			ps.setOrderId(order.getOrderId());
			ps.setDate(order.getDate().getTime());
			ps.setName(order.getName());
			ps.setEmail(order.getEmail());
			ps.setTotal(order.getTotal());
			String jsonString = objectMapper.writeValueAsString(ps);
			System.out.println(jsonString);
			return ResponseEntity.status(HttpStatus.ACCEPTED).contentType(MediaType.APPLICATION_JSON).body(jsonString);
		} catch (IOException e) {
			String error = objectMapper.writeValueAsString(e.getMessage());
			System.out.println(error);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An I/O error occurred");
		}

	}

	// TODO: Task 6 - GET /api/orders/<email>

	// TODO: Task 7 - DELETE /api/order/<orderId>

}

package ibf2022.batch3.assessment.csf.orderbackend.controllers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ibf2022.batch3.assessment.csf.orderbackend.models.Order;
import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;
import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrderSimple;
import ibf2022.batch3.assessment.csf.orderbackend.services.OrderException;
import ibf2022.batch3.assessment.csf.orderbackend.services.OrderingService;

@Controller
@RequestMapping()
public class OrderController {

	@Autowired
	private OrderingService orderingService;

	// TODO: Task 3 - POST /api/order
	@PostMapping(path = "api/order", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> PostOrder(@RequestBody PizzaOrder order)
			throws JsonProcessingException, OrderException {

		ObjectMapper objectMapper = new ObjectMapper();
		PizzaOrder orderWithID = new PizzaOrder();
		try {
			orderWithID = orderingService.placeOrder(order);
			PizzaOrderSimple ps = new PizzaOrderSimple();
			ps.setOrderId(orderWithID.getOrderId());
			ps.setDate(orderWithID.getDate().getTime());
			ps.setName(orderWithID.getName());
			ps.setEmail(orderWithID.getEmail());
			ps.setTotal(orderWithID.getTotal());
			System.out.println(orderWithID);
			String jsonString = objectMapper.writeValueAsString(ps);
			System.out.println(jsonString);
			return ResponseEntity.status(HttpStatus.ACCEPTED).contentType(MediaType.APPLICATION_JSON).body(jsonString);
		} catch (IOException | OrderException e) {
			String error = objectMapper.writeValueAsString(e.getMessage());
			System.out.println(error);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

	// TODO: Task 6 - GET /api/orders/<email>

	@GetMapping(path = "/api/orders/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getOrders(@PathVariable String email) throws JsonProcessingException {

		List<PizzaOrder> pizzaList = new LinkedList<>();
		pizzaList = orderingService.getPendingOrdersByEmail(email);
		System.out.println("orderlist-----------------" + pizzaList);

		List<Order> orderList = new LinkedList<>();

		for (PizzaOrder po : pizzaList) {
			Order o = new Order();
			o.setOrderId(po.getOrderId());
			o.setDate(po.getDate());
			o.setTotal(po.getTotal());
			orderList.add(o);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		String jsoString = objectMapper.writeValueAsString(orderList);

		return ResponseEntity.status(HttpStatus.ACCEPTED).contentType(MediaType.APPLICATION_JSON).body(jsoString);
	}

	// TODO: Task 7 - DELETE /api/order/<orderId>

	@DeleteMapping(path = "api/order/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> delivered(@PathVariable String orderId) {
		try {
			orderingService.markOrderDelivered(orderId);
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
					.body(e.toString());
		}
	}

}

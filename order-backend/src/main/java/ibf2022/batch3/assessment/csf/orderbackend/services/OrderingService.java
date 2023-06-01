package ibf2022.batch3.assessment.csf.orderbackend.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;
import ibf2022.batch3.assessment.csf.orderbackend.respositories.OrdersRepository;
import ibf2022.batch3.assessment.csf.orderbackend.respositories.PendingOrdersRepository;
import org.springframework.http.MediaType;

@Service
public class OrderingService {

	@Autowired
	private OrdersRepository ordersRepo;

	@Autowired
	private PendingOrdersRepository pendingOrdersRepo;

	// TODO: Task 5
	// WARNING: DO NOT CHANGE THE METHOD'S SIGNATURE
	public PizzaOrder placeOrder(PizzaOrder order) throws OrderException {
		final String URL = "https://pizza-pricing-production.up.railway.app/order";

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("name", order.getName());
		map.add("email", order.getEmail());
		map.add("sauce", order.getSauce());
		map.add("size", order.getSize().toString());
		map.add("thickCrust", order.getThickCrust().toString());
		List<String> toppingsList = order.getToppings(); // Assuming order.getToppings() returns a List<String>
		String toppingsString = String.join(",", toppingsList);
		map.add("toppings", toppingsString);
		map.add("comments", order.getComments() != null ? order.getComments() : "");

		RequestEntity<MultiValueMap<String, String>> req = RequestEntity
				.post(URL)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(map);

		RestTemplate template = new RestTemplate();
		ResponseEntity<String> resp = template.exchange(req, String.class);

		System.out.println(resp.getBody());
		String response = resp.getBody();
		List<String> values = Arrays.asList(response.split(","));

		PizzaOrder OrderWithID = order;
		OrderWithID.setOrderId(values.get(0));
		OrderWithID.setDate(new Date(Long.parseLong(values.get(1))));
		OrderWithID.setTotal(Float.parseFloat(values.get(2)));

		ordersRepo.add(OrderWithID);
		pendingOrdersRepo.add(OrderWithID);

		return OrderWithID;

	}

	// For Task 6
	// WARNING: Do not change the method's signature or its implemenation
	public List<PizzaOrder> getPendingOrdersByEmail(String email) {
		return ordersRepo.getPendingOrdersByEmail(email);
	}

	// For Task 7
	// WARNING: Do not change the method's signature or its implemenation
	public boolean markOrderDelivered(String orderId) throws Exception {
		return (ordersRepo.markOrderDelivered(orderId) && pendingOrdersRepo.delete(orderId));
	}

}

package ibf2022.batch3.assessment.csf.orderbackend.respositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;

@Repository
public class OrdersRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	// TODO: Task 3
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	//   Native MongoDB query here for add()
	public void add(PizzaOrder order) {
		String crust;

		if(order.getThickCrust()==true){
			crust="thick crust";
		}else{
			crust="thin crust";
		}

		Document doc = new Document()
		.append("_id", order.getOrderId())
		.append("date", order.getDate())
		.append("total", order.getTotal())
		.append("name", order.getName())
		.append("email", order.getEmail())
		.append("sauce", order.getSauce())
		.append("size", order.getSize())
		.append("crust", crust)
		.append("comments", order.getComments())
		.append("toppings", order.getToppings());

		Document reuslt = mongoTemplate.insert(doc, "orders");
		System.out.println("inserted order");
	}
	
	// TODO: Task 6
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	//   Native MongoDB query here for getPendingOrdersByEmail()
	public List<PizzaOrder> getPendingOrdersByEmail(String email) {

		return null;
	}

	// TODO: Task 7
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	//   Native MongoDB query here for markOrderDelivered()
	public boolean markOrderDelivered(String orderId) {

		return false;
	}


}

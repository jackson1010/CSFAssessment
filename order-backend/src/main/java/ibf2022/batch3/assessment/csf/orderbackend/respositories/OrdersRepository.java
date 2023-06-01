package ibf2022.batch3.assessment.csf.orderbackend.respositories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;

@Repository
public class OrdersRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	// TODO: Task 3
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	// Native MongoDB query here for add()
	public void add(PizzaOrder order) {
		String crust;

		if (order.getThickCrust() == true) {
			crust = "thick crust";
		} else {
			crust = "thin crust";
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
	// Native MongoDB query here for getPendingOrdersByEmail()
	// db.orders.find({
	// email: 'zinwin.1994@gmail.com',
	// $or: [{ delivered: { $exists: false } },{ delivered: false }]},
	// {_id: 1,date: 1,total: 1}).sort({date: -1});

	public List<PizzaOrder> getPendingOrdersByEmail(String email) {

		List<PizzaOrder> result = new LinkedList<>();
		Query query = new Query();
		query.fields().include("_id");
		query.fields().include("date");
		query.fields().include("total");

		Criteria criteria = new Criteria();
		criteria.andOperator(
				Criteria.where("email").is(email),
				new Criteria().orOperator(
						Criteria.where("delivered").exists(false),
						Criteria.where("delivered").is(false)));

		query.addCriteria(criteria);
		query.with(Sort.by(Sort.Direction.DESC, "date"));

		result = mongoTemplate.find(query, Document.class, "orders")
				.stream().map(d -> createFromMongo(d)).toList();
		return result;
	}

	private PizzaOrder createFromMongo(Document d) {
		PizzaOrder po = new PizzaOrder();
		po.setOrderId(d.getString("_id"));
		po.setTotal(d.getDouble("total").floatValue());
		po.setDate(d.getDate("date"));
		return po;
	}

	// TODO: Task 7
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	// Native MongoDB query here for markOrderDelivered()
	public boolean markOrderDelivered(String orderId) throws Exception {
		System.out.println("delteing mongo");
		Query query = new Query();
		Criteria criteria = Criteria.where("_id").is(orderId);
		query.addCriteria(criteria);
		Update update = new Update().set("delivered", true);
		UpdateResult result = mongoTemplate.updateFirst(query, update, "orders");
		return result.getModifiedCount() > 0;
	}

}

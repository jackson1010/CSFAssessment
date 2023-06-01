package ibf2022.batch3.assessment.csf.orderbackend.services;

import org.springframework.stereotype.Service;

@Service
public class OrderException extends Exception {

	public OrderException() { }

	public OrderException(String msg) {
		super(msg);
	}
}

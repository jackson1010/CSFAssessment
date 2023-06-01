package ibf2022.batch3.assessment.csf.orderbackend.models;

import java.util.Date;

public class Order {
    private String orderId;
    private Float total;
    private Date date;
    
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public Float getTotal() {
        return total;
    }
    public void setTotal(Float total) {
        this.total = total;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    
    
}

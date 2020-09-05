package in.yarafertilisers.Models;

public class EnterProductDAO {
    String id="";
    String product_name = "";
    String quantity = "";
    String order_in="";
    String numbers = "";

    public EnterProductDAO() {

    }

    public EnterProductDAO(String id, String product_name, String quantity, String order_in, String numbers) {
        this.id = id;
        this.product_name = product_name;
        this.quantity = quantity;
        this.order_in = order_in;
        this.numbers = numbers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrder_in() {
        return order_in;
    }

    public void setOrder_in(String order_in) {
        this.order_in = order_in;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
}

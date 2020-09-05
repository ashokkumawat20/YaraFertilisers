package in.yarafertilisers.Models;

/**
 * Created by admin on 3/7/2017.
 */

public class ProductsDAO {
    String id = "";
    String product_name = "";
    String order_in="";

    public ProductsDAO() {

    }

    public ProductsDAO(String id, String product_name) {
        this.id = id;
        this.product_name = product_name;

    }

    public ProductsDAO(String id, String product_name, String order_in) {
        this.id = id;
        this.product_name = product_name;
        this.order_in = order_in;
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

    public String getOrder_in() {
        return order_in;
    }

    public void setOrder_in(String order_in) {
        this.order_in = order_in;
    }

    @Override
    public String toString() {
        return product_name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductsDAO) {
            ProductsDAO c = (ProductsDAO) obj;
            if (c.getProduct_name().equals(product_name) && c.getId() == id) return true;
        }

        return false;
    }
}

package in.yarafertilisers.Models;

public class InvoicedProductDAO {
    String id = "";
    String sales_order_id = "";
    String invoice_no = "";
    String invoice_date = "";
    String warehouse = "";
    String user_name = "";
    String numbers = "";

    public InvoicedProductDAO() {

    }

    public InvoicedProductDAO(String id, String sales_order_id, String invoice_no, String invoice_date, String warehouse, String user_name, String numbers) {
        this.id = id;
        this.sales_order_id = sales_order_id;
        this.invoice_no = invoice_no;
        this.invoice_date = invoice_date;
        this.warehouse = warehouse;
        this.user_name = user_name;
        this.numbers = numbers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSales_order_id() {
        return sales_order_id;
    }

    public void setSales_order_id(String sales_order_id) {
        this.sales_order_id = sales_order_id;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
}

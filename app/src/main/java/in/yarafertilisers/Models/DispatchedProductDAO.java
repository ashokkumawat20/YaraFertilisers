package in.yarafertilisers.Models;

public class DispatchedProductDAO {
    String id = "";
    String sales_order_id = "";
    String invoice_no = "";
    String invoice_dt = "";
    String warehouse = "";
    String invoice_user_name = "";
    String dispatch = "";
    String driver_contact_no = "";
    String dispatch_user = "";
    String numbers = "";

    public DispatchedProductDAO() {

    }

    public DispatchedProductDAO(String id, String sales_order_id, String invoice_no, String invoice_dt, String warehouse, String invoice_user_name, String dispatch, String driver_contact_no, String dispatch_user, String numbers) {
        this.id = id;
        this.sales_order_id = sales_order_id;
        this.invoice_no = invoice_no;
        this.invoice_dt = invoice_dt;
        this.warehouse = warehouse;
        this.invoice_user_name = invoice_user_name;
        this.dispatch = dispatch;
        this.driver_contact_no = driver_contact_no;
        this.dispatch_user = dispatch_user;
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

    public String getInvoice_dt() {
        return invoice_dt;
    }

    public void setInvoice_dt(String invoice_dt) {
        this.invoice_dt = invoice_dt;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getInvoice_user_name() {
        return invoice_user_name;
    }

    public void setInvoice_user_name(String invoice_user_name) {
        this.invoice_user_name = invoice_user_name;
    }

    public String getDispatch() {
        return dispatch;
    }

    public void setDispatch(String dispatch) {
        this.dispatch = dispatch;
    }

    public String getDriver_contact_no() {
        return driver_contact_no;
    }

    public void setDriver_contact_no(String driver_contact_no) {
        this.driver_contact_no = driver_contact_no;
    }

    public String getDispatch_user() {
        return dispatch_user;
    }

    public void setDispatch_user(String dispatch_user) {
        this.dispatch_user = dispatch_user;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
}

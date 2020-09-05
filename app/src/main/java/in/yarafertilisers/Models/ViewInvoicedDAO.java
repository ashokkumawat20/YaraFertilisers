package in.yarafertilisers.Models;

public class ViewInvoicedDAO {
    String id = "";
    String order_no="";
    String order_date = "";
    String dealer_name = "";
    String sales_rep_comment = "";
    String invoice_dept_comment = "";
    String dispatch_dept_comment = "";
    String numbers = "";

    public ViewInvoicedDAO() {

    }

    public ViewInvoicedDAO(String id, String order_no, String order_date, String dealer_name, String sales_rep_comment, String invoice_dept_comment, String dispatch_dept_comment, String numbers) {
        this.id = id;
        this.order_no = order_no;
        this.order_date = order_date;
        this.dealer_name = dealer_name;
        this.sales_rep_comment = sales_rep_comment;
        this.invoice_dept_comment = invoice_dept_comment;
        this.dispatch_dept_comment = dispatch_dept_comment;
        this.numbers = numbers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public String getSales_rep_comment() {
        return sales_rep_comment;
    }

    public void setSales_rep_comment(String sales_rep_comment) {
        this.sales_rep_comment = sales_rep_comment;
    }

    public String getInvoice_dept_comment() {
        return invoice_dept_comment;
    }

    public void setInvoice_dept_comment(String invoice_dept_comment) {
        this.invoice_dept_comment = invoice_dept_comment;
    }

    public String getDispatch_dept_comment() {
        return dispatch_dept_comment;
    }

    public void setDispatch_dept_comment(String dispatch_dept_comment) {
        this.dispatch_dept_comment = dispatch_dept_comment;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
}

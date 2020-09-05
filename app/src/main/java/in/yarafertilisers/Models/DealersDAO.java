package in.yarafertilisers.Models;

public class DealersDAO {

    String id = "";
    String dealer_details = "";
    String bill_to = "";
    String ship_to = "";

    public DealersDAO() {

    }

    public DealersDAO(String id, String dealer_details, String bill_to, String ship_to) {
        this.id = id;
        this.dealer_details = dealer_details;
        this.bill_to = bill_to;
        this.ship_to = ship_to;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDealer_details() {
        return dealer_details;
    }

    public void setDealer_details(String dealer_details) {
        this.dealer_details = dealer_details;
    }

    public String getBill_to() {
        return bill_to;
    }

    public void setBill_to(String bill_to) {
        this.bill_to = bill_to;
    }

    public String getShip_to() {
        return ship_to;
    }

    public void setShip_to(String ship_to) {
        this.ship_to = ship_to;
    }

    @Override
    public String toString() {
        return dealer_details;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DealersDAO) {
            DealersDAO c = (DealersDAO) obj;
            if (c.getDealer_details().equals(dealer_details))
                return true;
        }

        return false;
    }
}

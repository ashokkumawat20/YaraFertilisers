package in.yarafertilisers.Utils;

import java.util.ArrayList;

/**
 * Created by admin on 12/9/2016.
 */

public class Config {
    // public static final String BASE_URL = "http://192.168.1.10:80/afcks/api/";

    public static final String BASE_URL = "http://yarafertilisers.in/afcks/api/";
    public static final String AVAILABLEAUTHORIZESALESREPANDRETURNID = BASE_URL + "user/availableAuthorizeSalesRepAndReturnID";
    public static final String GETAVAILABLEMOBILEDEVICEID = BASE_URL + "user/getAvailableMobileDeviceID";
    public static final String DISPLAYDEALERFORSALESORDERBYPREFIX = BASE_URL + "user/DisplayDealerForSalesOrderByPreFix";
    public static final String ADDSALESORDERUSINGSTOREPROC = BASE_URL + "user/addSalesOrderUsingStoreProc";
    public static final String DISPLAYPRODUCTSNOTINSALESORDER = BASE_URL + "user/DisplayProductsNotInSalesOrder";
    public static final String CHECKQTYINLIMITS = BASE_URL + "user/CheckQtyInLimits";
    public static final String ADDSALESORDERLINEITEMSUSINGSTOREPROC = BASE_URL + "user/AddSalesOrderLineItemsUsingStoreProc";
    public static final String SAVEORDERUSINGSTOREPROC = BASE_URL + "user/SaveOrderUsingStoreProc";
    public static final String SHOWPENDINGSALESORDERLINEITEM = BASE_URL + "user/ShowPendingSalesOrderLineItem";
    public static final String DELETEENTERPRODUCT = BASE_URL + "user/DeleteEnterProduct";
    public static final String SHOWPENDINGSALESORDER = BASE_URL + "user/ShowPendingSalesOrder";
    public static final String SHOWUNDERINVOICESTATUS = BASE_URL + "user/ShowUnderInvoiceStatus";
    public static final String SHOWUNDERINVOICESTATUSINVOICES = BASE_URL + "user/ShowUnderInvoiceStatusInvoices";
    public static final String SHOWDISPATCHEDINVOICESALESORDER = BASE_URL + "user/ShowDispatchedInvoiceSalesOrder";
    public static final String SHOWDISPATCHEDINVOICES = BASE_URL + "user/ShowDispatchedInvoices";
    public static final String UPDATEDEALERADDRESSUSINGSTOREPROC = BASE_URL + "user/UpdateDealerAddressUsingStoreProc";
    public static final String GETPENDINGSALESORDERCOUNTID = BASE_URL + "user/getPendingSalesOrderCountID";
    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "AFCKS Images";
    public static String DATA_ENTERLEVEL_COURSES = "";
    public static String DATA_SPLIZATION_COURSES = "";

    public static String DATA_MOVE_FROM_LOCATION = "";
    public static ArrayList<String> VALUE = new ArrayList<String>();
    // public static final String SMS_ORIGIN = "WAVARM";
    public static final String SMS_ORIGIN = "AFCKST";
}

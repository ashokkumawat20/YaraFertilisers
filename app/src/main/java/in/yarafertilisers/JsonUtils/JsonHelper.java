package in.yarafertilisers.JsonUtils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.yarafertilisers.Models.DispatchedProductDAO;
import in.yarafertilisers.Models.EnterProductDAO;
import in.yarafertilisers.Models.InvoicedProductDAO;
import in.yarafertilisers.Models.ViewInvoicedDAO;
import in.yarafertilisers.Models.ViewPendingDAO;


/**
 * Created by admin on 2/18/2017.
 */

public class JsonHelper {

    private ArrayList<EnterProductDAO> enterProductDAOArrayList = new ArrayList<EnterProductDAO>();
    private EnterProductDAO enterProductDAO;
    private ArrayList<ViewPendingDAO> viewPendingDAOArrayList = new ArrayList<ViewPendingDAO>();
    private ViewPendingDAO viewPendingDAO;
    private ArrayList<ViewInvoicedDAO> viewInvoicedDAOArrayList = new ArrayList<ViewInvoicedDAO>();
    private ViewInvoicedDAO viewInvoicedDAO;

    private ArrayList<InvoicedProductDAO> invoicedProductDAOArrayList = new ArrayList<InvoicedProductDAO>();
    private InvoicedProductDAO invoicedProductDAO;

    private ArrayList<DispatchedProductDAO> dispatchedProductDAOArrayList = new ArrayList<DispatchedProductDAO>();
    private DispatchedProductDAO dispatchedProductDAO;

    public ArrayList<EnterProductDAO> parseEnterProductList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                enterProductDAO = new EnterProductDAO();
                String sequence = String.format("%02d", i + 1);
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                enterProductDAO.setId(json_data.getString("id"));
                enterProductDAO.setProduct_name(json_data.getString("product_name"));
                enterProductDAO.setQuantity(json_data.getString("qty"));
                enterProductDAO.setOrder_in(json_data.getString("order_in"));
                enterProductDAO.setNumbers("" + sequence);
                enterProductDAOArrayList.add(enterProductDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return enterProductDAOArrayList;
    }

    public ArrayList<ViewPendingDAO> parseViewPendingOrderList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                viewPendingDAO = new ViewPendingDAO();
                String sequence = String.format("%02d", i + 1);
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                viewPendingDAO.setId(json_data.getString("id"));
                viewPendingDAO.setOrder_date(json_data.getString("order_date"));
                viewPendingDAO.setDealer_name(json_data.getString("dealer_name"));
                viewPendingDAO.setSales_rep_comment(json_data.getString("sales_rep_comment"));
                viewPendingDAO.setInvoice_dept_comment(json_data.getString("invoice_dept_comment"));
                viewPendingDAO.setDispatch_dept_comment(json_data.getString("dispatch_dept_comment"));
                viewPendingDAO.setLine_item(json_data.getString("line_item"));
                viewPendingDAO.setNumbers("" + sequence);
                viewPendingDAOArrayList.add(viewPendingDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return viewPendingDAOArrayList;
    }

    public ArrayList<ViewInvoicedDAO> parseViewInvoicedList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                viewInvoicedDAO = new ViewInvoicedDAO();
                String sequence = String.format("%02d", i + 1);
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                viewInvoicedDAO.setId(json_data.getString("id"));
                viewInvoicedDAO.setOrder_date(json_data.getString("order_date"));
                viewInvoicedDAO.setDealer_name(json_data.getString("dealer"));
                viewInvoicedDAO.setOrder_no(json_data.getString("order_no"));
                viewInvoicedDAO.setSales_rep_comment(json_data.getString("sales_rep_comment"));
                viewInvoicedDAO.setInvoice_dept_comment(json_data.getString("invoice_dept_comment"));
                viewInvoicedDAO.setDispatch_dept_comment(json_data.getString("dispatch_dept_comment"));
                viewInvoicedDAO.setNumbers("" + sequence);
                viewInvoicedDAOArrayList.add(viewInvoicedDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return viewInvoicedDAOArrayList;
    }


    public ArrayList<InvoicedProductDAO> parseInvoicedList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                invoicedProductDAO = new InvoicedProductDAO();
                String sequence = String.format("%02d", i + 1);
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                invoicedProductDAO.setId(json_data.getString("id"));
                invoicedProductDAO.setSales_order_id(json_data.getString("sales_order_id"));
                invoicedProductDAO.setInvoice_no(json_data.getString("invoice_no"));
                invoicedProductDAO.setInvoice_date(json_data.getString("invoice_date"));
                invoicedProductDAO.setWarehouse(json_data.getString("warehouse"));
                invoicedProductDAO.setUser_name(json_data.getString("user_name"));
                invoicedProductDAO.setNumbers("" + sequence);
                invoicedProductDAOArrayList.add(invoicedProductDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return invoicedProductDAOArrayList;
    }

    public ArrayList<DispatchedProductDAO> parseDispatchedList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                dispatchedProductDAO = new DispatchedProductDAO();
                String sequence = String.format("%02d", i + 1);
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                dispatchedProductDAO.setId(json_data.getString("id"));
                dispatchedProductDAO.setSales_order_id(json_data.getString("sales_order_id"));
                dispatchedProductDAO.setInvoice_no(json_data.getString("invoice_no"));
                dispatchedProductDAO.setInvoice_dt(json_data.getString("invoice_dt"));
                dispatchedProductDAO.setWarehouse(json_data.getString("warehouse"));
                dispatchedProductDAO.setDispatch_user(json_data.getString("dispatch_user"));
                dispatchedProductDAO.setInvoice_user_name(json_data.getString("invoice_user_name"));
                dispatchedProductDAO.setDispatch(json_data.getString("dispatch"));
                dispatchedProductDAO.setDriver_contact_no(json_data.getString("driver_contact_no"));
                dispatchedProductDAO.setNumbers("" + sequence);
                dispatchedProductDAOArrayList.add(dispatchedProductDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dispatchedProductDAOArrayList;
    }
}

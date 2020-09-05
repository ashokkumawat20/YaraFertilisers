package in.yarafertilisers.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.yarafertilisers.Models.EnterProductDAO;
import in.yarafertilisers.Models.ViewPendingDAO;
import in.yarafertilisers.R;
import in.yarafertilisers.Utils.Config;
import in.yarafertilisers.Utils.FeesListener;
import in.yarafertilisers.Utils.WebClient;
import in.yarafertilisers.View.ProductDetailsView;


/**
 * Created by admin on 3/18/2017.
 */

public class ViewPendingOrderListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    public LayoutInflater inflater;
    List<ViewPendingDAO> data;
    ViewPendingDAO current;
    int currentPos = 0;
    String id;
    int ID;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj, jsonLeadObjSms;
    JSONArray jsonArray;
    String centerListResponse = "", templateSMSResponse = "";
    boolean status, status1;
    String message = "", message1 = "";
    String msg = "";
    String txt_msg = "";

    int temp_sms_status = 0;
    static FeesListener mListener;

    // create constructor to innitilize context and data sent from MainActivity
    public ViewPendingOrderListAdpter(Context context, List<ViewPendingDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_view_pending_so_details, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int pos = position;
        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        current = data.get(position);
        myHolder.sr.setText(current.getNumbers());
        myHolder.sr.setTag(position);
        myHolder.orderDate.setText(current.getOrder_date());
        myHolder.orderDate.setTag(position);
        myHolder.qty.setText(current.getLine_item());
        myHolder.qty.setTag(position);
        myHolder.dealerName.setText(current.getDealer_name());
        myHolder.dealerName.setTag(position);
        myHolder.salesrepcomment.setText("Sales Comment : "+current.getSales_rep_comment());
        myHolder.salesrepcomment.setTag(position);
        if (!current.getInvoice_dept_comment().equals("")) {
            myHolder.invrepcomment.setVisibility(View.VISIBLE);
            myHolder.invrepcomment.setText("Invoice Comment : "+current.getInvoice_dept_comment());
            myHolder.invrepcomment.setTag(position);
        } else {
            myHolder.invrepcomment.setVisibility(View.GONE);
        }
        if (!current.getDispatch_dept_comment().equals("")) {
            myHolder.dispatrepcomment.setVisibility(View.VISIBLE);
            myHolder.dispatrepcomment.setText("Dispatch Comment : "+current.getDispatch_dept_comment());
            myHolder.dispatrepcomment.setTag(position);
        } else {
            myHolder.dispatrepcomment.setVisibility(View.GONE);
        }
        myHolder.lead_Layout.setTag(position);
        myHolder.lead_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (Integer) v.getTag();
                current = data.get(id);
                //  Toast.makeText(context, current.getId(), Toast.LENGTH_SHORT).show();
                prefEditor.putString("sales_order_id", current.getId());
                prefEditor.commit();
                ProductDetailsView productDetailsView = new ProductDetailsView();
                productDetailsView.show(((FragmentActivity) context).getSupportFragmentManager(), "productDetailsView");
            }
        });

    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView sr, orderDate, qty, dealerName, salesrepcomment, invrepcomment, dispatrepcomment;
        LinearLayout lead_Layout;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            sr = (TextView) itemView.findViewById(R.id.sr);
            orderDate = (TextView) itemView.findViewById(R.id.orderDate);
            qty = (TextView) itemView.findViewById(R.id.qty);
            dealerName = (TextView) itemView.findViewById(R.id.dealerName);
            salesrepcomment = (TextView) itemView.findViewById(R.id.salesrepcomment);
            invrepcomment = (TextView) itemView.findViewById(R.id.invrepcomment);
            dispatrepcomment = (TextView) itemView.findViewById(R.id.dispatrepcomment);
            lead_Layout = (LinearLayout) itemView.findViewById(R.id.lead_Layout);

        }

    }

    // method to access in activity after updating selection
    public List<ViewPendingDAO> getSservicelist() {
        return data;
    }


    //
    protected boolean isJSONValid(String callReoprtResponse2) {
        // TODO Auto-generated method stub
        try {
            new JSONObject(callReoprtResponse2);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(callReoprtResponse2);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }

    public static void bindListener(FeesListener listener) {
        mListener = listener;
    }
}

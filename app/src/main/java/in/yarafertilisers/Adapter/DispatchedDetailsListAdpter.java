package in.yarafertilisers.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.yarafertilisers.Models.DispatchedProductDAO;
import in.yarafertilisers.Models.InvoicedProductDAO;
import in.yarafertilisers.R;
import in.yarafertilisers.Utils.Config;
import in.yarafertilisers.Utils.FeesListener;
import in.yarafertilisers.Utils.WebClient;


/**
 * Created by admin on 3/18/2017.
 */

public class DispatchedDetailsListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    public LayoutInflater inflater;
    List<DispatchedProductDAO> data;
    DispatchedProductDAO current;
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
    public DispatchedDetailsListAdpter(Context context, List<DispatchedProductDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_view_dispatched_details, parent, false);
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
        myHolder.inVoiceDate.setText("Invoice Date : "+current.getInvoice_dt());
        myHolder.inVoiceDate.setTag(position);
        myHolder.invoiceNo.setText(current.getInvoice_no());
        myHolder.invoiceNo.setTag(position);
        myHolder.wharehouse.setText("Wharehouse : "+current.getWarehouse());
        myHolder.wharehouse.setTag(position);
        myHolder.userName.setText(current.getInvoice_user_name());
        myHolder.userName.setTag(position);
        myHolder.driverNo.setText("Driver Mobile : "+current.getDriver_contact_no());
        myHolder.driverNo.setTag(position);
        myHolder.dispatchUser.setText(current.getDispatch_user());
        myHolder.dispatchUser.setTag(position);
        myHolder.dispatchDate.setText("Dispatch Date : "+current.getDispatch());
        myHolder.dispatchDate.setTag(position);

    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView sr, inVoiceDate, invoiceNo, wharehouse, userName, driverNo, dispatchUser, dispatchDate;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            sr = (TextView) itemView.findViewById(R.id.sr);
            inVoiceDate = (TextView) itemView.findViewById(R.id.inVoiceDate);
            invoiceNo = (TextView) itemView.findViewById(R.id.invoiceNo);
            wharehouse = (TextView) itemView.findViewById(R.id.wharehouse);
            userName = (TextView) itemView.findViewById(R.id.userName);
            driverNo = (TextView) itemView.findViewById(R.id.driverNo);
            dispatchUser = (TextView) itemView.findViewById(R.id.dispatchUser);
            dispatchDate = (TextView) itemView.findViewById(R.id.dispatchDate);
        }

    }

    // method to access in activity after updating selection
    public List<DispatchedProductDAO> getSservicelist() {
        return data;
    }


    //
    private class deleteProduct extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(context);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {

                        put("id", id);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            centerListResponse = serviceAccess.SendHttpPost(Config.DELETEENTERPRODUCT, jsonLeadObj);
            Log.i("resp", "leadListResponse" + centerListResponse);


            if (centerListResponse.compareTo("") != 0) {
                if (isJSONValid(centerListResponse)) {

                    try {

                        JSONObject jObject = new JSONObject(centerListResponse);
                        status = jObject.getBoolean("status");
                        message = jObject.getString("message");
                        jsonArray = new JSONArray(centerListResponse);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {

                    Toast.makeText(context, "Please check your network connection", Toast.LENGTH_LONG).show();

                }
            } else {

                Toast.makeText(context, "Please check your network connection.", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (status) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                mListener.messageReceived(message);
            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            }
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
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

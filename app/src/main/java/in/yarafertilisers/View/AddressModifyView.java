package in.yarafertilisers.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.yarafertilisers.Activity.AddressModifyActivity;
import in.yarafertilisers.Adapter.ViewProductListAdpter;
import in.yarafertilisers.JsonUtils.JsonHelper;
import in.yarafertilisers.Models.DealersDAO;
import in.yarafertilisers.Models.EnterProductDAO;
import in.yarafertilisers.R;
import in.yarafertilisers.Utils.AppStatus;
import in.yarafertilisers.Utils.Config;
import in.yarafertilisers.Utils.Constant;
import in.yarafertilisers.Utils.FeesListener;
import in.yarafertilisers.Utils.WebClient;


public class AddressModifyView extends DialogFragment {


    Context context;
    View registerView;
    int count;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    AutoCompleteTextView SearchDealer;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    EditText billToEdtTxt, shipToEdtTxt;
    Button updatedealerAddress;
    JSONObject jsonSchedule, jsonLeadObjSaveOrder;
    String dealerResponse = "";
    public List<DealersDAO> dealersDAOList;
    DealersDAO dealersDAO;
    public ArrayAdapter<DealersDAO> bAdapter;
    String dealer_id = "", billto = "", shipto = "", saveOrderResponse = "";
    ImageView clear_dealer;
    ProgressDialog mProgressDialog;
    boolean status1;
    String message1 = "";
    static FeesListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        registerView = inflater.inflate(R.layout.dialog_address_modify, null);

        context = getActivity();
        Window window = getDialog().getWindow();

        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.CENTER | Gravity.CENTER);

        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();

        params.y = 50;

        window.setAttributes(params);
        preferences = getActivity().getSharedPreferences("Prefrence", getActivity().MODE_PRIVATE);
        prefEditor = preferences.edit();

        clear_dealer = (ImageView) registerView.findViewById(R.id.clear_dealer);
        dealersDAOList = new ArrayList<DealersDAO>();
        billToEdtTxt = (EditText) registerView.findViewById(R.id.billToEdtTxt);
        shipToEdtTxt = (EditText) registerView.findViewById(R.id.shipToEdtTxt);
        updatedealerAddress = (Button) registerView.findViewById(R.id.updatedealerAddress);
        billToEdtTxt.setText(preferences.getString("Bill_to", ""));
        shipToEdtTxt.setText(preferences.getString("Ship_to", ""));
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);

        updatedealerAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    billto = billToEdtTxt.getText().toString().trim();
                    shipto = shipToEdtTxt.getText().toString().trim();
                    if (validate(billto, shipto)) {
                        new updateData().execute();
                    }

                } else {

                    Toast.makeText(getActivity(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });

        getDialog().setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //Hide your keyboard here!!!
                    //Toast.makeText(getActivity(), "PLease enter your information to get us connected with you.", Toast.LENGTH_LONG).show();
                    return true; // pretend we've processed it
                } else
                    return false; // pass on to be processed as normal
            }
        });
        return registerView;
    }


    public boolean validate(String billto, String shipto) {
        boolean isValidate = false;
        if (billto.equals("")) {
            Toast.makeText(getActivity(), "Please enter Bill to address.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else if (shipto.equals("")) {
            Toast.makeText(getActivity(), "Please enter Ship to address.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else {
            isValidate = true;
        }
        return isValidate;
    }


    private class updateData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
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

            jsonLeadObjSaveOrder = new JSONObject() {
                {
                    try {
                        put("pID", preferences.getString("dealer_id", ""));
                        put("pBillTo", billto);
                        put("pShipTo", shipto);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };


            WebClient serviceAccess = new WebClient();
            saveOrderResponse = serviceAccess.SendHttpPost(Config.UPDATEDEALERADDRESSUSINGSTOREPROC, jsonLeadObjSaveOrder);
            Log.i("resp", "saveOrderResponse" + saveOrderResponse);
            if (isJSONValid(saveOrderResponse)) {


                try {

                    JSONObject jsonObject = new JSONObject(saveOrderResponse);
                    status1 = jsonObject.getBoolean("status");
                    message1 = jsonObject.getString("message");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } else {

                Toast.makeText(getActivity(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void args) {

            if (status1) {

                Toast.makeText(getActivity(), message1, Toast.LENGTH_LONG).show();

                // Close the progressdialog

                mProgressDialog.dismiss();
                dismiss();
                mListener.messageReceived(message1);
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();
                dismiss();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        //Toast.makeText(getActivity(), "Your information is valuable for us and won't be misused.", Toast.LENGTH_SHORT).show();
                        count++;
                        if (count >= 1) {
                            // update();
                            dismiss();
                        }
                        return true;
                    } else {
                        //Hide your keyboard here!!!!!!
                        return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
            }
        });
    }

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

    public static void bindListener(FeesListener listener) {
        mListener = listener;
    }
}
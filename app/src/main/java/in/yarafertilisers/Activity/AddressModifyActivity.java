package in.yarafertilisers.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.yarafertilisers.Models.DealersDAO;
import in.yarafertilisers.R;
import in.yarafertilisers.Utils.AppStatus;
import in.yarafertilisers.Utils.Config;
import in.yarafertilisers.Utils.Constant;
import in.yarafertilisers.Utils.WebClient;

public class AddressModifyActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_modify);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        SearchDealer = (AutoCompleteTextView) findViewById(R.id.SearchDealer);
        clear_dealer = (ImageView) findViewById(R.id.clear_dealer);
        dealersDAOList = new ArrayList<DealersDAO>();
        billToEdtTxt = (EditText) findViewById(R.id.billToEdtTxt);
        shipToEdtTxt = (EditText) findViewById(R.id.shipToEdtTxt);
        updatedealerAddress = (Button) findViewById(R.id.updatedealerAddress);
        SearchDealer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(SearchDealer.getText())) {
                        clear_dealer.setVisibility(View.VISIBLE);

                        getDealerSelect(SearchDealer.getText().toString());

                    }
                }
                return false;
            }
        });
        clear_dealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDealer.setText("");
                clear_dealer.setVisibility(View.GONE);
                dealer_id = "";
                billToEdtTxt.setText("");
                shipToEdtTxt.setText("");
                billToEdtTxt.setVisibility(View.GONE);
                shipToEdtTxt.setVisibility(View.GONE);
                updatedealerAddress.setVisibility(View.GONE);
                if (dealerResponse.compareTo("") != 0) {
                    dealersDAOList.clear(); // this list which you hava passed in Adapter for your listview
                    bAdapter.notifyDataSetChanged(); // notify to listview for refresh
                }


            }
        });

        updatedealerAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    billto = billToEdtTxt.getText().toString().trim();
                    shipto = shipToEdtTxt.getText().toString().trim();
                    if (validate(dealer_id, billto, shipto)) {
                        new updateData().execute();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean validate(String dealer, String billto, String shipto) {
        boolean isValidate = false;
        if (dealer.trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Please select dealer.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (billto.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter Bill to address.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else if (shipto.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter Ship to address.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else {
            isValidate = true;
        }
        return isValidate;
    }

    public void getDealerSelect(final String channelPartnerSelect) {

        jsonSchedule = new JSONObject() {
            {
                try {
                    put("Prefixtext", channelPartnerSelect);

                    //put("user_id", "RS");

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("json exception", "json exception" + e);
                }
            }
        };


        Thread objectThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();

                Log.i("json", "json" + jsonSchedule);

                dealerResponse = serviceAccess.SendHttpPost(Config.DISPLAYDEALERFORSALESORDERBYPREFIX, jsonSchedule);
                Log.i("resp", "dealerResponse" + dealerResponse);


                try {
                    JSONArray callArrayList = new JSONArray(dealerResponse);
                    dealersDAOList.clear();
                    // user_id="";
                    for (int i = 0; i < callArrayList.length(); i++) {
                        dealersDAO = new DealersDAO();
                        JSONObject json_data = callArrayList.getJSONObject(i);
                        dealersDAOList.add(new DealersDAO(json_data.getString("id"), json_data.getString("dealer_details"), json_data.getString("bill_to"), json_data.getString("ship_to")));

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        bAdapter = new ArrayAdapter<DealersDAO>(AddressModifyActivity.this, R.layout.item, dealersDAOList);
                        SearchDealer.setAdapter(bAdapter);

                        SearchDealer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                                // String s = parent.getItemAtPosition(i).toString();
                                DealersDAO student = (DealersDAO) parent.getAdapter().getItem(i);
                                dealer_id = student.getId();
                                billToEdtTxt.setVisibility(View.VISIBLE);
                                shipToEdtTxt.setVisibility(View.VISIBLE);
                                updatedealerAddress.setVisibility(View.VISIBLE);
                                billToEdtTxt.setText(student.getBill_to());
                                shipToEdtTxt.setText(student.getShip_to());
                                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            }
                        });
                        bAdapter.notifyDataSetChanged();

                    }
                });


            }
        });

        objectThread.start();

    }

    private class updateData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(AddressModifyActivity.this);
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
                        put("pID", dealer_id);
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

                Toast.makeText(AddressModifyActivity.this, "Please check your network connection.", Toast.LENGTH_LONG).show();

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void args) {

            if (status1) {
                // mListener.messageReceived(message1);
                Toast.makeText(AddressModifyActivity.this, message1, Toast.LENGTH_LONG).show();

                // Close the progressdialog
                mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();
            }
        }
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
}

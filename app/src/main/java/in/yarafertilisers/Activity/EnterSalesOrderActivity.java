package in.yarafertilisers.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.yarafertilisers.Adapter.EnterProductListAdpter;
import in.yarafertilisers.JsonUtils.JsonHelper;
import in.yarafertilisers.Models.DealersDAO;
import in.yarafertilisers.Models.EnterProductDAO;
import in.yarafertilisers.Models.ProductsDAO;
import in.yarafertilisers.R;
import in.yarafertilisers.Utils.AppStatus;
import in.yarafertilisers.Utils.Config;
import in.yarafertilisers.Utils.Constant;
import in.yarafertilisers.Utils.FeesListener;
import in.yarafertilisers.Utils.WebClient;
import in.yarafertilisers.View.AddressModifyView;
import in.yarafertilisers.View.ProductDetailsView;

public class EnterSalesOrderActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    AutoCompleteTextView SearchDealer;
    EditText remarksEdtTxt, enterQtyEdtTxt;
    Button enterProducts, addSalesOrder, finishSales;
    String dealerResponse = "";
    JSONObject jsonSchedule, jsonObj, jsonLeadObj, jsonScheduleQty, jsonObjaddSalesOrder, jsonLeadObjSaveOrder;
    JSONArray jsonArray;
    DealersDAO dealersDAO;
    public List<DealersDAO> dealersDAOList;
    public ArrayAdapter<DealersDAO> bAdapter;
    String dealer_id = "", comments = "", addSalesResponse = "", msg = "", sales_order_id = "", DisplayProductsNotInSalesOrderResponse = "",
            product_id = "", qty = "", qty_flag = "", CheckQtyInLimitsResponse = "", addSalesOrderResponse = "", saveOrderResponse = "", message1 = "",
            enterProductListResponse = "";
    boolean click = true;
    boolean clickAddSales = true;
    boolean status, status1;
    Spinner spnrProduct;
    ProgressDialog mProgressDialog;
    ArrayList<ProductsDAO> productlist;
    LinearLayout showhide, hideLayout;
    List<EnterProductDAO> data;
    RecyclerView mProductList;
    EnterProductListAdpter enterProductListAdpter;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    ImageView clear_dealer, modify_dealer;
    String Bill_to = "", Ship_to = "";
    Spinner spinnerCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_sales_order);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        SearchDealer = (AutoCompleteTextView) findViewById(R.id.SearchDealer);
        remarksEdtTxt = (EditText) findViewById(R.id.remarksEdtTxt);
        enterQtyEdtTxt = (EditText) findViewById(R.id.enterQtyEdtTxt);
        enterProducts = (Button) findViewById(R.id.enterProducts);
        addSalesOrder = (Button) findViewById(R.id.addSalesOrder);
        finishSales = (Button) findViewById(R.id.finishSales);
        showhide = (LinearLayout) findViewById(R.id.showhide);
        hideLayout = (LinearLayout) findViewById(R.id.hideLayout);
        dealersDAOList = new ArrayList<DealersDAO>();
        clear_dealer = (ImageView) findViewById(R.id.clear_dealer);
        modify_dealer = (ImageView) findViewById(R.id.modify_dealer);
        mProductList = (RecyclerView) findViewById(R.id.enterProductList);
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
                modify_dealer.setVisibility(View.GONE);
                dealer_id = "";
                if (dealerResponse.compareTo("") != 0) {
                    dealersDAOList.clear(); // this list which you hava passed in Adapter for your listview
                    bAdapter.notifyDataSetChanged(); // notify to listview for refresh
                }


            }
        });
        enterProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    comments = remarksEdtTxt.getText().toString().trim();
                    prefEditor.putString("sales_comments_team", comments);
                    prefEditor.commit();
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    if (validate(dealer_id, comments)) {
                        if (click) {
                            //Toast.makeText(context, "Thank You! your information is added Successfully", Toast.LENGTH_LONG).show();
                            enterProducts.setVisibility(View.GONE);
                            sendData(dealer_id, comments);
                            click = false;
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Wait...", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
        enterQtyEdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newTextdealer = s.toString();
                getCheckValidQty(newTextdealer);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        addSalesOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    qty = enterQtyEdtTxt.getText().toString().trim();
                    if (validateProduct(product_id, qty_flag)) {
                        if (clickAddSales) {
                            InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                            addSalesOrderData(product_id, qty);
                            clickAddSales = false;
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Wait...", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });

        finishSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(EnterSalesOrderActivity.this);
                    alertDialog.setTitle("Save sales order");
                    alertDialog.setMessage("Do you want save sales order");


                    // alertDialog.setIcon(R.drawable.msg_img);
                    alertDialog.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    new saveData().execute();

                                }
                            });

                    alertDialog.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });

                    alertDialog.show();


                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
        EnterProductListAdpter.bindListener(new FeesListener() {
            @Override
            public void messageReceived(String messageText) {
                new getEnterProductList().execute();
            }
        });
        AddressModifyView.bindListener(new FeesListener() {
            @Override
            public void messageReceived(String messageText) {
                SearchDealer.setText("");
                clear_dealer.setVisibility(View.GONE);
                modify_dealer.setVisibility(View.GONE);
                dealer_id = "";
                if (dealerResponse.compareTo("") != 0) {
                    dealersDAOList.clear(); // this list which you hava passed in Adapter for your listview
                    bAdapter.notifyDataSetChanged(); // notify to listview for refresh
                }
            }
        });

        modify_dealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

                    if (!dealer_id.equals("")) {
                        prefEditor.putString("dealer_id", dealer_id);
                        prefEditor.putString("Ship_to", Ship_to);
                        prefEditor.putString("Bill_to", Bill_to);
                        prefEditor.commit();
                        AddressModifyView addressModifyView = new AddressModifyView();
                        addressModifyView.show(getSupportFragmentManager(), "addressModifyView");

                    } else {
                        Toast.makeText(getApplicationContext(), "Please select dealer name", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
        // new getEnterProductList().execute();

        if (!preferences.getString("sales_order_id_temp", "").equals("0")) {
            sales_order_id = preferences.getString("sales_order_id_temp", "");
            if (!sales_order_id.equals("")) {
                SearchDealer.setText(preferences.getString("dealer_name_team", ""));
                remarksEdtTxt.setText(preferences.getString("sales_comments_team", ""));
                enterProducts.setVisibility(View.GONE);
                showhide.setVisibility(View.VISIBLE);
                // new initDisplayProductsNotInSalesOrderSpinner().execute();
                enterQtyEdtTxt.setText("");
                new getEnterProductList().execute();
            }

        }

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
                        bAdapter = new ArrayAdapter<DealersDAO>(EnterSalesOrderActivity.this, R.layout.item, dealersDAOList);
                        SearchDealer.setAdapter(bAdapter);

                        SearchDealer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                                // String s = parent.getItemAtPosition(i).toString();
                                DealersDAO student = (DealersDAO) parent.getAdapter().getItem(i);

                                dealer_id = student.getId();
                                Bill_to = student.getBill_to();
                                Ship_to = student.getShip_to();
                                prefEditor.putString("dealer_name_team", student.getDealer_details());
                                prefEditor.commit();
                                modify_dealer.setVisibility(View.VISIBLE);
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

    public void getCheckValidQty(final String channelPartnerSelect) {

        jsonScheduleQty = new JSONObject() {
            {
                try {
                    put("pID", product_id);
                    put("pQty", channelPartnerSelect);

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

                CheckQtyInLimitsResponse = serviceAccess.SendHttpPost(Config.CHECKQTYINLIMITS, jsonScheduleQty);
                Log.i("resp", "CheckQtyInLimitsResponse" + CheckQtyInLimitsResponse);


                try {
                    jsonScheduleQty = new JSONObject(CheckQtyInLimitsResponse);
                    status = jsonScheduleQty.getBoolean("status");
                    msg = jsonScheduleQty.getString("message");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (status) {
                            qty_flag = "1";
                        } else {
                            qty_flag = "0";
                        }

                    }
                });


            }
        });

        objectThread.start();

    }

    public boolean validate(String dealer, String comments) {
        boolean isValidate = false;
        if (dealer.trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Please select dealer.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (comments.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter comments.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else {
            isValidate = true;
        }
        return isValidate;
    }

    public boolean validateProduct(String product_id, String qty) {
        boolean isValidate = false;
        if (product_id.trim().equals("0")) {
            Toast.makeText(getApplicationContext(), "Please select product.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (qty.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter quantity.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else if (qty.equals("0")) {
            Toast.makeText(getApplicationContext(), "Please enter valid quantity.", Toast.LENGTH_LONG).show();
            clickAddSales = true;
            isValidate = false;
        } else {
            isValidate = true;
        }
        return isValidate;
    }

    public void sendData(final String dealer_id, final String comments) {


        jsonObj = new JSONObject() {
            {
                try {
                    put("SalesRepID", preferences.getString("employee_user_id", ""));
                    put("DealerID", dealer_id);
                    put("SalesRepComment", comments);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();

                Log.i("jsonObj", "jsonObj" + jsonObj);
                addSalesResponse = serviceAccess.SendHttpPost(Config.ADDSALESORDERUSINGSTOREPROC, jsonObj);
                Log.i("addSalesResponse", "addSalesResponse" + addSalesResponse);
                final Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() { // This thread runs in the UI
                            @Override
                            public void run() {
                                if (addSalesResponse.compareTo("") == 0) {
                                    //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                                } else {

                                    try {

                                        JSONObject jObject = new JSONObject(addSalesResponse);
                                        status = jObject.getBoolean("status");
                                        msg = jObject.getString("message");
                                        if (status) {
                                            sales_order_id = jObject.getString("sales_order_id");
                                            prefEditor.putString("sales_order_id_temp", jObject.getString("sales_order_id"));
                                            prefEditor.commit();
                                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                            showhide.setVisibility(View.VISIBLE);
                                            new initDisplayProductsNotInSalesOrderSpinner().execute();
                                        }
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                };

                new Thread(runnable).start();
            }
        });
        objectThread.start();
    }

    public void addSalesOrderData(final String product_id, final String pqty) {


        jsonObjaddSalesOrder = new JSONObject() {
            {
                try {
                    put("SalesOrderID", sales_order_id);
                    put("ProductID", product_id);
                    put("Qty", pqty);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();

                Log.i("jsonObj", "jsonObj" + jsonObjaddSalesOrder);
                addSalesOrderResponse = serviceAccess.SendHttpPost(Config.ADDSALESORDERLINEITEMSUSINGSTOREPROC, jsonObjaddSalesOrder);
                Log.i("addSalesResponse", "addSalesOrderResponse" + addSalesOrderResponse);
                final Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() { // This thread runs in the UI
                            @Override
                            public void run() {
                                if (addSalesOrderResponse.compareTo("") == 0) {
                                    //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                                } else {

                                    try {

                                        JSONObject jObject = new JSONObject(addSalesOrderResponse);
                                        status = jObject.getBoolean("status");
                                        msg = jObject.getString("message");
                                        if (status) {
                                            clickAddSales = true;
                                            enterQtyEdtTxt.setText("");
                                            new getEnterProductList().execute();

                                        }
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                };

                new Thread(runnable).start();
            }
        });
        objectThread.start();
    }

    //
    private class initDisplayProductsNotInSalesOrderSpinner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(EnterSalesOrderActivity.this);
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
                        // put("user_id", "" + preferences.getInt("user_id", 0));
                        put("SalesOrderID", sales_order_id);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/lms/api/lead/showlead";
            Log.i("json", "json" + jsonLeadObj);
            DisplayProductsNotInSalesOrderResponse = serviceAccess.SendHttpPost(Config.DISPLAYPRODUCTSNOTINSALESORDER, jsonLeadObj);
            Log.i("resp", "DisplayProductsNotInSalesOrderResponse" + DisplayProductsNotInSalesOrderResponse);

            if (DisplayProductsNotInSalesOrderResponse.compareTo("") != 0) {
                if (isJSONValid(DisplayProductsNotInSalesOrderResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                productlist = new ArrayList<>();
                                productlist.add(new ProductsDAO("0", "Select Product"));
                                JSONArray LeadSourceJsonObj = new JSONArray(DisplayProductsNotInSalesOrderResponse);
                                for (int i = 0; i < LeadSourceJsonObj.length(); i++) {
                                    JSONObject json_data = LeadSourceJsonObj.getJSONObject(i);
                                    productlist.add(new ProductsDAO(json_data.getString("id"), json_data.getString("product_name"), json_data.getString("order_in")));

                                }

                                // jsonArray = new JSONArray(DisplayProductsNotInSalesOrderResponse);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (productlist.size() > 0) {


                spinnerCustom = (Spinner) findViewById(R.id.spnrProduct);
                ArrayAdapter<ProductsDAO> adapter = new ArrayAdapter<ProductsDAO>(EnterSalesOrderActivity.this, android.R.layout.simple_spinner_dropdown_item, productlist);

                spinnerCustom.setAdapter(adapter);
                spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                        ProductsDAO LeadSource = (ProductsDAO) parent.getSelectedItem();
                        //  Toast.makeText(getApplicationContext(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getBranch_name(), Toast.LENGTH_SHORT).show();
                        product_id = LeadSource.getId();
                        if (!product_id.equals("0")) {
                            enterQtyEdtTxt.setHint("* Enter Qty in " + LeadSource.getOrder_in());
                            enterQtyEdtTxt.setVisibility(View.VISIBLE);
                        } else {
                            enterQtyEdtTxt.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }


                });
                mProgressDialog.dismiss();

            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();
            }
        }
    }

    private class saveData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(EnterSalesOrderActivity.this);
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
                        put("SalesOrderID", sales_order_id);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };


            WebClient serviceAccess = new WebClient();
            saveOrderResponse = serviceAccess.SendHttpPost(Config.SAVEORDERUSINGSTOREPROC, jsonLeadObjSaveOrder);
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

                Toast.makeText(EnterSalesOrderActivity.this, "Please check your network connection.", Toast.LENGTH_LONG).show();

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void args) {

            if (status1) {
                // mListener.messageReceived(message1);
                prefEditor.putString("sales_order_id_temp", "0");
                prefEditor.putString("dealer_name_team", "");
                prefEditor.putString("sales_comments_team", "");
                prefEditor.commit();
                finish();
                Intent intent = new Intent(EnterSalesOrderActivity.this, YaraDashBoardActivity.class);
                startActivity(intent);
                // Close the progressdialog
                mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();
            }
        }
    }

    //
    private class getEnterProductList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(EnterSalesOrderActivity.this);
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
                        put("SalesOrderID", sales_order_id);
                        //put("SalesOrderID", 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/srujanlms_new/api/Leadraw/showleadraw";
            Log.i("json", "json" + jsonLeadObj);
            enterProductListResponse = serviceAccess.SendHttpPost(Config.SHOWPENDINGSALESORDERLINEITEM, jsonLeadObj);
            Log.i("resp", "enterProductListResponse" + enterProductListResponse);
            data = new ArrayList<>();
            if (enterProductListResponse.compareTo("") != 0) {
                if (isJSONValid(enterProductListResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {


                                JsonHelper jsonHelper = new JsonHelper();
                                data = jsonHelper.parseEnterProductList(enterProductListResponse);
                                jsonArray = new JSONArray(enterProductListResponse);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (data.size() > 0) {
                hideLayout.setVisibility(View.VISIBLE);
                enterProductListAdpter = new EnterProductListAdpter(EnterSalesOrderActivity.this, data);
                mProductList.setAdapter(enterProductListAdpter);
                mProductList.setLayoutManager(new LinearLayoutManager(EnterSalesOrderActivity.this));
                // Close the progressdialog
                mProgressDialog.dismiss();
                new initDisplayProductsNotInSalesOrderSpinner().execute();
            } else {
                hideLayout.setVisibility(View.GONE);
                enterProductListAdpter = new EnterProductListAdpter(EnterSalesOrderActivity.this, data);
                mProductList.setAdapter(enterProductListAdpter);
                mProductList.setLayoutManager(new LinearLayoutManager(EnterSalesOrderActivity.this));
                // Close the progressdialog
                mProgressDialog.dismiss();
                new initDisplayProductsNotInSalesOrderSpinner().execute();
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

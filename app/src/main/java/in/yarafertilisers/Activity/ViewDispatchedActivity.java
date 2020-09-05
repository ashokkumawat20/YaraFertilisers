package in.yarafertilisers.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.yarafertilisers.Adapter.ViewDispatchedListAdpter;
import in.yarafertilisers.Adapter.ViewInvoicedListAdpter;
import in.yarafertilisers.JsonUtils.JsonHelper;
import in.yarafertilisers.Models.ViewInvoicedDAO;
import in.yarafertilisers.R;
import in.yarafertilisers.Utils.Config;
import in.yarafertilisers.Utils.WebClient;

public class ViewDispatchedActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    RecyclerView mProductList;
    ProgressDialog mProgressDialog;
    JSONObject jsonLeadObj;
    String viewInvoicedListResponse = "";
    JSONArray jsonArray;
    ViewDispatchedListAdpter viewDispatchedListAdpter;
    List<ViewInvoicedDAO> data;

    ImageView serach_hide, clear;
    public EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dispatched);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        mProductList = (RecyclerView) findViewById(R.id.pendingSOList);
        search = (EditText) findViewById(R.id.search);
        serach_hide = (ImageView) findViewById(R.id.serach_hide);
        clear = (ImageView) findViewById(R.id.clear);
        new getViewDispactedList().execute();
        serach_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                clear.setVisibility(View.VISIBLE);
                serach_hide.setVisibility(View.GONE);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText("");
                serach_hide.setVisibility(View.VISIBLE);
                clear.setVisibility(View.GONE);

            }
        });
        addTextListener();
    }

    //
    public void addTextListener() {

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {
                clear.setVisibility(View.VISIBLE);
                serach_hide.setVisibility(View.GONE);

                query = query.toString().toLowerCase();
                final List<ViewInvoicedDAO> filteredList = new ArrayList<ViewInvoicedDAO>();
                if (data != null) {
                    if (data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {

                            String subject = data.get(i).getDealer_name().toLowerCase();
                            String tag = data.get(i).getOrder_no().toLowerCase();
                            String msg_txt = data.get(i).getSales_rep_comment().toLowerCase();
                            if (subject.contains(query)) {
                                filteredList.add(data.get(i));
                            } else if (tag.contains(query)) {

                                filteredList.add(data.get(i));
                            } else if (msg_txt.contains(query)) {

                                filteredList.add(data.get(i));
                            }
                        }
                    }
                }

                viewDispatchedListAdpter = new ViewDispatchedListAdpter(ViewDispatchedActivity.this, filteredList);
                mProductList.setAdapter(viewDispatchedListAdpter);
                mProductList.setLayoutManager(new LinearLayoutManager(ViewDispatchedActivity.this));
                viewDispatchedListAdpter.notifyDataSetChanged();  // data set changed
            }
        });
    }

    private class getViewDispactedList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ViewDispatchedActivity.this);
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

                        put("SalesRepID", preferences.getString("employee_user_id", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/srujanlms_new/api/Leadraw/showleadraw";
            Log.i("json", "json" + jsonLeadObj);
            viewInvoicedListResponse = serviceAccess.SendHttpPost(Config.SHOWDISPATCHEDINVOICESALESORDER, jsonLeadObj);
            Log.i("resp", "viewInvoicedListResponse" + viewInvoicedListResponse);
            data = new ArrayList<>();
            if (viewInvoicedListResponse.compareTo("") != 0) {
                if (isJSONValid(viewInvoicedListResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {


                                JsonHelper jsonHelper = new JsonHelper();
                                data = jsonHelper.parseViewInvoicedList(viewInvoicedListResponse);
                                jsonArray = new JSONArray(viewInvoicedListResponse);
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
                viewDispatchedListAdpter = new ViewDispatchedListAdpter(ViewDispatchedActivity.this, data);
                mProductList.setAdapter(viewDispatchedListAdpter);
                mProductList.setLayoutManager(new LinearLayoutManager(ViewDispatchedActivity.this));
                // Close the progressdialog
                mProgressDialog.dismiss();
                // new initDisplayProductsNotInSalesOrderSpinner().execute();
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

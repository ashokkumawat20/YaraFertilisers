package in.yarafertilisers.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.yarafertilisers.R;
import in.yarafertilisers.Utils.AppStatus;
import in.yarafertilisers.Utils.Config;
import in.yarafertilisers.Utils.Constant;
import in.yarafertilisers.Utils.WebClient;

public class YaraDashBoardActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    private JSONObject jsonObj,jsonObj1;
    String verifyMobileDeviceIdResponse = "",countResponse="";
    boolean status;
    String deviceId = "";
    TelephonyManager telephonyManager;
    LinearLayout enterSalesOrder, viewPendingSO, viewInvoicedSO, viewTwoMonthsDispatchedSO,viewAddressModify;
    TextView pendingSO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yara_dash_board);

        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        enterSalesOrder = (LinearLayout) findViewById(R.id.enterSalesOrder);
        viewPendingSO = (LinearLayout) findViewById(R.id.viewPendingSO);
        viewInvoicedSO = (LinearLayout) findViewById(R.id.viewInvoicedSO);
        viewTwoMonthsDispatchedSO = (LinearLayout) findViewById(R.id.viewTwoMonthsDispatchedSO);
        viewAddressModify= (LinearLayout) findViewById(R.id.viewAddressModify);
        pendingSO=(TextView)findViewById(R.id.pendingSO);
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            deviceId = getDeviceId();
            verifyMobileDeviceId();
        } else {

            //Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
        }
        enterSalesOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    Intent intent = new Intent(YaraDashBoardActivity.this, EnterSalesOrderActivity.class);
                    startActivity(intent);
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewPendingSO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    finish();
                    Intent intent = new Intent(YaraDashBoardActivity.this, ViewPendingSOActivity.class);
                    startActivity(intent);
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewInvoicedSO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    Intent intent = new Intent(YaraDashBoardActivity.this, ViewInvoicedSOActivity.class);
                    startActivity(intent);
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewTwoMonthsDispatchedSO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    Intent intent = new Intent(YaraDashBoardActivity.this, ViewDispatchedActivity.class);
                    startActivity(intent);
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewAddressModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    Intent intent = new Intent(YaraDashBoardActivity.this, AddressModifyActivity.class);
                    startActivity(intent);
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    public String getDeviceId() {

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = telephonyManager.getDeviceId();
        return deviceId;
    }

    public void verifyMobileDeviceId() {


        jsonObj = new JSONObject() {
            {
                try {
                    put("pDeviceID", deviceId);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        jsonObj1 = new JSONObject() {
            {
                try {
                    put("SalesRepID", preferences.getString("employee_user_id",""));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();
                countResponse = serviceAccess.SendHttpPost(Config.GETPENDINGSALESORDERCOUNTID, jsonObj1);
                Log.i("countResponse", "countResponse" + countResponse);

                verifyMobileDeviceIdResponse = serviceAccess.SendHttpPost(Config.GETAVAILABLEMOBILEDEVICEID, jsonObj);
                Log.i("loginResponse", "verifyMobileDeviceIdResponse" + verifyMobileDeviceIdResponse);
                final Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() { // This thread runs in the UI
                            @Override
                            public void run() {
                                if (verifyMobileDeviceIdResponse.compareTo("") == 0) {

                                } else {

                                    try {
                                        JSONObject jObject = new JSONObject(verifyMobileDeviceIdResponse);
                                        JSONObject jObject1 = new JSONObject(countResponse);
                                        status = jObject.getBoolean("status");

                                        if (status) {
                                            pendingSO.setText(jObject1.getString("count"));

                                        } else {

                                            finish();
                                            prefEditor.putString("employee_user_id", "");
                                            prefEditor.commit();
                                            Intent intent = new Intent(YaraDashBoardActivity.this, VerifyEmpActivity.class);
                                            startActivity(intent);
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
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}

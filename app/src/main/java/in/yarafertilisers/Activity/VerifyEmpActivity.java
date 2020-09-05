package in.yarafertilisers.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.yarafertilisers.R;
import in.yarafertilisers.Utils.AppStatus;
import in.yarafertilisers.Utils.Config;
import in.yarafertilisers.Utils.Constant;
import in.yarafertilisers.Utils.WebClient;

public class VerifyEmpActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    String deviceId = "";
    TelephonyManager telephonyManager;
    Button verifyButton;
    EditText verify_emailid, verify_pin;
    String email = "";
    String pin = "";
    ProgressDialog mProgressDialog;
    private JSONObject jsonObj, jsonObject;
    JSONArray jsonArray;
    String employeeYaraResponse = "";

    Boolean status;
    String user_id = "";
    String msg = "";
    static final Integer PHONE_STATE = 0x5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_emp);

        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        verifyButton = (Button) findViewById(R.id.verifyButton);
        verify_emailid = (EditText) findViewById(R.id.verify_emailid);
        verify_pin = (EditText) findViewById(R.id.verify_pin);

        askForPermission(Manifest.permission.READ_PHONE_STATE, PHONE_STATE);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    deviceId = getDeviceId();
                    email = verify_emailid.getText().toString().trim();
                    pin = verify_pin.getText().toString().trim();
                    if (validate(email, pin)) {

                        new userAvailable().execute();

                    }
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(VerifyEmpActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(VerifyEmpActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(VerifyEmpActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(VerifyEmpActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            // Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    public String getDeviceId() {

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = telephonyManager.getDeviceId();
        return deviceId;
    }
//

    private class userAvailable extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(VerifyEmpActivity.this);
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
            jsonObj = new JSONObject() {
                {
                    try {

                        put("email_id", email);
                        put("pin", pin);
                        put("device_id", deviceId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonObj);
            employeeYaraResponse = serviceAccess.SendHttpPost(Config.AVAILABLEAUTHORIZESALESREPANDRETURNID, jsonObj);
            Log.i("resp", "employeeYaraResponse" + employeeYaraResponse);


            if (employeeYaraResponse.compareTo("") != 0) {
                if (isJSONValid(employeeYaraResponse)) {


                    try {

                        jsonObject = new JSONObject(employeeYaraResponse);
                        status = jsonObject.getBoolean("status");
                        msg = jsonObject.getString("message");
                        user_id = jsonObject.getString("emp_user_id");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                }
            } else {

                Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (status) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                prefEditor.putString("employee_user_id", user_id);
                prefEditor.commit();
                finish();
                Intent intent = new Intent(VerifyEmpActivity.this, YaraDashBoardActivity.class);
                startActivity(intent);
                // Close the progressdialog
                mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                mProgressDialog.dismiss();

            }
        }
    }

    public boolean validate(String email, String pin) {
        boolean isValidate = false;
        if (email.trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter email id.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (pin.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter pin No.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else {
            isValidate = true;
        }
        return isValidate;
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

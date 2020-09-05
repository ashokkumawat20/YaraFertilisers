package in.yarafertilisers.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import in.yarafertilisers.R;


public class SplashActivity extends AppCompatActivity {
    String msg = "Android : ";
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        init();




    }

    /**
     * Called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(msg, "The onStart() event");
        init();


    }

    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "The onResume() event");
        init();


    }

    /**
     * Called when another activity is taking focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "The onPause() event");

    }

    /**
     * Called when the activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "The onStop() event");

    }

    /**
     * Called just before the activity is destroyed.
     */
    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.d(msg, "The onDestroy() event");

    }

    public void init() {
        if (!preferences.getString("employee_user_id", "").equals("")) {
            Intent intent = new Intent(SplashActivity.this, YaraDashBoardActivity.class);
            startActivity(intent);
        } else {

            Intent intent = new Intent(SplashActivity.this, VerifyEmpActivity.class);
            startActivity(intent);
        }

    }





}


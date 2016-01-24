package com.gtechoogle.autoreddetect;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gtechoogle.autoreddetect.utils.Utils;

public class MainActivity extends Activity {
    private Button mBt;
    private static String FILE_NAME = "red_packet";
    private static String KEY_SWITCH_STATE = "switch_state";
    private static int sSwitchState;
    private static final Intent sSettingsIntent =
            new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sSwitchState = readSwitchState();
        Utils.UILog("mSwitchState = " + sSwitchState);
        mBt = (Button) findViewById(R.id.start);
        mBt.setText(sSwitchState == Utils.ON ? R.string.start_successful : R.string.start);
        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sSwitchState == Utils.OFF) {
                    launchAutoMoneyDetect();
                }
            }
        });
    }

    private void launchAutoMoneyDetect() {
        startActivity(sSettingsIntent);
    }

    private int readSwitchState() {
        return Utils.isAccessibilitySettingsOn(this) ? Utils.ON : Utils.OFF;
    }
    private void writeSwitchState(int value) {
//        SharedPreferences mySharedPreferences= getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mySharedPreferences.edit();
//        editor.putInt(KEY_SWITCH_STATE, value);
//        editor.commit();
        startActivity(sSettingsIntent);
    }
    public static int getSwitchState() {
        return sSwitchState;
    }
}

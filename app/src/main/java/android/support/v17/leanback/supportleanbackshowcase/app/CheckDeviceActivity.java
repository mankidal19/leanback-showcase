/*
 * Created by Nurul Aiman, as an Open Source Project
 * Documented on 04/01/2019
 * Other interesting source code can be found at https://bitbucket.org/mankidal19/
 */
package android.support.v17.leanback.supportleanbackshowcase.app;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import android.support.v17.leanback.supportleanbackshowcase.app.mobile.activity.MobileMainActivity;

/**
 * This class is the first activity launched when the app is first opened
 * to check either the device is an Android TV or not. If the device is an
 * Android TV, start activity MainActivity. Else, start activity MobileMainActivity.
 */
public class CheckDeviceActivity extends Activity {

    private static String TAG = "CheckDeviceActivity";
    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
            Log.d(TAG, "Running on a TV Device");
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Log.d(TAG, "Running on a non-TV Device");
            intent = new Intent(this, MobileMainActivity.class);
            startActivity(intent);
        }
    }
}

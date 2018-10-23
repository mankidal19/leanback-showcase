package nurulaiman.sony.activity;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import nurulaiman.sony.mobile.activity.MobileMainActivity;

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

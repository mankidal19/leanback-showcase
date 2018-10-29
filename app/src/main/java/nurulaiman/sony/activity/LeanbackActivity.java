package nurulaiman.sony.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;

public class LeanbackActivity extends Activity {

    private static String TAG = "LeanbackActivity";


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_PROG_RED:
                if (action == KeyEvent.ACTION_DOWN) {
                    //sendBroadcast();
                    sendShortcutBroadcast(event);
                }
                return true;
            case KeyEvent.KEYCODE_PROG_GREEN:
                if (action == KeyEvent.ACTION_DOWN) {
                    //sendBroadcast();
                    sendShortcutBroadcast(event);
                }
                return true;
            case KeyEvent.KEYCODE_PROG_YELLOW:
                if (action == KeyEvent.ACTION_DOWN) {
                    //sendBroadcast();
                    sendShortcutBroadcast(event);
                }
                return true;
            case KeyEvent.KEYCODE_PROG_BLUE:
                if (action == KeyEvent.ACTION_DOWN) {
                    //sendBroadcast();
                    sendShortcutBroadcast(event);
                }
                return true;

            default:
                return super.dispatchKeyEvent(event);
        }
    }

    protected void  sendShortcutBroadcast(KeyEvent keyEvent){
        Intent intent = new Intent("activity-says-hi");

       intent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);

        if(!(this instanceof MainActivity)){

            //setContentView(R.layout.fragment_main_browse);
            Log.d(TAG,"Not instanceof MainActivity");
            super.onBackPressed();
            //startActivity(new Intent(this,MainActivity.class));
            //LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        }



        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

}

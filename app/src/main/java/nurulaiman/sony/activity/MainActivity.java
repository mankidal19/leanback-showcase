package nurulaiman.sony.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;

import nurulaiman.sony.fragment.MainBrowseFragment;

public class MainActivity extends Activity {

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_browse);

        //MainBrowseFragment fragment = MainBrowseFragment.newInstance();

    }

    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        appData.putBoolean(SearchableActivity.JARGON, true);
        startSearch(null, false, appData, false);
        Log.i(TAG,"search requested");
        return true;
    }


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

            case KeyEvent.KEYCODE_BACK:
                if(action==KeyEvent.ACTION_DOWN){
                    finish();
                    startActivity(getIntent());
                }
                return true;

            case KeyEvent.KEYCODE_ESCAPE:
                if(action==KeyEvent.ACTION_DOWN){
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
                return true;



            default:
                return super.dispatchKeyEvent(event);
        }
    }

    private void  sendShortcutBroadcast(KeyEvent keyEvent){
        Intent intent = new Intent("activity-says-hi");

        //intent.putExtra("keyEvent",keyEvent);
        intent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}

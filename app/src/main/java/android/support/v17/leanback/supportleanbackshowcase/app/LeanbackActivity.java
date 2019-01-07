/*
 * Created by Nurul Aiman, as an Open Source Project
 * Documented on 04/01/2019
 * Other interesting source code can be found at https://bitbucket.org/mankidal19/
 */

package android.support.v17.leanback.supportleanbackshowcase.app;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;


/**
 *This class is the parent activity to other activity classes in this project
 * to receive and send the RGYB button input when available to its child class
 * currently in display.
 */
public class LeanbackActivity extends Activity {

    private static String TAG = "LeanbackActivity";


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_PROG_RED:
            case KeyEvent.KEYCODE_PROG_GREEN:
            case KeyEvent.KEYCODE_PROG_YELLOW:
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
            finish();

            //startActivity(new Intent(this,MainActivity.class));
            //LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        }



        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG,"onActivityResult");

        if (requestCode == 101) {

            if(data!=null){
                KeyEvent event = data.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                sendShortcutBroadcast(event);
            }
            finishActivity(101);

            finishActivity(101);
            if(resultCode == Activity.RESULT_OK){
                //String result=data.getStringExtra("result");
               /* KeyEvent event = data.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                sendShortcutBroadcast(event);*/
                Log.d(TAG,"result ok");
                //finishActivity(101);

                //finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Log.d(TAG,"result cancelled");
                finish();

            }
        }
    }



}

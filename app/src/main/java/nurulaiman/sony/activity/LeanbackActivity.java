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

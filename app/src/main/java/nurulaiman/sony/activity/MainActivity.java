package nurulaiman.sony.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.VideoView;

import nurulaiman.sony.fragment.MainBrowseFragment;
import nurulaiman.sony.fragment.MySettingsFragment;

import static android.content.Intent.EXTRA_KEY_EVENT;

public class MainActivity extends LeanbackActivity {

    private static String TAG = "MainActivity";
    private  KeyEvent keyEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_main_browse);
        setContentView(R.layout.activity_main);

        String interfaceMode = MySettingsFragment.getDefaults("pref_interface_key",this);

        if(interfaceMode.equals("enduser")){
            //video background
            VideoView videoview = (VideoView) findViewById(R.id.videoView);
            Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.polina);
            videoview.setVideoURI(uri);
            videoview.start();

            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
        }


        MainBrowseFragment fragment = MainBrowseFragment.newInstance();
        //MainBrowseFragment fragment = new MainBrowseFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment)
                .commit();
        Log.d(TAG,"onCreate called");


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //to debug issue RGYB shortcuts not working after voice search

        //super.onSaveInstanceState(outState);
        Log.d(TAG,"onSaveInstanceState called");
    }

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

       /* if(getIntent().getExtras()!=null){
            keyEvent = (KeyEvent) getIntent().getExtras().get(Intent.EXTRA_KEY_EVENT);
            Log.d(TAG,"getExtras not null");

            if(keyEvent!=null){
                super.sendShortcutBroadcast(keyEvent);
                Log.d(TAG,"keyEvent not null");
                getIntent().removeExtra(Intent.EXTRA_KEY_EVENT);
            }

        }

        else{
            Log.d(TAG,"getExtras null");

        }*/


        return super.onCreateView(name, context, attrs);
    }

    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        appData.putBoolean(SearchableActivity.JARGON, true);
        startSearch(null, false, appData, false);
        Log.d(TAG,"search requested");
        return true;
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        Log.d(TAG,"onActivityResult");

        if (requestCode == 1) {
            Log.d(TAG,"onActivityResult: request code 1");
            Log.d(TAG,"onActivityResult: result code "+resultCode);

            if(resultCode==Activity.RESULT_OK){
                Log.d(TAG,"onActivityResult: OK");
                recreate();
            }

        }
    }

}

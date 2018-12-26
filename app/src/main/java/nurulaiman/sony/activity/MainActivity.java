package nurulaiman.sony.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import nurulaiman.sony.fragment.MainBrowseFragment;
import nurulaiman.sony.fragment.MySettingsFragment;

import static android.content.Intent.EXTRA_KEY_EVENT;

public class MainActivity extends LeanbackActivity {

    private static String TAG = "MainActivity";
    private  KeyEvent keyEvent;
    private VideoView videoView = null;
    private String interfaceMode;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_main_browse);
        setContentView(R.layout.activity_main);


        interfaceMode = MySettingsFragment.getDefaults("pref_interface_key",this);
        provider = MySettingsFragment.getDefaults("pref_providers_key",this);

        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setFocusable(false);

        Log.d(TAG,"interfaceMode, provider: "+ interfaceMode + ", "+provider);

        //for first time after installing
        if(interfaceMode == null){
            MySettingsFragment.setDefaults("pref_interface_key","enduser",this);
            interfaceMode = MySettingsFragment.getDefaults("pref_interface_key",this);

            Log.d(TAG,"new interfaceMode: "+ interfaceMode);

        }

        if(provider == null){
            MySettingsFragment.setDefaults("pref_providers_key","fptplay",this);
            provider = MySettingsFragment.getDefaults("pref_providers_key",this);

            Log.d(TAG,"new provider: "+ provider);

        }


        //video background only for enduser of fptplay & hotstar
        if(interfaceMode.equals("enduser")&&provider.equals("fptplay")
                ||interfaceMode.equals("enduser")&&provider.equals("hotstar") ){

                //video background
                //videoView = (VideoView) findViewById(R.id.videoView);

                if(provider.equals("fptplay")){
                    Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.fptplay);
                    videoView.setVideoURI(uri);
                }

                else if(provider.equals("hotstar")){
                    Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.hotstar_promo);
                    videoView.setVideoURI(uri);
                }

                videoView.setFocusable(false);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setLooping(true);
                        mp.setVolume(0f,0f);
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
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume called");



        if(interfaceMode.equals("enduser")&&provider.equals("fptplay")
                ||interfaceMode.equals("enduser")&&provider.equals("hotstar") ){
            videoView.start();
            Log.d(TAG,"start video");

        }

        else if(videoView!=null){
            videoView.setFocusable(false);
            Log.d(TAG,"videoview not null, setfocusable to false");

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause called");

        if(interfaceMode.equals("enduser")&&provider.equals("fptplay")
                ||interfaceMode.equals("enduser")&&provider.equals("hotstar") ){
            videoView.suspend();
            Log.d(TAG,"suspend video");

        }

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

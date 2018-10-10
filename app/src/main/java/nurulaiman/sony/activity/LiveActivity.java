package nurulaiman.sony.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;

import java.util.ArrayList;

import android.support.v17.leanback.supportleanbackshowcase.R;


public class LiveActivity extends FragmentActivity {

    //default live TV
    private String liveVideoId = "FdtQ2ZgLbEs";
    private YouTubePlayerView youTubePlayerView = null;

    //for channel up/down button
    private ArrayList<String> channelArrayList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        initChannelList();

       /* if(getIntent().getExtras().getInt("mediaId") >=0){
            int mediaId = getIntent().getExtras().getInt("mediaId");
            liveVideoId = channelArrayList.get(mediaId);
            Log.i("in Live Activity","video id: " + liveVideoId);
        }*/


        initYouTubePlayerView();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //if(youTubePlayerView !=null){
            //youTubePlayerView.release();
        //}


        finish();

        Log.i("in Live Activity","onBackPressed() is called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(youTubePlayerView !=null){
            //youTubePlayerView.release();
        }

        Log.i("in Live Activity","onPause() is called");

    }

    @Override
    protected void onDestroy(){

        youTubePlayerView.release();
        youTubePlayerView = null;
        super.onDestroy();
        Log.i("in Live Activity","onDestroy() is called");
    }

    @Override
    protected void onStop(){


        super.onStop();
        Log.i("in Live Activity","onStop() is called");
    }


    //to handle channel up & down button
    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event){

        boolean handled = false;
        KeyCode = event.getKeyCode();
        Intent intent = new Intent(this, LiveActivity.class);

        Log.i("KeyEvent",KeyCode + " button pressed");

        if(event.getKeyCode() == KeyEvent.KEYCODE_CHANNEL_DOWN){
            Log.i("KeyEvent","Channel down button pressed");
            handled=true;
            onDestroy();
            //youTubePlayerView.release();
            intent.putExtra("videoId",getPrevChannel());

            startActivity(intent);
        }

        else if(event.getKeyCode() == KeyEvent.KEYCODE_CHANNEL_UP){
            Log.i("KeyEvent","Channel up button pressed");
            handled=true;
            onDestroy();
            intent.putExtra("videoId",getNextChannel());

            //youTubePlayerView.release();

            startActivity(intent);
        }

        else if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            Log.i("KeyEvent","Return button pressed");
            handled=true;
            intent = new Intent(this, MainActivity.class);

            onDestroy();
            startActivity(intent);
        }

        else if(event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE){
            Log.i("KeyEvent","Exit button pressed");
            handled=true;
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }


        if(handled){


            return handled;
        }

        else{
            return super.onKeyDown(KeyCode, event);
        }
        //return handled;
    }





    private void initYouTubePlayerView() {
        //YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.getPlayerUIController().showFullscreenButton(false);
        youTubePlayerView.getPlayerUIController().enableLiveVideoUI(true);

        liveVideoId = getIntent().getExtras().getString("videoId");
        Log.i("in Live Activity","current videoID: "+ liveVideoId);

        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.initialize(youTubePlayer -> {

            youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    youTubePlayer.loadVideo(liveVideoId,0f);

                }


            });

        }, true);
    }

    //initialize channel list ArrayList
    private void initChannelList(){
        channelArrayList.add("FdtQ2ZgLbEs");
        channelArrayList.add("2LWt-dxd0v4");
        channelArrayList.add("DrtjkQAa1lI");
        channelArrayList.add("KkXq2sv6Tos");
        channelArrayList.add("Lvp-lSqHVKc");
    }

    private String getNextChannel(){
        int idx = channelArrayList.indexOf(liveVideoId);

        //if idx is not the last element
        if (idx!=channelArrayList.size()-1){
            idx++;

            return channelArrayList.get(idx);
        }

        else{
            return channelArrayList.get(0);
        }


    }

    private String getPrevChannel(){
        int idx = channelArrayList.indexOf(liveVideoId);

        //if idx is not the first element
        if (idx!=0){
            idx--;

            return channelArrayList.get(idx);
        }

        else{
            return channelArrayList.get(channelArrayList.size()-1);
        }


    }

}

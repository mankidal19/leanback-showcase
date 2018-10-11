package nurulaiman.sony.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;

import java.util.ArrayList;

import android.support.v17.leanback.supportleanbackshowcase.R;
import android.view.View;
import android.widget.TextView;

import nurulaiman.sony.data.MockDatabase;


public class LiveActivity extends FragmentActivity {

    //default live TV
    private String liveVideoId = "FdtQ2ZgLbEs";
    private YouTubePlayerView youTubePlayerView = null;
    //to implement play/pause
    private YouTubePlayer youTubePlayer = null;
    private boolean playing = true;

    //for channel up/down button
    private ArrayList<String> channelArrayList = new ArrayList<String>();

    //to implement channel title
    private String videoTitle = null;
    MockDatabase mockDatabase = new MockDatabase(this);



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

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE){
            Log.i("KeyEvent","Play/Pause button pressed");
            handled=true;
            if(playing){
                youTubePlayer.pause();
                playing=false;
                youTubePlayerView.getPlayerUIController().showUI(true);

            }
            else{
                youTubePlayer.play();
                playing = true;
            }

        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_PLAY){
            Log.i("KeyEvent","Play button pressed");
            handled=true;
            if(!playing){
                youTubePlayer.play();
                playing = true;
            }

        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_PAUSE){
            Log.i("KeyEvent","Pause button pressed");
            handled=true;
            if(playing){
                youTubePlayer.pause();
                playing = false;
                //youTubePlayerView.getPlayerUIController().showSeekBar(true);

            }

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
        //youTubePlayerView.getPlayerUIController().showPlayPauseButton(true);
        //youTubePlayerView.getPlayerUIController().showVideoTitle(true);
        //youTubePlayerView.getPlayerUIController().showUI(true);

        liveVideoId = getIntent().getExtras().getString("videoId");
        Log.i("in Live Activity","current videoID: "+ liveVideoId);

        //to set video title

        //method #1- using intent
        /*videoTitle = getIntent().getExtras().getString("videoTitle");
        if(videoTitle!=null){
            youTubePlayerView.getPlayerUIController().setVideoTitle(videoTitle);
        }*/

        //method #2- using mockdatabase searchCard()
        videoTitle = mockDatabase.searchCard(liveVideoId).getTitle();
        if(videoTitle!=null){
            //youTubePlayerView.getPlayerUIController().setVideoTitle(videoTitle);

            //to display pop-up when changing channel
            //setContentView(R.layout.change_channel);
            TextView textView = findViewById(R.id.textView1);
            textView.setText(videoTitle);

            //hide after 3 seconds
            textView.postDelayed(new Runnable() {
                public void run() {
                    textView.setVisibility(View.GONE);
                }
            }, 5000);
        }






        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.initialize(youTubePlayer -> {

            passYoutubePlayer(youTubePlayer);
            youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    youTubePlayer.loadVideo(liveVideoId,0f);

                }


            });

        }, true);
    }

    private void passYoutubePlayer(YouTubePlayer youTubePlayer) {
        this.youTubePlayer = youTubePlayer;
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

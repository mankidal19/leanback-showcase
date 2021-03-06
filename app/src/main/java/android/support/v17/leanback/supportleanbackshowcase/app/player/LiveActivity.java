package android.support.v17.leanback.supportleanbackshowcase.app.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.app.settings.MySettingsFragment;
import android.support.v17.leanback.supportleanbackshowcase.data.MockDatabase;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;

import java.util.ArrayList;


/**
 * Activity to play live YouTube video
 */
public class LiveActivity extends FragmentActivity {

    private final String TAG = "LiveActivity";
    //default live TV
    private String liveVideoId = "wp-R57ZaiXY";
    private YouTubePlayerView youTubePlayerView = null;
    //to implement play/pause
    private YouTubePlayer youTubePlayer = null;
    private boolean playing = true;

    //for channel up/down button
    private ArrayList<String> channelArrayList = new ArrayList<String>();

    //to implement channel title
    private String videoTitle = null;
    MockDatabase mockDatabase = new MockDatabase(this);
    private TextView textView = null;

    private ImageView iconView = null;
    private int defaultHideTime = 1000;

    private String provider  = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        provider  = MySettingsFragment.getDefaults("pref_providers_key",getApplicationContext());


        initChannelList();
        initYouTubePlayerView();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

        Log.d(TAG,"onBackPressed() is called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(youTubePlayerView !=null){
            //youTubePlayerView.release();
        }

        Log.d(TAG,"onPause() is called");

    }

    /**
     * Releases the current YouTube player view.
     */
    @Override
    protected void onDestroy(){

        youTubePlayerView.release();
        youTubePlayerView = null;
        super.onDestroy();
        Log.d(TAG,"onDestroy() is called");
    }

    @Override
    protected void onStop(){


        super.onStop();
        Log.d(TAG,"onStop() is called");
    }


    /**
     * Overrides onKeyDown method to handle several remote keys such as
     * KEYCODE_CHANNEL_DOWN, KEYCODE_CHANNEL_UP,KEYCODE_ESCAPE and more
     * to behave uniquely in live video player activity only.
     * @param KeyCode KeyCode of the key pressed
     * @param event KeyEvent of the key pressed
     * @return boolean value either key is handled or not
     */
    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event){

        boolean handled = false;
        KeyCode = event.getKeyCode();
        Intent intent = new Intent(this, LiveActivity.class);

        Log.d(TAG,KeyCode + " button pressed");

        if(event.getKeyCode() == KeyEvent.KEYCODE_CHANNEL_DOWN){
            Log.d(TAG,"Channel down button pressed");
            handled=true;

            liveVideoId = getPrevChannel();
            initVideoTitle();


            iconView.setImageDrawable(getDrawable(R.drawable.ic_ch_minus));
            iconView.setVisibility(View.VISIBLE);

            youTubePlayer.loadVideo(liveVideoId,0f);

            hideIconView(defaultHideTime);
        }

        else if(event.getKeyCode() == KeyEvent.KEYCODE_CHANNEL_UP){
            Log.d(TAG,"Channel up button pressed");
            handled=true;
            //onDestroy();

            liveVideoId = getNextChannel();
            initVideoTitle();


            youTubePlayer.loadVideo(liveVideoId,0f);

            iconView.setImageDrawable(getDrawable(R.drawable.ic_ch_plus));
            iconView.setVisibility(View.VISIBLE);

            hideIconView(defaultHideTime);

        }

        else if(event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE){
            Log.d(TAG,"Exit button pressed");
            handled=true;
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE){
            Log.d(TAG,"Play/Pause button pressed");
            handled=true;
            if(playing){
                youTubePlayer.pause();
                playing=false;
                youTubePlayerView.getPlayerUIController().showUI(true);
                //textView.setVisibility(View.VISIBLE);

                iconView.setImageDrawable(getDrawable(R.drawable.ic_pause_white_24dp));
                iconView.setVisibility(View.VISIBLE);

            }
            else{
                youTubePlayer.play();
                playing = true;
                //textView.setVisibility(View.INVISIBLE);

                iconView.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_white_24dp));
                iconView.setVisibility(View.VISIBLE);
            }

            hideIconView(defaultHideTime);

        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_PLAY){
            Log.d(TAG,"Play button pressed");
            handled=true;
            if(!playing){
                youTubePlayer.play();
                playing = true;
                //textView.setVisibility(View.INVISIBLE);

                iconView.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_white_24dp));
                iconView.setVisibility(View.VISIBLE);
                hideIconView(defaultHideTime);
            }

        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_PAUSE){
            Log.d(TAG,"Pause button pressed");
            handled=true;
            if(playing){
                youTubePlayer.pause();
                playing = false;
                //youTubePlayerView.getPlayerUIController().showSeekBar(true);
                //textView.setVisibility(View.VISIBLE);

                iconView.setImageDrawable(getDrawable(R.drawable.ic_pause_white_24dp));
                iconView.setVisibility(View.VISIBLE);
                hideIconView(defaultHideTime);
            }

        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_REWIND){
            Log.d(TAG,"Rewind button pressed");
            handled=true;
            Toast.makeText(this, "REWIND button feature available on VOD.", Toast.LENGTH_SHORT)
                    .show();

            iconView.setImageDrawable(getDrawable(R.drawable.ic_fast_rewind_white_24dp));
            iconView.setVisibility(View.VISIBLE);
            hideIconView(defaultHideTime);
        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_FAST_FORWARD){
            Log.d(TAG,"Fast Forward button pressed");
            handled=true;
            Toast.makeText(this, "FAST FORWARD button feature available on VOD.", Toast.LENGTH_SHORT)
                    .show();


            iconView.setImageDrawable(getDrawable(R.drawable.ic_fast_forward_white_24dp));
            iconView.setVisibility(View.VISIBLE);
            hideIconView(defaultHideTime);
        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_PREVIOUS){
            Log.d(TAG,"Rewind button pressed");
            handled=true;
            Toast.makeText(this, "PREVIOUS button feature available on TV Shows VOD.", Toast.LENGTH_SHORT)
                    .show();
            iconView.setImageDrawable(getDrawable(R.drawable.ic_skip_previous_white_24dp));
            iconView.setVisibility(View.VISIBLE);
            hideIconView(defaultHideTime);
        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_NEXT){
            Log.d(TAG,"Fast Forward button pressed");
            handled=true;
            Toast.makeText(this, "NEXT button feature available on TV Shows VOD.", Toast.LENGTH_SHORT)
                    .show();

            iconView.setImageDrawable(getDrawable(R.drawable.ic_skip_next_white_24dp));
            iconView.setVisibility(View.VISIBLE);
            hideIconView(defaultHideTime);
        }

        //implement RGYB shortcut from YT player
        else if(KeyCode==KeyEvent.KEYCODE_PROG_RED||KeyCode==KeyEvent.KEYCODE_PROG_GREEN
                ||KeyCode==KeyEvent.KEYCODE_PROG_YELLOW||KeyCode==KeyEvent.KEYCODE_PROG_BLUE){
            handled = true;
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Intent.EXTRA_KEY_EVENT,event);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }


        if(handled){


            return handled;
        }

        else{

            return super.onKeyDown(KeyCode, event);
        }
        //return handled;
    }


    /**
     * Initialize the {@link YouTubePlayerView} for playing the live YouTube video
     */
    private void initYouTubePlayerView() {

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.getPlayerUIController().showFullscreenButton(false);
        youTubePlayerView.getPlayerUIController().enableLiveVideoUI(true);

        liveVideoId = getIntent().getExtras().getString("videoId");
        Log.d(TAG,"current videoID: "+ liveVideoId);


        //to display icon
        iconView = findViewById(R.id.iconView2);
        iconView.setVisibility(View.GONE);

        initVideoTitle();

        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.initialize(youTubePlayer -> {

            this.passYoutubePlayer((YouTubePlayer) youTubePlayer);
            youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    youTubePlayer.loadVideo(liveVideoId,0f);

                }


                @Override
                public void onStateChange(@NonNull PlayerConstants.PlayerState state) {
                    switch(state){
                        case PAUSED:
                            textView.setVisibility(View.VISIBLE);
                        case PLAYING:
                            textView.postDelayed(new Runnable() {
                                public void run() {
                                    textView.setVisibility(View.INVISIBLE);
                                }
                            }, 6000);
                    }
                    Log.d(TAG,"current player state- " + state);
                    super.onStateChange(state);
                }
            });

        }, true);
    }

    private void passYoutubePlayer(YouTubePlayer youTubePlayer) {
        this.youTubePlayer = youTubePlayer;
    }

    /**
     * To initialize the channel's title
     */
    public void initVideoTitle(){
        videoTitle = mockDatabase.searchCard(liveVideoId).getTitle();
        if(videoTitle!=null){
            String channelNum = "10"+ Integer.toString(channelArrayList.indexOf(liveVideoId)+1);
            String displayText = channelNum + ": " + videoTitle;
            textView = findViewById(R.id.textView1);
            textView.setVisibility(View.VISIBLE);
            textView.setText(displayText);


        }


    }

    /**
     * To hide icon
     * @param time time delay before icon is hidden
     */
    public void hideIconView(int time){
        iconView.postDelayed(new Runnable() {
            public void run() {
                iconView.setVisibility(View.GONE);
            }
        }, time);
    }


    /**
     * To initialize channel list ArrayList
     */
    private void initChannelList(){

        channelArrayList.add("wp-R57ZaiXY");

        //custom live channels for specific provider
        if(provider.equals("hotstar")){
            channelArrayList.add("bdae9OSA-bA");
        }
        else if(provider.equals("fptplay")){
            channelArrayList.add("ryN5slhHZkc");
        }

        channelArrayList.add("uopc-rWT1nw");
        channelArrayList.add("GtoIMPrppUY");
        channelArrayList.add("u7aE36v9xg8");
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

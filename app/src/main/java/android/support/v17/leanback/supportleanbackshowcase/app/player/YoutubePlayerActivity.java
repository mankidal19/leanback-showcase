package android.support.v17.leanback.supportleanbackshowcase.app.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.models.Card;
import android.support.v17.leanback.supportleanbackshowcase.models.DetailedCard;
import android.support.v17.leanback.supportleanbackshowcase.utils.MatchingCardUtils;
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
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.YouTubePlayerTracker;

import java.util.ArrayList;

/**
 * Activity to play non-live YouTube video
 */
public class YoutubePlayerActivity extends FragmentActivity {

    private static String TAG = "YoutubePlayerActivity";

    private String youtubeVideoId = null;
    private YouTubePlayerView youTubePlayerView = null;
    private boolean playing = true;
    private YouTubePlayer youTubePlayer = null;
    YouTubePlayerTracker tracker = new YouTubePlayerTracker();

    //for skipping to next/prev episodes
    private ArrayList<Card> episodes = new ArrayList<Card>();

    private DetailedCard showDetailedCard = null;
    private String youtubeVideoTitle = null;
    private static String prevVideoTitle = null;

    private ArrayList<String> episodeArrayList = new ArrayList<String>();
    private ArrayList<String> titleArrayList = new ArrayList<String>();

    //for finding matching detailed card
    private MatchingCardUtils matchingCardUtils = null;


    //for displaying current video title
    private TextView textView = null;

    //for displaying icon when user presses button
    //private TextView textMessageView = null;
    private ImageView iconView = null;
    private int defaultHideTime = 1000;


    private String playlistId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        matchingCardUtils = new MatchingCardUtils(this);

        initEpisodes();

    }

    @Override
    protected void onPause(){

        prevVideoTitle = youtubeVideoTitle;
        super.onPause();
    }

    private void initEpisodes(){

        Bundle extra = getIntent().getBundleExtra("extra");

        showDetailedCard = (DetailedCard) extra.getSerializable("data");
        youtubeVideoId = getIntent().getExtras().getString("videoId");
        youtubeVideoTitle = getIntent().getExtras().getString("videoTitle");

        if(!showDetailedCard.getText().toLowerCase().contains("movie")){
        //if(showDetailedCard==null){
            Log.d(TAG,"NON-MOVIE VOD");



            episodes.addAll((ArrayList<Card>) extra.getSerializable("recommended"));

            for(Card card:episodes){
                episodeArrayList.add(card.getVideoId());
                titleArrayList.add(card.getTitle());
            }
            Log.d(TAG,"title length,episode length: "+ titleArrayList.size()+", "+episodeArrayList.size());

        }

        else{
            Log.d(TAG,"MOVIE VOD");

        }



            initYouTubePlayerView();



        if(prevVideoTitle==null){
            prevVideoTitle = youtubeVideoTitle;
        }



        Log.i(TAG,"Video title: "+ youtubeVideoTitle);




   }

    /**
     * Initialize the {@link YouTubePlayerView} for playing the non-live YouTube video
     */
    private void initYouTubePlayerView() {
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.getPlayerUIController().showFullscreenButton(false);
        youTubePlayerView.getPlayerUIController().showUI(false);
        youTubePlayerView.getPlayerUIController().enableLiveVideoUI(false);

        Log.i(TAG,"current videoID: "+ youtubeVideoId);


        //to display icon
        iconView = findViewById(R.id.iconView);
        iconView.setVisibility(View.GONE);


        //to display video title
        if(youtubeVideoTitle!=null){
            //to display pop-up when changing episode

            textView = findViewById(R.id.textView2);
            textView.setText(youtubeVideoTitle);
            textView.setVisibility(View.VISIBLE);

            //hide after 5 seconds
           /* textView.postDelayed(new Runnable() {
                public void run() {
                    textView.setVisibility(View.GONE);
                }
            }, 5000);*/
        }

        getLifecycle().addObserver(youTubePlayerView);


        youTubePlayerView.initialize(youTubePlayer -> {

            this.passYoutubePlayer((YouTubePlayer) youTubePlayer);
            youTubePlayer.addListener(tracker);
            youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    youTubePlayer.loadVideo(youtubeVideoId,0f);
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
     * Overrides onKeyDown method to handle several remote keys such as
     * KEYCODE_CHANNEL_DOWN, KEYCODE_CHANNEL_UP,KEYCODE_ESCAPE and more
     * to behave uniquely in VOD video player activity only.
     * @param KeyCode KeyCode of the key pressed
     * @param event KeyEvent of the key pressed
     * @return boolean value either key is handled or not
     */
    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event){

        boolean handled = false;
        //KeyCode = event.getKeyCode();
        Intent intent = null;

        Log.i("KeyEvent",KeyCode + " button pressed");

        if(KeyCode == KeyEvent.KEYCODE_ESCAPE){
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
                textView.setVisibility(View.VISIBLE);

                iconView.setImageDrawable(getDrawable(R.drawable.ic_pause_white_24dp));
                iconView.setVisibility(View.VISIBLE);
            }
            else{
                youTubePlayer.play();
                playing = true;
                //textView.setVisibility(View.GONE);

                iconView.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_white_24dp));
                iconView.setVisibility(View.VISIBLE);
            }

            hideIconView(defaultHideTime);

        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_PLAY){
            Log.i("KeyEvent","Play button pressed");
            handled=true;
            if(!playing){
                youTubePlayer.play();
                playing = true;
                //textView.setVisibility(View.GONE);

                iconView.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_white_24dp));
                iconView.setVisibility(View.VISIBLE);
                hideIconView(defaultHideTime);
            }


        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_PAUSE){
            Log.i("KeyEvent","Pause button pressed");
            handled=true;
            if(playing){
                youTubePlayer.pause();
                playing = false;
                textView.setVisibility(View.VISIBLE);
                //youTubePlayerView.getPlayerUIController().showSeekBar(true);

                iconView.setImageDrawable(getDrawable(R.drawable.ic_pause_white_24dp));
                iconView.setVisibility(View.VISIBLE);
                hideIconView(defaultHideTime);

            }

        }
        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_REWIND){
            Log.i("KeyEvent","Rewind button pressed");
            handled=true;
            youTubePlayer.seekTo(tracker.getCurrentSecond()-10);

            iconView.setImageDrawable(getDrawable(R.drawable.ic_fast_rewind_white_24dp));
            iconView.setVisibility(View.VISIBLE);
            hideIconView(defaultHideTime);
        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_FAST_FORWARD){
            Log.i("KeyEvent","Fast Forward button pressed");
            handled=true;
            youTubePlayer.seekTo(tracker.getCurrentSecond()+10);

            iconView.setImageDrawable(getDrawable(R.drawable.ic_fast_forward_white_24dp));
            iconView.setVisibility(View.VISIBLE);
            hideIconView(defaultHideTime);
        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_NEXT){
            Log.i("KeyEvent","Next button pressed");
            handled=true;

            iconView.setImageDrawable(getDrawable(R.drawable.ic_skip_next_white_24dp));
            iconView.setVisibility(View.VISIBLE);


            String newVideoId = getNextEpisode();

            if(newVideoId!=null){
                youtubeVideoTitle = titleArrayList.get(episodeArrayList.indexOf(newVideoId));
                youtubeVideoId = newVideoId;
                initVideoTitle();
                youTubePlayer.loadVideo(youtubeVideoId,0f);
            }
            hideIconView(defaultHideTime);

        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_PREVIOUS){
            Log.i("KeyEvent","Previous button pressed");
            handled=true;

            iconView.setImageDrawable(getDrawable(R.drawable.ic_skip_previous_white_24dp));
            iconView.setVisibility(View.VISIBLE);

            String newVideoId = getPrevEpisode();


            if(newVideoId!=null){

                youtubeVideoTitle = titleArrayList.get(episodeArrayList.indexOf(newVideoId));
                youtubeVideoId = newVideoId;
                initVideoTitle();
                youTubePlayer.loadVideo(youtubeVideoId,0f);
            }
            hideIconView(3000);
        }

        else if(KeyCode==KeyEvent.KEYCODE_CHANNEL_DOWN){
            Log.i("KeyEvent","CH- button pressed");
            handled=true;
            Toast.makeText(this, "CH- button feature available on Live TV.", Toast.LENGTH_SHORT)
                    .show();

            iconView.setImageDrawable(getDrawable(R.drawable.ic_ch_minus));
            iconView.setVisibility(View.VISIBLE);
            hideIconView(defaultHideTime);
        }

        else if(KeyCode==KeyEvent.KEYCODE_CHANNEL_UP){
            Log.i("KeyEvent","CH+ button pressed");
            handled=true;
            Toast.makeText(this, "CH+ button feature available on Live TV.", Toast.LENGTH_SHORT)
                    .show();

            iconView.setImageDrawable(getDrawable(R.drawable.ic_ch_plus));
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
     * To initialize the video's title
     */
    public void initVideoTitle(){

        if(youtubeVideoTitle!=null){

            textView = findViewById(R.id.textView2);
            textView.setVisibility(View.VISIBLE);
            textView.setText(youtubeVideoTitle);

        }
    }


    private String getPrevEpisode() {

        int idx = episodeArrayList.indexOf(youtubeVideoId);


        if(episodeArrayList.isEmpty()){
            Toast.makeText(this, "PREVIOUS button feature available on TV Shows VOD.", Toast.LENGTH_SHORT)
                    .show();
            return null;
        }

        else if (idx!=0){
            idx--;

            return  episodeArrayList.get(idx);
        }

        else{

            Toast.makeText(this, "Previous episode unavailable.", Toast.LENGTH_SHORT)
                    .show();
            return  null;
        }

    }

    private String getNextEpisode() {
        int idx = episodeArrayList.indexOf(youtubeVideoId);

        //if idx is not the last element
        if (idx!=episodeArrayList.size()-1){
            idx++;

            return episodeArrayList.get(idx);
        }

        else if(episodeArrayList.isEmpty()){
            Toast.makeText(this, "NEXT button feature available on TV Shows VOD.", Toast.LENGTH_SHORT)
                    .show();
            return null;
        }

        else{
            Toast.makeText(this, "Next episode unavailable.", Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
    }



}

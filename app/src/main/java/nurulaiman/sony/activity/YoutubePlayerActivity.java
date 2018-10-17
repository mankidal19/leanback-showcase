package nurulaiman.sony.activity;

import android.content.Intent;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.models.Card;
import android.support.v17.leanback.supportleanbackshowcase.models.DetailedCard;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.YouTubePlayerTracker;

import java.util.ArrayList;

import nurulaiman.sony.utils.MatchingCardUtils;

public class YoutubePlayerActivity extends FragmentActivity {

    private String youtubeVideoId = null;
    private YouTubePlayerView youTubePlayerView = null;
    private boolean playing = true;
    private YouTubePlayer youTubePlayer = null;
    YouTubePlayerTracker tracker = new YouTubePlayerTracker();

    //for skipping to next/prev episodes
    private Card[] episodes = null;
    private DetailedCard showDetailedCard = null;
    private String youtubeVideoTitle = null;
    private static String prevVideoTitle = null;
    private ArrayList<String> episodeArrayList = new ArrayList<String>();
    private ArrayList<String> titleArrayList = new ArrayList<String>();

    //for finding matching detailed card
    private MatchingCardUtils matchingCardUtils = null;


    //for displaying current video title
    private TextView textView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        matchingCardUtils = new MatchingCardUtils(this);

        initEpisodes();

        initYouTubePlayerView();
    }

    @Override
    protected void onPause(){

        prevVideoTitle = youtubeVideoTitle;
        super.onPause();
    }

    private void initEpisodes(){


        youtubeVideoId = getIntent().getExtras().getString("videoId");



        youtubeVideoTitle = getIntent().getExtras().getString("videoTitle");

        if(prevVideoTitle==null){
            prevVideoTitle = youtubeVideoTitle;
        }


        Log.i("YoutubePlayerActivity","Video title: "+ youtubeVideoTitle);



            showDetailedCard = matchingCardUtils.findMatchingCard(youtubeVideoTitle);
            if(!showDetailedCard.getText().toLowerCase().contains("movie")){
                episodes = showDetailedCard.getRecommended();


                for(Card card:episodes){
                    episodeArrayList.add(card.getVideoId());
                    titleArrayList.add(card.getTitle());
                }

                if(!episodeArrayList.contains(showDetailedCard.getVideoId())){
                    episodeArrayList.add(0,showDetailedCard.getVideoId());
                    titleArrayList.add(0,showDetailedCard.getTitle());
                }

                //for debugging
                for(String ep:episodeArrayList){
                    Log.i("YoutubePlayerActivity","Episode video id: "+ ep);

                }

            }






    }

    private void initYouTubePlayerView() {
        //YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.getPlayerUIController().showFullscreenButton(false);
        //youTubePlayerView.getPlayerUIController().showUI(true);
        youTubePlayerView.getPlayerUIController().enableLiveVideoUI(false);


        //youtubeVideoId = getIntent().getExtras().getString("videoId");
        Log.i("YoutubePlayerActivity","current videoID: "+ youtubeVideoId);


        //to display video title
        if(youtubeVideoTitle!=null){
            //youTubePlayerView.getPlayerUIController().setVideoTitle(videoTitle);

            //to display pop-up when changing channel
            //

            textView = findViewById(R.id.textView2);
            textView.setText(youtubeVideoTitle);

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
            youTubePlayer.addListener(tracker);
            youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    youTubePlayer.loadVideo(youtubeVideoId,0f);
                 }


            });


        }, true);
    }

    private void passYoutubePlayer(YouTubePlayer youTubePlayer) {
        this.youTubePlayer = youTubePlayer;
    }

    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event){

        boolean handled = false;
        //KeyCode = event.getKeyCode();
        Intent intent = null;

        Log.i("KeyEvent",KeyCode + " button pressed");


        if(KeyCode == KeyEvent.KEYCODE_BACK){
            Log.i("KeyEvent","Return button pressed");
            handled=true;
            intent = new Intent(this, MainActivity.class);
            onDestroy();
            startActivity(intent);

        }

        else if(KeyCode == KeyEvent.KEYCODE_ESCAPE){
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
            }
            else{
                youTubePlayer.play();
                playing = true;
                textView.setVisibility(View.GONE);

            }

        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_PLAY){
            Log.i("KeyEvent","Play button pressed");
            handled=true;
            if(!playing){
                youTubePlayer.play();
                playing = true;
                textView.setVisibility(View.GONE);
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

            }

        }
        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_REWIND){
            Log.i("KeyEvent","Rewind button pressed");
            handled=true;
            youTubePlayer.seekTo(tracker.getCurrentSecond()-10);
        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_FAST_FORWARD){
            Log.i("KeyEvent","Fast Forward button pressed");
            handled=true;
            youTubePlayer.seekTo(tracker.getCurrentSecond()+10);
        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_NEXT){
            Log.i("KeyEvent","Next button pressed");
            handled=true;



            String newVideoId = getNextEpisode();

            if(newVideoId!=null){
                onDestroy();
                intent = new Intent(this, YoutubePlayerActivity.class);

                intent.putExtra("videoId",newVideoId);
                intent.putExtra("videoTitle",titleArrayList.get(episodeArrayList.indexOf(newVideoId)));
                startActivity(intent);
            }

        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_PREVIOUS){
            Log.i("KeyEvent","Previous button pressed");
            handled=true;

            String newVideoId = getPrevEpisode();

            if(newVideoId!=null){
                onDestroy();
                intent = new Intent(this, YoutubePlayerActivity.class);

                intent.putExtra("videoId",newVideoId);
                intent.putExtra("videoTitle",titleArrayList.get(episodeArrayList.indexOf(newVideoId)));
                startActivity(intent);
            }
        }

        else if(KeyCode==KeyEvent.KEYCODE_CHANNEL_DOWN){
            Log.i("KeyEvent","CH- button pressed");
            handled=true;
            Toast.makeText(this, "Features unavailable.", Toast.LENGTH_LONG)
                    .show();
        }

        else if(KeyCode==KeyEvent.KEYCODE_CHANNEL_UP){
            Log.i("KeyEvent","CH+ button pressed");
            handled=true;
            Toast.makeText(this, "Features unavailable.", Toast.LENGTH_LONG)
                    .show();
        }



        if(handled){
            //onDestroy();
            //startActivity(intent);

            return handled;
        }

        else{
            return super.onKeyDown(KeyCode, event);
        }
        //return handled;
    }

    private String getPrevEpisode() {
        int idx = episodeArrayList.indexOf(youtubeVideoId);


        if(episodeArrayList.isEmpty()){
            Toast.makeText(this, "Features unavailable.", Toast.LENGTH_LONG)
                    .show();
            return null;
        }

        else if (idx!=0){
            idx--;

            return  episodeArrayList.get(idx);
        }

        else{

            Toast.makeText(this, "Previous episode unavailable.", Toast.LENGTH_LONG)
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
            Toast.makeText(this, "Features unavailable.", Toast.LENGTH_LONG)
                    .show();
            return null;
        }

        else{
            Toast.makeText(this, "Next episode unavailable.", Toast.LENGTH_LONG)
                    .show();
            return null;
        }
    }

}

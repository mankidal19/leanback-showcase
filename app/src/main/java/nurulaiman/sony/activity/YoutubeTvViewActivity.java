/*
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2016 Bertrand Martel
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package nurulaiman.sony.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.models.Card;
import android.support.v17.leanback.supportleanbackshowcase.models.DetailedCard;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.bmartel.youtubetv.YoutubeTvView;
import nurulaiman.sony.utils.MatchingCardUtils;

/**
 * Created by bertrandmartel on 04/11/16.
 * ANOTHER VERSION OF YOUTUBE PLAYER USING DIFFERENT API
 */
public class YoutubeTvViewActivity extends Activity {

    private YoutubeTvView mYoutubeView;
    private String youtubeVideoId = null;
    private boolean playing = true;
    private boolean ccOn = false;


    //for skipping to next/prev episodes
    private ArrayList<Card> episodes = null;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_tv_view);

        //mYoutubeView = (YoutubeTvView) findViewById(R.id.youtube_tv_video);

        matchingCardUtils = new MatchingCardUtils(this);

        initEpisodes();
        initYouTubePlayerView();
    }
    private void initYouTubePlayerView(){

        mYoutubeView = (YoutubeTvView) findViewById(R.id.youtube_tv_video);
        mYoutubeView.playVideo(youtubeVideoId);


        Log.d("YoutubeTvView","play video "+ youtubeVideoId);


        //to display icon
        iconView = findViewById(R.id.iconView);
        iconView.setVisibility(View.GONE);


        //to display video title
        if(youtubeVideoTitle!=null){

            textView = findViewById(R.id.textView2);
            textView.setText(youtubeVideoTitle);
            textView.setVisibility(View.VISIBLE);

            //hide after 5 seconds
            textView.postDelayed(new Runnable() {
                public void run() {
                    textView.setVisibility(View.GONE);
                }
            }, 5000);
        }
    }
    private void initEpisodes(){


        youtubeVideoId = getIntent().getExtras().getString("videoId");



        youtubeVideoTitle = getIntent().getExtras().getString("videoTitle");

        if(prevVideoTitle==null){
            prevVideoTitle = youtubeVideoTitle;
        }


        Log.i("YoutubeTvViewActivity","Video title: "+ youtubeVideoTitle);



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
            for(String ep:titleArrayList){
                Log.d("YoutubeTvViewActivity","Episode video title: "+ ep);

            }

        }






    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mYoutubeView.closePlayer();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

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
                mYoutubeView.pause();
                playing=false;

                textView.setVisibility(View.VISIBLE);

                iconView.setImageDrawable(getDrawable(R.drawable.ic_pause_white_24dp));
                iconView.setVisibility(View.VISIBLE);
            }
            else{
                mYoutubeView.start();
                playing = true;
                textView.setVisibility(View.GONE);

                iconView.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_white_24dp));
                iconView.setVisibility(View.VISIBLE);
            }

            hideIconView(defaultHideTime);

        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_PLAY){
            Log.i("KeyEvent","Play button pressed");
            handled=true;
            if(!playing){
                mYoutubeView.start();
                playing = true;
                textView.setVisibility(View.GONE);

                iconView.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_white_24dp));
                iconView.setVisibility(View.VISIBLE);
                hideIconView(defaultHideTime);
            }


        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_PAUSE){
            Log.i("KeyEvent","Pause button pressed");
            handled=true;
            if(playing){
                mYoutubeView.pause();
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
            //youTubePlayer.seekTo(tracker.getCurrentSecond()-10);
            mYoutubeView.moveBackward(10);
            iconView.setImageDrawable(getDrawable(R.drawable.ic_fast_rewind_white_24dp));
            iconView.setVisibility(View.VISIBLE);
            hideIconView(defaultHideTime);
        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_FAST_FORWARD){
            Log.i("KeyEvent","Fast Forward button pressed");
            handled=true;
            //youTubePlayer.seekTo(tracker.getCurrentSecond()+10);
            mYoutubeView.moveForward(10);
            iconView.setImageDrawable(getDrawable(R.drawable.ic_fast_forward_white_24dp));
            iconView.setVisibility(View.VISIBLE);
            hideIconView(defaultHideTime);
        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_NEXT){
            throw new RuntimeException("This is a crash");
            /*Log.i("KeyEvent","Next button pressed");
            handled=true;

            iconView.setImageDrawable(getDrawable(R.drawable.ic_skip_next_white_24dp));
            iconView.setVisibility(View.VISIBLE);


            youtubeVideoId = getNextEpisode();
            if(youtubeVideoId!=null){



                youtubeVideoTitle = titleArrayList.get(episodeArrayList.indexOf(youtubeVideoId));
                initVideoTitle();

                mYoutubeView.playVideo(youtubeVideoId);


            }
            hideIconView(defaultHideTime);*/
        }

        else if(KeyCode==KeyEvent.KEYCODE_MEDIA_PREVIOUS){
            throw new RuntimeException("This is a crash");

            /*Log.i("KeyEvent","Previous button pressed");
            handled=true;

            iconView.setImageDrawable(getDrawable(R.drawable.ic_skip_previous_white_24dp));
            iconView.setVisibility(View.VISIBLE);


            youtubeVideoId = getPrevEpisode();

            if(youtubeVideoId!=null){

                youtubeVideoTitle = titleArrayList.get(episodeArrayList.indexOf(youtubeVideoId));
                initVideoTitle();

                mYoutubeView.playVideo(youtubeVideoId);



            }
            hideIconView(3000);*/
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

        else if(KeyCode==KeyEvent.KEYCODE_CAPTIONS){
            Log.i("KeyEvent","CC button pressed");
            handled=true;

            Bundle args = new Bundle();
            if(!ccOn){
                args.putBoolean("closedCaptions",true);
                mYoutubeView.updateView(args);
                ccOn=true;
            }
            else{
                args.putBoolean("closedCaptions",false);
                mYoutubeView.updateView(args);
                ccOn=false;
            }


            iconView.setImageDrawable(getDrawable(R.drawable.ic_closed_caption_white_24dp));
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

    //hide icon
    public void hideIconView(int time){
        iconView.postDelayed(new Runnable() {
            public void run() {
                iconView.setVisibility(View.GONE);
            }
        }, time);
    }



    //to display next video title faster
    public void initVideoTitle(){

        if(youtubeVideoTitle!=null){

            textView = findViewById(R.id.textView2);
            textView.setText(youtubeVideoTitle);
            textView.setVisibility(View.VISIBLE);

            //hide after 5 seconds
            textView.postDelayed(new Runnable() {
                public void run() {
                    textView.setVisibility(View.GONE);
                }
            }, 5000);
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

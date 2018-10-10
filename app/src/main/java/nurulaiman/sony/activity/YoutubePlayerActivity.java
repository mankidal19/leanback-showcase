package nurulaiman.sony.activity;

import android.content.Intent;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.YouTubePlayerTracker;

public class YoutubePlayerActivity extends FragmentActivity {

    private String youtubeVideoId = null;
    private YouTubePlayerView youTubePlayerView = null;
    private boolean playing = true;
    private YouTubePlayer youTubePlayer = null;
    YouTubePlayerTracker tracker = new YouTubePlayerTracker();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);



        initYouTubePlayerView();
    }

    private void initYouTubePlayerView() {
        //YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.getPlayerUIController().showFullscreenButton(false);
        youTubePlayerView.getPlayerUIController().showUI(true);
        youTubePlayerView.getPlayerUIController().enableLiveVideoUI(false);


        youtubeVideoId = getIntent().getExtras().getString("videoId");
        Log.i("YoutubePlayerActivity","current videoID: "+ youtubeVideoId);

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
        Intent intent = new Intent(this, LiveActivity.class);

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

}

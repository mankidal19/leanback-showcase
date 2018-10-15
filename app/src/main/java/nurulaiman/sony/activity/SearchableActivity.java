/*
 * Copyright (c) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nurulaiman.sony.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v17.leanback.supportleanbackshowcase.models.Card;
import android.util.Log;
import android.widget.Toast;

import nurulaiman.sony.data.MockDatabase;

import static android.support.v4.content.IntentCompat.EXTRA_START_PLAYBACK;

/**
 * Handles the intent from a global search. <br>
 * The assistant determines if the content should begin immediately, and lets us know with the
 * boolean extra value, {@link android.support.v4.content.IntentCompat#EXTRA_START_PLAYBACK
 * EXTRA_START_PLAYBACK}.
 */
public class SearchableActivity extends Activity {

    protected static final String JARGON = null;
    private static final String TAG = "SearchableActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Search data " + getIntent().getData());

        if (getIntent() != null && getIntent().getData() != null) {
            Uri uri = getIntent().getData();
            int id = Integer.valueOf(uri.getLastPathSegment());
            MockDatabase mockDatabase = new MockDatabase(this);
            Card selectedCard = mockDatabase.findMovieWithId(id);

            boolean startPlayback = getIntent().getBooleanExtra(EXTRA_START_PLAYBACK, false);
            Log.d(TAG, "Should start playback? " + (startPlayback ? "yes" : "no"));
            Log.i(TAG,uri.toString());

            if (startPlayback) {

                //startActivity(LiveActivity.createIntent(this, id));
                Intent intent = new Intent(this, LiveActivity.class);
                intent.putExtra("mediaId",id);
                intent.putExtra("liveVideoId",selectedCard.getVideoId());
                startActivity(intent);

                Log.d(TAG,"startplayback");

            } else {
                /*Movie movie = MockDatabase.findMovieWithId(id);
                startActivity(VideoDetailsActivity.createIntent(this, movie));*/

                // intent = new Intent(this, LiveActivity.class);
                //startActivity(intent);
                //startActivity(LiveActivity.createIntent(this, id));


                play(selectedCard,id);


                Log.d(TAG,"notstartplayback");
            }
        }
        finish();
    }

    public void play(Card selectedCard,int id){
        if(selectedCard.isLive()){

            //for sample live tv details page
            if(selectedCard.getTitle().toLowerCase().contains("jazeera")){
                Intent intent = new Intent(this, DetailViewLiveBroadcastActivity.class);
                intent.putExtra("mediaId",id);
                intent.putExtra("videoId",selectedCard.getVideoId());

                //to set video title
                intent.putExtra("videoTitle",selectedCard.getTitle());

                startActivity(intent);
                Log.d(TAG,"open sample live tv details page");
            }
            //directly play video
            else{
                Intent intent = new Intent(this, LiveActivity.class);
                intent.putExtra("mediaId",id);
                intent.putExtra("videoId",selectedCard.getVideoId());

                //to set video title
                intent.putExtra("videoTitle",selectedCard.getTitle());

                startActivity(intent);
                Log.d(TAG,"play live video");
            }

        }

        else{
            //for sample movie details page
            if(selectedCard.getTitle().toLowerCase().contains("gone")||selectedCard.getTitle().toLowerCase().contains("korean")){
                Intent intent = new Intent(this, DetailViewMovieActivity.class);
                intent.putExtra("mediaId",id);
                intent.putExtra("videoId",selectedCard.getVideoId());
                intent.putExtra("videoTitle",selectedCard.getTitle());

                startActivity(intent);
                Log.d(TAG,"open movie details page");
            }
            //for dummy movie
            else if(selectedCard.getDescription().toLowerCase().contains("$3.99")){
                Toast.makeText(this, "this is a dummy movie", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,MainActivity.class));
            }
            /*//for sample tv show details page
            else if(selectedCard.getTitle().toLowerCase().contains("superman")){
                Intent intent = new Intent(this, DetailViewTvShowActivity.class);
                intent.putExtra("mediaId",id);
                intent.putExtra("videoId",selectedCard.getVideoId());
                startActivity(intent);
                Log.d(TAG,"open sample tv show details page");
            }
            //directly plays video
            else{
                Intent intent = new Intent(this, YoutubePlayerActivity.class);
                intent.putExtra("mediaId",id);
                intent.putExtra("videoId",selectedCard.getVideoId());
                startActivity(intent);
                Log.d(TAG,"play non-live youtube video");
            }*/
            else{
                Intent intent = new Intent(this, DetailViewTvShowActivity.class);
                intent.putExtra("mediaId",id);
                intent.putExtra("videoId",selectedCard.getVideoId());
                intent.putExtra("videoTitle",selectedCard.getTitle());

                startActivity(intent);
                Log.d(TAG,"open tv show details page");
            }


        }
    }
}

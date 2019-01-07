package android.support.v17.leanback.supportleanbackshowcase.app.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.supportleanbackshowcase.app.LeanbackActivity;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.models.Card;
import android.support.v17.leanback.supportleanbackshowcase.models.DetailedCard;
import android.util.Log;
import android.view.KeyEvent;

import android.support.v17.leanback.supportleanbackshowcase.app.media.LiveActivity;

import android.support.v17.leanback.supportleanbackshowcase.utils.MatchingCardUtils;


/**
 *Activity class to render the specific details view fragment based on type of content chosen.
 *Fragments could be either {@link DetailViewTvShowFragment} for VOD with episodes,
 *{@link DetailViewMovieFragment} for Movie VOD, or {@link DetailViewLiveBroadcastFragment} for live TV channel.
 */
public class DetailViewActivity extends LeanbackActivity {
    //for finding matching detailed card
    private MatchingCardUtils matchingCardUtils = null;
    private DetailedCard chosenCard = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        matchingCardUtils = new MatchingCardUtils(this);
        chosenCard = matchingCardUtils.findExactTitleMatchingCard(getIntent().getExtras().getString("videoTitle"));

        setContentView(R.layout.activity_detail_example);


        if (savedInstanceState == null) {


            MyDetailsFragment fragment;

            if(chosenCard.getType().equals(Card.Type.LIVE_TV)){
                fragment = new DetailViewLiveBroadcastFragment();
            }

            else if(chosenCard.getType().equals(Card.Type.MOVIE)){
                fragment = new DetailViewMovieFragment();
            }

            else{
                fragment = new DetailViewTvShowFragment();
            }

            getFragmentManager().beginTransaction()
                    .replace(R.id.details_fragment, fragment)
                    .commit();
        }
    }

    /**
     *Overrides onKeyDown method for handling KEYCODE_ESCAPE to exit the application
     * @param KeyCode KeyCode of the key pressed
     * @param event KeyEvent of the key pressed
     * @return boolean value either key is handled or not
     */
    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event){

        boolean handled = false;
        //KeyCode = event.getKeyCode();
        Intent intent = new Intent(this, LiveActivity.class);

        Log.i("KeyEvent",KeyCode + " button pressed");


        if(KeyCode == KeyEvent.KEYCODE_ESCAPE){
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
}

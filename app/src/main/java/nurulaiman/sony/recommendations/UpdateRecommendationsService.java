package nurulaiman.sony.recommendations;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.app.recommendation.ContentRecommendation;
import android.support.v17.leanback.supportleanbackshowcase.models.DetailedCard;
import android.support.v17.leanback.supportleanbackshowcase.utils.Utils;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import nurulaiman.sony.activity.DetailViewLiveBroadcastActivity;
import nurulaiman.sony.activity.DetailViewMovieActivity;
import nurulaiman.sony.activity.DetailViewTvShowActivity;

public class UpdateRecommendationsService extends IntentService {

    private static final String TAG = "UpdateRecommendationsService";
    private static final String CONTENT_TAG = "OperatorApp_";
    private static final int MAX_RECOMMENDATIONS = 6;
    private Intent intent = null;


    private NotificationManager mNotifManager;


    public UpdateRecommendationsService() {
        super("RecommendationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (mNotifManager == null) {
            mNotifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Resources res = getResources();
        int cardWidth = res.getDimensionPixelSize(R.dimen.card_width);
        int cardHeight = res.getDimensionPixelSize(R.dimen.card_height);

        int count = 0;

        ArrayList<String> description = new ArrayList<String>();
        description.add("New");
        description.add("Popular");
        description.add("Similar Show");


        String json = Utils.inputStreamToString(res.openRawResource(
                R.raw.all_detailed_card_row));
        DetailedCard[] shows = new Gson().fromJson(json, DetailedCard[].class);
        DetailedCard[] chosenShows = new DetailedCard[MAX_RECOMMENDATIONS];


        ContentRecommendation.Builder builder = new ContentRecommendation.Builder()
                .setBadgeIcon(R.drawable.ic_operator_app_recommendations)
                .setAutoDismiss(true);


        //choose random shows index
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=0; i<shows.length; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        Collections.shuffle(description);
        //get the random shows
        for (int i=0; i<MAX_RECOMMENDATIONS; i++) {
            chosenShows[i] = shows[list.get(i)];

            builder.setBackgroundImageUri(chosenShows[i].getLocalImageResource())
                    .setIdTag(CONTENT_TAG + Long.toString(chosenShows[i].hashCode()))
                    .setTitle(chosenShows[i].getTitle())
                    .setText(description.get(i%description.size()))//random desc chosen
                    .setContentImage(BitmapFactory.decodeResource(res,chosenShows[i].getLocalImageResourceId(getApplicationContext())))
                    .setContentIntentData(ContentRecommendation.INTENT_TYPE_ACTIVITY,buildPendingIntent(chosenShows[i]), 0, null)
                    .setColor(res.getColor(R.color.custom_info_selected))
                    .build();

            // Create an object holding all the information used to recommend the content.
            ContentRecommendation rec = builder.build();
            Notification notification = rec.getNotificationObject(getApplicationContext());

            mNotifManager.notify(chosenShows[i].hashCode(), notification);

        }



    }

    private Intent buildPendingIntent(DetailedCard show) {

        if(show.isLive()){

            intent = new Intent(getApplicationContext(), DetailViewLiveBroadcastActivity.class);
            //intent.putExtra("mediaId",id);
            intent.putExtra("videoId",show.getVideoId());

            //to set video title
            intent.putExtra("videoTitle",show.getTitle());

        }

        else if(show.getText().toLowerCase().contains("movie")){
            intent = new Intent(getApplicationContext(), DetailViewMovieActivity.class);
            // intent.putExtra("mediaId",id);
            intent.putExtra("videoId",show.getVideoId());
            intent.putExtra("videoTitle",show.getTitle());

        }

        else{
            intent = new Intent(getApplicationContext(), DetailViewTvShowActivity.class);
            //intent.putExtra("mediaId",id);
            intent.putExtra("videoId",show.getVideoId());
            intent.putExtra("videoTitle",show.getTitle());

        }


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //stackBuilder.addParentStack(DetailsActivity.class);
        stackBuilder.addNextIntent(intent);
        // Ensure a unique PendingIntents, otherwise all
        // recommendations end up with the same PendingIntent
        intent.setAction(Long.toString(show.hashCode()));

        return intent;
    }

}

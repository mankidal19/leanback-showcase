/*
 * Created by Nurul Aiman, as an Open Source Project
 * Documented on 04/01/2019
 * Other interesting source code can be found at https://bitbucket.org/mankidal19/
 *
 *
 * This class extends AbstractCardPresenter<CustomImageCardView>
 * and is the Presenter class for CustomImageCardView
 */
package android.support.v17.leanback.supportleanbackshowcase.cards.presenters;

import android.content.Context;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.cards.CustomImageCardView;
import android.support.v17.leanback.supportleanbackshowcase.models.Card;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;

import com.bumptech.glide.Glide;

public class CustomImageCardViewPresenter extends AbstractCardPresenter<CustomImageCardView> {

    private Card.Type mType;

    private String mVideoId;

    private static String TAG = "CustomImageCardViewPresenter";


    public CustomImageCardViewPresenter(Context context, int cardThemeResId) {
        super(new ContextThemeWrapper(context, cardThemeResId));
    }

    public CustomImageCardViewPresenter(Context context) {
        this(context, R.style.DefaultCardTheme);
    }

    public CustomImageCardViewPresenter(Context context, Card.Type type, String videoId) {
        this(context, R.style.DefaultCardTheme);
        mType = type;
        mVideoId = videoId;
        Log.i("video ID loaded:",videoId);
    }

    @Override
    protected CustomImageCardView onCreateView() {
        CustomImageCardView imageCardView = new CustomImageCardView(getContext());
        imageCardView.showInfo(false);

        //only show info for card in focus
        imageCardView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                //change thumbnail's look accordingly when focus changes
                imageCardView.showInfo(hasFocus);
                imageCardView.changeInfoAreaColor(hasFocus);

                Log.d(TAG,"current view: "+ view.toString() + " to " + hasFocus);

                //Log.d(TAG,"focus changed for "+imageCardView.toString() + " to " + hasFocus);

            }

        });

        return imageCardView;
    }




    @Override
    public void onBindViewHolder(Card card, final CustomImageCardView cardView) {
        cardView.setTag(card);
        cardView.setTitleText(card.getTitle());
        cardView.setContentText(card.getDescription());

        if(card.getImageUrl()!=null){
            Glide.with(cardView.getContext())
                    .asBitmap()
                    .load(card.getImageUrl())
                    .into(cardView.getMainImageView());
        }

        else if (card.getLocalImageResourceName() != null) {
            int resourceId = getContext().getResources()
                    .getIdentifier(card.getLocalImageResourceName(),
                            "drawable", getContext().getPackageName());
            Glide.with(getContext())
                    .asBitmap()
                    .load(resourceId)
                    .into(cardView.getMainImageView());
        }
    }

}

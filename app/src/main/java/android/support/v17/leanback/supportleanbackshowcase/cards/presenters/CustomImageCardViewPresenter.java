package android.support.v17.leanback.supportleanbackshowcase.cards.presenters;

import android.content.Context;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.cards.CustomImageCardView;
import android.support.v17.leanback.supportleanbackshowcase.models.Card;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import nurulaiman.sony.fragment.MySettingsFragment;

import static android.support.v17.leanback.widget.BaseCardView.CARD_REGION_VISIBLE_SELECTED;

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
        //imageCardView.setInfoVisibility(CARD_REGION_VISIBLE_SELECTED);
        imageCardView.showInfo(false);

        //only show info for card in focus
        imageCardView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {


                imageCardView.showInfo(hasFocus);
                imageCardView.changeInfoAreaColor(hasFocus);

                Log.d(TAG,"focus changed for "+imageCardView.toString() + " to " + hasFocus);
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

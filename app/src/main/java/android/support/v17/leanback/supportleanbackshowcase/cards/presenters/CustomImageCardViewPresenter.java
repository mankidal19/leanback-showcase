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

public class CustomImageCardViewPresenter extends AbstractCardPresenter<CustomImageCardView> {

    private Card.Type mType;

    private String mVideoId;

    private static String TAG = "CustomImageCardViewPresenter";

    private ArrayList<CustomImageCardView> cardViewArrayList = new ArrayList<>();


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
//        imageCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "Clicked on ImageCardView", Toast.LENGTH_SHORT).show();
//            }
//        });

        //only show info for card in focus
        imageCardView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if(hasFocus){
                    for(CustomImageCardView cardView:cardViewArrayList){
                        cardView.showInfo(false);
                    }

                    imageCardView.showInfo(true);
                }
                else{
                    imageCardView.showInfo(false);
                }

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

        cardViewArrayList.add(cardView);

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

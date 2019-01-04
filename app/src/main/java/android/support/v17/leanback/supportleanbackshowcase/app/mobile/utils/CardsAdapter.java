package android.support.v17.leanback.supportleanbackshowcase.app.mobile.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.models.Card;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
 
import java.util.List;

import android.support.v17.leanback.supportleanbackshowcase.chromecast.ChromecastActivity;
import android.support.v17.leanback.supportleanbackshowcase.utils.MatchingCardUtils;
import android.support.v17.leanback.supportleanbackshowcase.chromecast.utils.PlaybackUtils;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.MyViewHolder> {
 
    private Context mContext;
    private List<Card> cardList;
   // private CardsAdapterListener listener;
    private MatchingCardUtils matchingCardUtils = null;
 
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description;
        public ImageView thumbnail, cast;
        public CardView cardView;
 
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            cardView = view.findViewById(R.id.card_view);
            //cast = (ImageView) view.findViewById(R.id.overflow);

            title.setSelected(true);

        }
    }
 
 
    public CardsAdapter(Context mContext, List<Card> cardList) {
        this.mContext = mContext;
        this.cardList = cardList;
       // this.listener = listener;
        matchingCardUtils = new MatchingCardUtils(mContext);
    }
 
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_card, parent, false);
 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        //Album album = albumList.get(position);
        Card card = cardList.get(position);
        holder.title.setText(card.getTitle());
        holder.description.setText(card.getDescription());
 
        // loading album cover using Glide library
        Glide.with(mContext).load(card.getLocalImageResourceId(mContext)).into(holder.thumbnail);

        /*holder.cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPopupMenu(holder.overflow);
            }
        });*/
        holder.thumbnail.setClickable(true);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //listener.onCardSelected(position, holder.thumbnail);
                startCastActivity(position);
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //listener.onCardSelected(position, holder.thumbnail);
                startCastActivity(position);
            }
        });
    }
 
    /**
     * do something when clicking cast icon
     */
    private void showPopupMenu(View view) {
        // inflate menu
        /*PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();*/
    }

    private void startCastActivity(int position){
        //String videoId = card.getVideoId();
        //Intent intent = new Intent(mContext,ChromecastActivity.class);
        //intent.putExtra("videoId",videoId);

        String videoId = cardList.get(position).getVideoId();
        PlaybackUtils.setVideoId(videoId);

        Intent intent = new Intent(mContext,ChromecastActivity.class);


        mContext.startActivity(intent);
    }
 
    /**
     * Click listener for popup menu items
     */
    //class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
 
       /* public MyMenuItemClickListener() {
        }
 
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }*/
 
    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public interface CardsAdapterListener {
        void onAddToFavoriteSelected(int position);

        void onPlayNextSelected(int position);

        void onCardSelected(int position, ImageView thumbnail);
    }
}
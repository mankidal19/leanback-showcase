package nurulaiman.sony.utils;

import android.content.Context;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.models.Card;
import android.support.v17.leanback.supportleanbackshowcase.models.CardRow;
import android.support.v17.leanback.supportleanbackshowcase.models.DetailedCard;
import android.support.v17.leanback.supportleanbackshowcase.utils.Utils;
import android.util.Log;

import com.google.gson.Gson;

import nurulaiman.sony.models.DetailedCardRow;

public class MatchingCardUtils {

    private DetailedCard chosenDetailedCard = null;
    private Context context = null;

    public MatchingCardUtils(Context context){
        this.context = context;
    }


    public DetailedCard findMatchingCard(Card card){


        String json = Utils.inputStreamToString(context.getResources().openRawResource(
                R.raw.all_detailed_card_row));

        //DetailedCardRow detailedCardRow = new Gson().fromJson(json, DetailedCardRow.class);
        DetailedCard[] rows = new Gson().fromJson(json, DetailedCard[].class);
        for(DetailedCard detailedCard:rows){
            if(card.getTitle().toLowerCase().equals(detailedCard.getTitle().toLowerCase())
                    && card.getType()==detailedCard.getType()){
                chosenDetailedCard = detailedCard;
                return chosenDetailedCard;
            }
        }

        return chosenDetailedCard;

    }

    public DetailedCard findMatchingCardUsingVideoId(String videoId){

        String json = Utils.inputStreamToString(context.getResources().openRawResource(
                R.raw.all_detailed_card_row));

        DetailedCard[] rows = new Gson().fromJson(json, DetailedCard[].class);
        for(DetailedCard detailedCard:rows){
            if(videoId.equals(detailedCard.getVideoId())){
                chosenDetailedCard = detailedCard;
                return chosenDetailedCard;
            }
        }

        return chosenDetailedCard;
    }

    //for movies
    public DetailedCard findExactTitleMatchingCard(String videoTitle){


        String json = Utils.inputStreamToString(context.getResources().openRawResource(
                R.raw.all_detailed_card_row));

        Log.d("MatchingCardUtils","Finding for title: "+videoTitle.toLowerCase());

        //DetailedCardRow detailedCardRow = new Gson().fromJson(json, DetailedCardRow.class);
        DetailedCard[] rows = new Gson().fromJson(json, DetailedCard[].class);
        for(DetailedCard detailedCard:rows){
            Log.d("MatchingCardUtils","Searched title: "+detailedCard.getTitle().toLowerCase());

            if(videoTitle.toLowerCase().equals(detailedCard.getTitle().toLowerCase())){

                Log.d("MatchingCardUtils","MATCH FOUND");

                chosenDetailedCard = detailedCard;
                return chosenDetailedCard;
            }
            else{
                Log.d("MatchingCardUtils","FALSE");

            }
        }

        return chosenDetailedCard;

    }

    public DetailedCard findMatchingCard(String videoTitle){


        String json = Utils.inputStreamToString(context.getResources().openRawResource(
                R.raw.all_detailed_card_row));

        //DetailedCardRow detailedCardRow = new Gson().fromJson(json, DetailedCardRow.class);
        DetailedCard[] rows = new Gson().fromJson(json, DetailedCard[].class);
        Log.d("MatchingCardUtils","Finding for title: "+videoTitle.toLowerCase());

        for(DetailedCard detailedCard:rows){
            Log.d("MatchingCardUtils","Searched title: "+detailedCard.getTitle().toLowerCase());

            String[] title = videoTitle.toLowerCase().split("]");
            String[] cardTitle = detailedCard.getTitle().toLowerCase().split("] ");

            Log.d("MatchingCardUtils",title[title.length-1]+", "+cardTitle[cardTitle.length-1]);

            if(title[title.length-1].contains(cardTitle[cardTitle.length-1])){

                chosenDetailedCard = detailedCard;
                Log.d("MatchingCardUtils","Match found: "+videoTitle.toLowerCase()+", "+detailedCard.getTitle().toLowerCase());
                return chosenDetailedCard;
            }
        }

        return chosenDetailedCard;

    }

    public boolean matchTwoCards(String a,String b){
        if(a.toLowerCase().contains(b.toLowerCase()) || b.toLowerCase().contains(a.toLowerCase())
                ||a.regionMatches(true,10,b,10,10)
                ||a.regionMatches(true,12,b,12,10)){

            Log.d("MatchingCardUtils","Two title matched: "+a+", "+b);
            return true;
        }

        else{
            Log.d("MatchingCardUtils","Two title not matched: "+a+", "+b);

            return false;
        }
    }




}

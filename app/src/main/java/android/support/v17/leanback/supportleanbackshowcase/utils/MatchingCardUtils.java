/*
 * Created by Nurul Aiman, as an Open Source Project
 * Documented on 04/01/2019
 * Other interesting source code can be found at https://bitbucket.org/mankidal19/
 *
 *
 * This MatchingCardUtils acts as a utility class that provide multiple methods to
 * find matching detailed card
 */

package android.support.v17.leanback.supportleanbackshowcase.utils;

import android.content.Context;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.models.Card;
import android.support.v17.leanback.supportleanbackshowcase.models.DetailedCard;
import android.util.Log;

import com.google.gson.Gson;

public class MatchingCardUtils {

    private DetailedCard chosenDetailedCard = null;
    private Context context = null;

    public MatchingCardUtils(Context context){
        this.context = context;
    }

    //match a Card with its DetailedCard
    public DetailedCard findMatchingCard(Card card){


        String json = Utils.inputStreamToString(context.getResources().openRawResource(
                R.raw.all_detailed_card_row));

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

    //find the DetailedCard with title exactly matching the string passed
    public DetailedCard findExactTitleMatchingCard(String videoTitle){


        String json = Utils.inputStreamToString(context.getResources().openRawResource(
                R.raw.all_detailed_card_row));

        Log.d("MatchingCardUtils","Finding for title: "+videoTitle.toLowerCase());

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

    //find the DetailedCard with title almost matching the string passed
    public DetailedCard findMatchingCard(String videoTitle){


        String json = Utils.inputStreamToString(context.getResources().openRawResource(
                R.raw.all_detailed_card_row));

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





}

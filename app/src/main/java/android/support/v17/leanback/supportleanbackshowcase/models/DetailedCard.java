/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package android.support.v17.leanback.supportleanbackshowcase.models;

import android.content.Context;
import android.support.v17.leanback.supportleanbackshowcase.models.Card;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A more detailed data object than {@link Card}, to be used in details view
 * i.e., {@link android.support.v17.leanback.supportleanbackshowcase.app.details.DetailViewActivity}.
 */
public class DetailedCard implements Serializable {

    @SerializedName("title") private String mTitle = "";
    @SerializedName("description") private String mDescription = "";
    @SerializedName("text") private String mText = "";
    @SerializedName("localImageResource") private String mLocalImageResource = null;
    @SerializedName("price") private String mPrice = null;
    @SerializedName("characters") private Card[] mCharacters = null;
    @SerializedName("recommended") private ArrayList<Card> mRecommended = new ArrayList<Card>();
    @SerializedName("year") private int mYear = 0;
    @SerializedName("trailerUrl") private String mTrailerUrl = null;
    @SerializedName("videoUrl") private String mVideoUrl = null;
    @SerializedName("videoId") private String mVideoId = null;
    @SerializedName("playlistId") private String mPlaylistId;
    @SerializedName("type") private Card.Type mType = Card.Type.DEFAULT;

    @SerializedName("live") private boolean mLive;

    public String getPrice() {
        return mPrice;
    }

    public Card.Type getType(){return mType;}

    public int getYear() {
        return mYear;
    }

    public String getLocalImageResource() {
        return mLocalImageResource;
    }

    public String getText() {
        return mText;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getTrailerUrl() {
        return mTrailerUrl;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public Card[] getCharacters() {
        return mCharacters;
    }

    public ArrayList<Card> getRecommended() {
        return mRecommended;
    }

    public void addRecommended(Card card){
        mRecommended.add(card);
    }

    public int getLocalImageResourceId(Context context) {
        return context.getResources()
                      .getIdentifier(getLocalImageResource(), "drawable", context.getPackageName());
    }

    public String getVideoId(){return mVideoId;}

    public void setPlaylistId(String id){
        this.mPlaylistId=id;
    }

    public void setTitle(String title){
        this.mTitle=title;
    }

    public void setText(String text){this.mText=text;}

    public String getPlaylistId() {
        return mPlaylistId;
    }

    public void setIsLive(boolean set){
        this.mLive=set;
    }

    public boolean isLive() {
        return mLive;
    }

}

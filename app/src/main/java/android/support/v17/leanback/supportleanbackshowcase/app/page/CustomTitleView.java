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
 */

package android.support.v17.leanback.supportleanbackshowcase.app.page;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.widget.SearchOrbView;
import android.support.v17.leanback.widget.TitleViewAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nurulaiman.sony.fragment.MySettingsFragment;

/**
 * Custom title view to be used in {@link android.support.v17.leanback.app.BrowseFragment}.
 */
public class CustomTitleView extends RelativeLayout implements TitleViewAdapter.Provider {
    private final TextView mTitleView;
    private final View mAnalogClockView;
    private final SearchOrbView mSearchOrbView;
    private final TextView searchTextView;

    //to display day & date
    private final TextView mDayDate;

    //to display logo
    private final ImageView mBadge;

    private final TitleViewAdapter mTitleViewAdapter = new TitleViewAdapter() {
        @Override
        public View getSearchAffordanceView() {
            return mSearchOrbView;
        }

        @Override
        public void setTitle(CharSequence titleText) {
            CustomTitleView.this.setTitle(titleText);
        }

        @Override
        public void setBadgeDrawable(Drawable drawable) {
            CustomTitleView.this.setBadgeDrawable(drawable);
        }

        @Override
        public void setOnSearchClickedListener(OnClickListener listener) {
            mSearchOrbView.setOnClickListener(listener);
        }

        @Override
        public void updateComponentsVisibility(int flags) {
            /*if ((flags & BRANDING_VIEW_VISIBLE) == BRANDING_VIEW_VISIBLE) {
                updateBadgeVisibility(true);
            } else {
                mAnalogClockView.setVisibility(View.GONE);
                mTitleView.setVisibility(View.GONE);
            }*/

            int visibility = (flags & SEARCH_VIEW_VISIBLE) == SEARCH_VIEW_VISIBLE
                    ? View.VISIBLE : View.INVISIBLE;
            mSearchOrbView.setVisibility(visibility);

            //if developer mode, show search textview, else hide
            if(MySettingsFragment.getDefaults("pref_interface_key",getContext()).equals("developer")){
                searchTextView.setVisibility(visibility);
            }
            else{
                searchTextView.setVisibility(View.GONE);
            }

        }

        private void updateBadgeVisibility(boolean visible) {
            if (visible) {
                mAnalogClockView.setVisibility(View.VISIBLE);
                mTitleView.setVisibility(View.VISIBLE);
                mDayDate.setVisibility(View.VISIBLE);
            } else {
                mAnalogClockView.setVisibility(View.GONE);
                mTitleView.setVisibility(View.GONE);
                mDayDate.setVisibility(View.GONE);

            }
        }
    };

    public CustomTitleView(Context context) {
        this(context, null);
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View root  = LayoutInflater.from(context).inflate(R.layout.custom_titleview, this);
        mTitleView = (TextView) root.findViewById(R.id.title_tv);
        mAnalogClockView = root.findViewById(R.id.clock);
        mSearchOrbView = root.findViewById(R.id.search_orb2);
        searchTextView = root.findViewById(R.id.searchTextView);


        Drawable drawable = getResources().getDrawable(R.drawable.ic_search_in_app,null);
        mSearchOrbView.setOrbIcon(drawable);


        mDayDate = root.findViewById(R.id.day_date);
        mBadge = root.findViewById(R.id.title_badge2);

        setDayDate();
    }

    public void setTitle(CharSequence title) {
        if (title != null) {
            mTitleView.setText(title);
            mTitleView.setVisibility(View.VISIBLE);
            mAnalogClockView.setVisibility(View.VISIBLE);
            mDayDate.setVisibility(View.VISIBLE);
            mBadge.setVisibility(View.VISIBLE);
        }
    }


    public void setBadgeDrawable(Drawable drawable) {
        if (drawable != null) {
            mTitleView.setVisibility(View.GONE);
            mAnalogClockView.setVisibility(View.VISIBLE);
            mDayDate.setVisibility(View.VISIBLE);

            mBadge.setImageDrawable(drawable);
            mBadge.setVisibility(View.VISIBLE);

        }


    }

    public void setDayDate(){
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM");
        Date date = new Date();
        String dayDate = dateFormat.format(date);
        mDayDate.setText(dayDate);

    }

    @Override
    public TitleViewAdapter getTitleViewAdapter() {
        return mTitleViewAdapter;
    }
}

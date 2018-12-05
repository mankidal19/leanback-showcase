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

package android.support.v17.leanback.supportleanbackshowcase.app.details;

import android.support.v17.leanback.supportleanbackshowcase.models.CardRow;
import android.support.v17.leanback.supportleanbackshowcase.utils.CardListRow;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.util.Log;

import nurulaiman.sony.models.IconHeaderItem;
import nurulaiman.sony.ui.presenter.IconHeaderItemPresenter;

/**
 * This {@link PresenterSelector} will return a {@link ListRowPresenter} which has shadow support
 * enabled or not depending on {@link CardRow#useShadow()} for a given row.
 */
public class ShadowRowPresenterSelector extends PresenterSelector {

    private static final String TAG = "ShadowRowPresenter";

    //disable zoom
    private ListRowPresenter mShadowEnabledRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_NONE,true);
    private ListRowPresenter mShadowDisabledRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_NONE,true);
    //private IconHeaderItemPresenter mIconHeaderItemPresenter = new IconHeaderItemPresenter();

    public ShadowRowPresenterSelector() {
        mShadowEnabledRowPresenter.setNumRows(1);
        mShadowDisabledRowPresenter.setShadowEnabled(false);

       /* mShadowDisabledRowPresenter.setHeaderPresenter(new IconHeaderItemPresenter());
        mShadowEnabledRowPresenter.setHeaderPresenter(new IconHeaderItemPresenter());*/
    }

    @Override public Presenter getPresenter(Object item) {
        if (!(item instanceof CardListRow))
        {   return mShadowDisabledRowPresenter;
        }

        Log.d(TAG,"(outside) item instance of " + item.toString());

        CardListRow listRow = (CardListRow) item;
        CardRow row = listRow.getCardRow();
        if (row.useShadow()) return mShadowEnabledRowPresenter;
        return mShadowDisabledRowPresenter;
    }

    @Override
    public Presenter[] getPresenters() {
        return new Presenter [] {
                mShadowDisabledRowPresenter,
                mShadowEnabledRowPresenter
        };
    }

    public void setPresenter(ListRowPresenter[] presenters){

        if(presenters!=null){
            mShadowDisabledRowPresenter = presenters[0];
            mShadowEnabledRowPresenter = presenters[1];
        }

    }

    public void setRows(int n){
        mShadowEnabledRowPresenter.setNumRows(n);
    }
}

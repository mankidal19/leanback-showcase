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

package android.support.v17.leanback.supportleanbackshowcase.app.details;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.app.DetailsFragmentBackgroundController;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.cards.presenters.CardPresenterSelector;
import android.support.v17.leanback.supportleanbackshowcase.models.Card;
import android.support.v17.leanback.supportleanbackshowcase.models.DetailedCard;
import android.support.v17.leanback.supportleanbackshowcase.utils.Utils;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import android.support.v17.leanback.supportleanbackshowcase.app.media.LiveActivity;
import android.support.v17.leanback.supportleanbackshowcase.app.settings.MySettingsFragment;

import android.support.v17.leanback.supportleanbackshowcase.utils.AppUiUtils;
import android.support.v17.leanback.supportleanbackshowcase.utils.MatchingCardUtils;

/**
 * Displays a card with more details using a {@link DetailsFragment}.
 */
public class DetailViewLiveBroadcastFragment extends MyDetailsFragment implements OnItemViewClickedListener,
        OnItemViewSelectedListener {

    public static final String TRANSITION_NAME = "t_for_transition";
    public static final String EXTRA_CARD = "card";
    private static String TAG = "LiveBroadcastDetailsFragment";

    private static final long ACTION_WATCHNOW = 1;
    private static final long ACTION_RECORD = 2;
    private static final long ACTION_RELATED = 3;

    private Action mActionWatchNow;
    private Action mActionRecord;
    private Action mActionRelated;
    private ArrayObjectAdapter mRowsAdapter;
    private final DetailsFragmentBackgroundController mDetailsBackground =
            new DetailsFragmentBackgroundController(this);

    private DetailedCard data = null;
    //for finding matching detailed card
    private MatchingCardUtils matchingCardUtils = null;

    //custom bg color
    private String provider;
    private String interfaceMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchingCardUtils = new MatchingCardUtils(getContext());

        provider = MySettingsFragment.getDefaults("pref_providers_key",getContext());
        interfaceMode = MySettingsFragment.getDefaults("pref_interface_key",getContext());


        setupUi();
        setupEventListeners();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.setContentDescription("Detail View Live TV");
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupUi() {
        // Load the card we want to display from a JSON resource. This JSON data could come from
        // anywhere in a real world app, e.g. a server.

        data = matchingCardUtils.findExactTitleMatchingCard(getActivity().getIntent().getExtras().getString("videoTitle"));

        if(data==null){
            String json = Utils
                    .inputStreamToString(getResources().openRawResource(R.raw.detail_live_broadcast));
            data = new Gson().fromJson(json, DetailedCard.class);
        }


        // Setup fragment
        setTitle(getString(R.string.detail_view_live_broadcast));

        FullWidthDetailsOverviewRowPresenter rowPresenter = new FullWidthDetailsOverviewRowPresenter(
                new DetailsDescriptionPresenter(getActivity())) {

            @Override
            protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
                // Customize Actionbar and Content by using custom colors.
                RowPresenter.ViewHolder viewHolder = super.createRowViewHolder(parent);

                View actionsView = viewHolder.view.
                        findViewById(R.id.details_overview_actions_background);

                AppUiUtils appUiUtils = new AppUiUtils(getContext());
                appUiUtils.setDetailsPageBg(actionsView,viewHolder);


                return viewHolder;
            }
        };

        FullWidthDetailsOverviewSharedElementHelper mHelper = new FullWidthDetailsOverviewSharedElementHelper();
        mHelper.setSharedElementEnterTransition(getActivity(), TRANSITION_NAME);
        rowPresenter.setListener(mHelper);
        rowPresenter.setParticipatingEntranceTransition(false);
        prepareEntranceTransition();

        ListRowPresenter shadowDisabledRowPresenter = new ListRowPresenter();
        shadowDisabledRowPresenter.setShadowEnabled(false);

        // Setup PresenterSelector to distinguish between the different rows.
        ClassPresenterSelector rowPresenterSelector = new ClassPresenterSelector();
        rowPresenterSelector.addClassPresenter(DetailsOverviewRow.class, rowPresenter);
        //rowPresenterSelector.addClassPresenter(CardListRow.class, shadowDisabledRowPresenter);
        rowPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
        mRowsAdapter = new ArrayObjectAdapter(rowPresenterSelector);

        // Setup action and detail row.
        DetailsOverviewRow detailsOverview = new DetailsOverviewRow(data);
        int imageResId = data.getLocalImageResourceId(getActivity());

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null && extras.containsKey(EXTRA_CARD)) {
            imageResId = extras.getInt(EXTRA_CARD, imageResId);
        }
        detailsOverview.setImageDrawable(getResources().getDrawable(imageResId, null));
        ArrayObjectAdapter actionAdapter = new ArrayObjectAdapter();

       /* mActionBuy = new Action(ACTION_BUY, getString(R.string.action_buy) + data.getPrice());
        mActionWishList = new Action(ACTION_WISHLIST, getString(R.string.action_wishlist));*/
        mActionRelated = new Action(ACTION_RELATED, getString(R.string.action_recommeded));

       mActionWatchNow = new Action(ACTION_WATCHNOW,"WATCH NOW");
       mActionRecord = new Action(ACTION_RECORD,"RECORD");

        actionAdapter.add(mActionWatchNow);
        actionAdapter.add(mActionRecord);
        actionAdapter.add(mActionRelated);
        detailsOverview.setActionsAdapter(actionAdapter);
        mRowsAdapter.add(detailsOverview);

        // Setup related row.
  /*      ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(
                new CardPresenterSelector(getActivity()));

        for (Card characterCard : data.getCharacters()) listRowAdapter.add(characterCard);
        HeaderItem header = new HeaderItem(0, getString(R.string.header_related));
        mRowsAdapter.add(new CardListRow(header, listRowAdapter, null));*/

        // Setup recommended row.
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenterSelector(getActivity()));
        for (Card card : data.getRecommended()) listRowAdapter.add(card);
        HeaderItem header = new HeaderItem(0, "Related Broadcasts");
        mRowsAdapter.add(new ListRow(header, listRowAdapter));

        setAdapter(mRowsAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startEntranceTransition();
            }
        }, 500);
        initializeBackground(data);
    }

    private void initializeBackground(DetailedCard data) {
        mDetailsBackground.enableParallax();
        mDetailsBackground.setCoverBitmap(BitmapFactory.decodeResource(getResources(),
                data.getLocalImageResourceId(getContext())));
    }

    private void setupEventListeners() {
        setOnItemViewSelectedListener(this);
        setOnItemViewClickedListener(this);
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                              RowPresenter.ViewHolder rowViewHolder, Row row) {


        if ((item instanceof Action)) {
            Action action = (Action) item;

            if (action.getId() == ACTION_RELATED) {
                setSelectedPosition(1);
            }
            else if(action.getId()==ACTION_WATCHNOW){
                Intent intent = new Intent(getContext(), LiveActivity.class);
                String videoId = getActivity().getIntent().getExtras().getString("videoId");
                intent.putExtra("videoId",videoId);
                //startActivity(intent);
                getActivity().startActivityForResult(intent,101);
                Log.d(TAG,"play live youtube video from details page with");
            }

            else {
                Toast.makeText(getActivity(), getString(R.string.action_cicked), Toast.LENGTH_LONG)
                        .show();
            }
        }

        else if(item instanceof Card && ((Card)item).getType().equals(Card.Type.DEFAULT)){
            Intent intent = new Intent(getContext(), DetailViewActivity.class);
            Card selectedCard = (Card)item;



            intent.putExtra("videoId",selectedCard.getVideoId());
            intent.putExtra("videoTitle",selectedCard.getTitle());

            Log.d(TAG,"open another live tv details page");

            //startActivity(intent);
            getActivity().startActivityForResult(intent,101);



        }

    }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                               RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (mRowsAdapter.indexOf(row) > 0) {
            int backgroundColor = getResources().getColor(R.color.detail_view_related_background);
            getView().setBackgroundColor(backgroundColor);
        } else {
            getView().setBackground(null);
        }
    }
}

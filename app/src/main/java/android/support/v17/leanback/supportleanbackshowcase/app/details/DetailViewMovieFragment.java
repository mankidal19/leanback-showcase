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
import android.support.v17.leanback.supportleanbackshowcase.models.CardListRow;
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

import android.support.v17.leanback.supportleanbackshowcase.app.media.YoutubePlayerActivity;
import android.support.v17.leanback.supportleanbackshowcase.app.settings.MySettingsFragment;

import android.support.v17.leanback.supportleanbackshowcase.utils.AppUiUtils;
import android.support.v17.leanback.supportleanbackshowcase.utils.MatchingCardUtils;

/**
 * Displays a Movie card with more details using a {@link MyDetailsFragment}.
 */
public class DetailViewMovieFragment extends MyDetailsFragment implements OnItemViewClickedListener,
        OnItemViewSelectedListener {

    public static final String TRANSITION_NAME = "t_for_transition";
    public static final String EXTRA_CARD = "card";

    private static final long ACTION_BUY = 1;
    private static final long ACTION_WATCHLIST = 2;
    private static final long ACTION_SUB_AUDIO = 3;
    private static final long ACTION_RECOMMEDED = 4;
    private static final String TAG = "DetailViewMovieFragment";

    private Action mActionBuy;
    private Action mActionWatchList;
    private Action mActionSubAudio;
    private Action mActionRecommeded;

    //for purchased/free movies
    private Action mActionWatchNow;
    private static final long ACTION_WATCHNOW = 5;
    private DetailedCard data = null;



    private ArrayObjectAdapter mRowsAdapter;
    private final DetailsFragmentBackgroundController mDetailsBackground =
            new DetailsFragmentBackgroundController(this);

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

    private void setupUi() {
        // Load the card we want to display from a JSON resource. This JSON data could come from
        // anywhere in a real world app, e.g. a server.

        //data = matchingCardUtils.findMatchingCard(getActivity().getIntent().getExtras().getString("videoTitle"));
        data = matchingCardUtils.findExactTitleMatchingCard(getActivity().getIntent().getExtras().getString("videoTitle"));

        if(data==null){
            String json = Utils
                    .inputStreamToString(getResources().openRawResource(R.raw.detail_movie));
            data = new Gson().fromJson(json, DetailedCard.class);
        }


        // Setup fragment
        setTitle(getString(R.string.detail_view_movie));

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
        rowPresenterSelector.addClassPresenter(CardListRow.class, shadowDisabledRowPresenter);
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

        if(data.getPrice().equals("FREE")||data.getPrice().equals("PURCHASED")){
            mActionWatchNow = new Action(ACTION_WATCHNOW,"WATCH NOW");
            actionAdapter.add(mActionWatchNow);
        }

        else{
            mActionBuy = new Action(ACTION_BUY, getString(R.string.action_buy) + " " + data.getPrice());
            actionAdapter.add(mActionBuy);
        }

        mActionWatchList = new Action(ACTION_WATCHLIST, getString(R.string.action_favorite));
        mActionSubAudio = new Action(ACTION_SUB_AUDIO,getString(R.string.action_sub_audio));
        mActionRecommeded = new Action(ACTION_RECOMMEDED, getString(R.string.action_recommeded));


        actionAdapter.add(mActionWatchList);
        actionAdapter.add(mActionSubAudio);
        actionAdapter.add(mActionRecommeded);
        detailsOverview.setActionsAdapter(actionAdapter);
        mRowsAdapter.add(detailsOverview);

        // Setup related row.
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(
                new CardPresenterSelector(getActivity()));
        for (Card characterCard : data.getCharacters()) listRowAdapter.add(characterCard);
        HeaderItem header = new HeaderItem(0, getString(R.string.header_casts));
        mRowsAdapter.add(new CardListRow(header, listRowAdapter, null));

        // Setup recommended row.
        listRowAdapter = new ArrayObjectAdapter(new CardPresenterSelector(getActivity()));
        for (Card card : data.getRecommended()) listRowAdapter.add(card);
        header = new HeaderItem(1, getString(R.string.header_recommended));
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
        //if (!(item instanceof Action)) return;

        if(item instanceof Action){
            Action action = (Action) item;

            if (action.getId() == ACTION_RECOMMEDED) {
                setSelectedPosition(2);
            }
            //FOR WATCH NOW FREE/PURCHASED MOVIES
            else if(action.getId()==ACTION_WATCHNOW){
                Intent intent = new Intent(getContext(), YoutubePlayerActivity.class);

                String videoId = getActivity().getIntent().getExtras().getString("videoId");
                intent.putExtra("videoId",videoId);
                intent.putExtra("videoTitle",data.getTitle());

                Bundle extras = new Bundle();

                extras.putSerializable("data",data);
                intent.putExtra("extra",extras);

                //startActivity(intent);
                getActivity().startActivityForResult(intent,101);
                Log.d(TAG,"play non-live youtube video from details page");
            }

            else {
                Toast.makeText(getActivity(), getString(R.string.action_cicked), Toast.LENGTH_LONG)
                        .show();
            }
        }

        else if(item instanceof Card && ((Card)item).getType().equals(Card.Type.DEFAULT)
                ||((Card)item).getType().equals(Card.Type.MOVIE)){
            Intent intent = new Intent(getContext(), DetailViewActivity.class);
            Card selectedCard = (Card)item;
            Bundle extras = new Bundle();

            extras.putSerializable("data",data);
            intent.putExtra("extra",extras);


            intent.putExtra("videoId",selectedCard.getVideoId());
            intent.putExtra("videoTitle",selectedCard.getTitle());

            Log.d(TAG,"open another movie details page");

            //startActivity(intent);
            getActivity().startActivityForResult(intent,101);



        }

        else
            return;

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

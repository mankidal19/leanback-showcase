package android.support.v17.leanback.supportleanbackshowcase.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.app.RowsFragment;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.app.page.SettingsIconPresenter;
import android.support.v17.leanback.supportleanbackshowcase.cards.presenters.CardPresenterSelector;
import android.support.v17.leanback.supportleanbackshowcase.models.Card;
import android.support.v17.leanback.supportleanbackshowcase.models.CardRow;
import android.support.v17.leanback.supportleanbackshowcase.models.CardListRow;
import android.support.v17.leanback.supportleanbackshowcase.utils.Utils;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.PageRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;


import android.support.v17.leanback.supportleanbackshowcase.app.details.DetailViewActivity;
import android.support.v17.leanback.supportleanbackshowcase.app.settings.MySettingsActivity;
import android.support.v17.leanback.supportleanbackshowcase.app.room.controller.search.SearchActivity;
import android.support.v17.leanback.supportleanbackshowcase.app.settings.MySettingsFragment;
import android.support.v17.leanback.supportleanbackshowcase.models.IconHeaderItem;
import android.support.v17.leanback.supportleanbackshowcase.models.presenters.CustomShadowRowPresenterSelector;
import android.support.v17.leanback.supportleanbackshowcase.models.presenters.IconHeaderItemPresenter;
import android.support.v17.leanback.supportleanbackshowcase.utils.MatchingCardUtils;

public class MainFragment extends BrowseFragment {
    private static final long HEADER_ID_1 = 1;
    private static String HEADER_NAME_1 = "[User Custom] HOME";
    private static final long HEADER_ID_2 = 2;
    private static final String HEADER_NAME_2 = "LIVE TV CHANNELS";
    private static final long HEADER_ID_3 = 3;
    private static final String HEADER_NAME_3 = "NEWS & SPORTS";
    private static final long HEADER_ID_4 = 4;
    private static final String HEADER_NAME_4 = "TV SHOWS";
    private static final long HEADER_ID_5 = 5;
    private static final String HEADER_NAME_5 = "MOVIES";


    private static final long HEADER_ID_9 = 9;
    private static final String HEADER_NAME_9 = "Login/Sign Up";
    private static final long HEADER_ID_10 = 10;
    private static final String HEADER_NAME_10 = "Settings";

    private static String TAG = "MainFragment";

    private BackgroundManager mBackgroundManager;

    private ArrayObjectAdapter mRowsAdapter;

    private MatchingCardUtils matchingCardUtils = null;

    //for RGYB buttons function
    private PageRowFragmentFactory mPageRowFragmentFactory = null;


    //for changing logo
    private int logo;
    private final String PREFERENCE_DEFAULT = "default";
    private final String PREFERENCE_ALTBALAJI = "altbalaji";
    private final String PREFERENCE_BIGFLIX = "bigflix";
    private final String PREFERENCE_HOTSTAR = "hotstar";
    private final String PREFERENCE_SONYLIV = "sonyliv";
    private final String PREFERENCE_TVF = "tvf";
    private final String PREFERENCE_FPT = "fptplay";

    private String provider;
    private String interfaceMode;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        provider = MySettingsFragment.getDefaults("pref_providers_key",getContext());
        interfaceMode = MySettingsFragment.getDefaults("pref_interface_key",getContext());

        setupUi();
        loadData();

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());

        //set bg to semi transparent, if enduser mode
        if(interfaceMode.equals("enduser")){
            mBackgroundManager.setColor(getResources().getColor(R.color.semi_transparent_background));
        }


        //for RGYB buttons function
        mPageRowFragmentFactory = new PageRowFragmentFactory(mBackgroundManager);
        getMainFragmentRegistry().registerFragment(PageRow.class,
                mPageRowFragmentFactory);

        matchingCardUtils = new MatchingCardUtils(getContext());

        setBrowseTransitionListener(new BrowseTransitionListener(){
            @Override
            public void onHeadersTransitionStart(boolean withHeaders) {
                super.onHeadersTransitionStart(withHeaders);
                Log.d(TAG,"onHeadersTransitionStart, withHeaders: "+ withHeaders);
            }

            @Override
            public void onHeadersTransitionStop(boolean withHeaders) {
                super.onHeadersTransitionStop(withHeaders);
                Log.d(TAG,"onHeadersTransitionStop, withHeaders: "+ withHeaders);

            }
        });

    }


    public static MainFragment newInstance() {
        Log.d(TAG, "newInstance: ");
        MainFragment fragment = new MainFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }


    @Override
    public void onResume(){
        super.onResume();

    }

    //for RGYB shortcut buttons
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



// Register to receive messages.
// We are registering an observer (mMessageReceiver) to receive Intents
// with actions named "activity-says-hi".
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("activity-says-hi"));
    }

    /**
     * Our handler for received Intents. This will be called whenever an Intent
     * with an action named "custom-event-name" is broadcasted.
     */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            KeyEvent keyEvent = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            int keyCode = keyEvent.getKeyCode();

                // KeyEvent keyEvent = KeyEvent(KeyEvent.keyCodeToString(keyCode));
                Log.d(TAG,"KEYEVENT RECEIVED: " + keyEvent.toString());

                //need the if, to debug if opening app from Google Assistant
                if(getRowsFragment()!=null){
                    startHeadersTransition(false);
                    Log.d(TAG,"getRowsFragment not null");
                    //setHeadersState(HEADERS_HIDDEN);
                }
                else{
                    Log.d(TAG,"getRowsFragment null");

                }

                switch (keyCode) {


                    case KeyEvent.KEYCODE_PROG_RED:
                        //LIVE TV
                        Log.d(TAG,"Red button pressed");
                        setSelectedPosition(1,true);

                        break;

                    case KeyEvent.KEYCODE_PROG_GREEN:
                        //DUMMY DRAMA
                        Log.d(TAG,"Green button pressed");
                        setSelectedPosition(2,true);

                        break;

                    case KeyEvent.KEYCODE_PROG_YELLOW:
                        Log.d(TAG,"Yellow button pressed");
                        setSelectedPosition(3,true);

                        break;

                    case KeyEvent.KEYCODE_PROG_BLUE:
                        Log.d(TAG,"Blue button pressed");
                        setSelectedPosition(4,true);

                        break;
            }

        }
    };


    /**
     * Setup main fragment UI based on content provider and interface mode set.
     */
    private void setupUi() {
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        switch (provider){
            case PREFERENCE_DEFAULT:
                default:
                logo = R.drawable.operator_app_logo_small;
                break;
            case PREFERENCE_ALTBALAJI:
                logo = R.drawable.altbalaji;
                break;
            case PREFERENCE_BIGFLIX:
                logo = R.drawable.bigflix;
                break;
            case PREFERENCE_HOTSTAR:
                //logo = R.drawable.hotstar;
                logo = R.drawable.hotstar_trans;
                break;
            case PREFERENCE_SONYLIV:
                logo = R.drawable.sonyliv;
                break;
            case PREFERENCE_TVF:
                logo = R.drawable.tvf;
                break;
            case PREFERENCE_FPT:
                logo = R.drawable.fpt_logo;

        }

        //change header name & brand color based on interface mode
        if(interfaceMode.equals("developer")){
            HEADER_NAME_1 = "[User Custom] HOME";
            setBrandColor(getResources().getColor(R.color.fastlane_background));
        }
        else{
            HEADER_NAME_1 = "HOME";
            setBrandColor(getResources().getColor(R.color.semi_transparent_fastlane_background));

        }

        setBadgeDrawable(getResources().getDrawable(logo, null));


        //for displaying logo beside header
        setHeaderPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object o) {
                return new IconHeaderItemPresenter();
            }
        });

        setTitle("YouTube OpApp");
        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);

            }
        });

        prepareEntranceTransition();
    }

    private void loadData() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_NONE,true));
        setAdapter(mRowsAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                createRows();
                startEntranceTransition();
            }
        }, 2000);
    }

    /**
     * Setup main menu header title and link to their respective fragment.
     */
    private void createRows() {


        IconHeaderItem headerItem1 = new IconHeaderItem(HEADER_ID_1, HEADER_NAME_1,R.drawable.ic_home_black_24dp);
        PageRow pageRow1 = new PageRow(headerItem1);
        mRowsAdapter.add(pageRow1);

        IconHeaderItem headerItem2 = new IconHeaderItem(HEADER_ID_2, HEADER_NAME_2,R.drawable.live_red);
        PageRow pageRow2 = new PageRow(headerItem2);
        mRowsAdapter.add(pageRow2);

        IconHeaderItem headerItem3 = new IconHeaderItem(HEADER_ID_3, HEADER_NAME_3,R.drawable.vod_green);
        PageRow pageRow3 = new PageRow(headerItem3);
        mRowsAdapter.add(pageRow3);

        IconHeaderItem headerItem4 = new IconHeaderItem(HEADER_ID_4, HEADER_NAME_4,R.drawable.vod_yellow);
        PageRow pageRow4 = new PageRow(headerItem4);
        mRowsAdapter.add(pageRow4);

        IconHeaderItem headerItem5 = new IconHeaderItem(HEADER_ID_5, HEADER_NAME_5,R.drawable.vod_blue);
        PageRow pageRow5 = new PageRow(headerItem5);
        mRowsAdapter.add(pageRow5);



        IconHeaderItem headerItem9 = new IconHeaderItem(HEADER_ID_9, HEADER_NAME_9,R.drawable.ic_person_black_24dp);
        PageRow pageRow9 = new PageRow(headerItem9);
        mRowsAdapter.add(pageRow9);

        IconHeaderItem headerItem10 = new IconHeaderItem(HEADER_ID_10, HEADER_NAME_10,R.drawable.ic_settings_black_24dp);
        PageRow pageRow10 = new PageRow(headerItem10);
        mRowsAdapter.add(pageRow10);

    }

    private static class PageRowFragmentFactory extends BrowseFragment.FragmentFactory {
        private final BackgroundManager mBackgroundManager;

        PageRowFragmentFactory(BackgroundManager backgroundManager) {
            this.mBackgroundManager = backgroundManager;
        }

        @Override
        public Fragment createFragment(Object rowObj) {
            Row row = (Row)rowObj;

            mBackgroundManager.setDrawable(null);

            //debug
            if(row!=null){
                Log.d(TAG,"HeaderItem obtained- "+row+", "+row.getHeaderItem().getId());

            }

            else {
                Log.d(TAG,"HeaderItem NOT obtained ");
            }


            if(row.getHeaderItem().getId() == HEADER_ID_9){
                return new MainFragment.FragmentLoginSignUp();
            }
            else if(row.getHeaderItem().getId() == HEADER_ID_10){
                return new MainFragment.SettingsFragment();
            }
            else if(row.getHeaderItem().getId() == HEADER_ID_1){
                return new MainFragment.FragmentHome();
            }
            else if(row.getHeaderItem().getId() == HEADER_ID_2){
                return new MainFragment.FragmentLiveBroadcast();
            }
            else if(row.getHeaderItem().getId() == HEADER_ID_4){

                return new MainFragment.FragmentTvShow();
            }
            else if(row.getHeaderItem().getId() == HEADER_ID_5){
                return new MainFragment.FragmentMovie();
            }

            else if(row.getHeaderItem().getId() == HEADER_ID_3){

                return new MainFragment.FragmentNewsSports();
            }


            throw new IllegalArgumentException(String.format("Invalid row %s", rowObj));
        }
    }


    /**
     * Fragment for category Live TV Channels.
     */
    public static class FragmentLiveBroadcast extends RowsFragment {
        private final ArrayObjectAdapter mRowsAdapter;

        public FragmentLiveBroadcast() {
            CustomShadowRowPresenterSelector presenterSelector = new CustomShadowRowPresenterSelector();

            //set 2 rows
            presenterSelector.setRows(2);
            mRowsAdapter = new ArrayObjectAdapter(presenterSelector);


            setAdapter(mRowsAdapter);
            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                @Override
                public void onItemClicked(
                        Presenter.ViewHolder itemViewHolder,
                        Object item,
                        RowPresenter.ViewHolder rowViewHolder,
                        Row row) {
                    Card selectedCard = (Card)item;
                    Intent intent = new Intent(getContext(), DetailViewActivity.class);
                    intent.putExtra("videoId",selectedCard.getVideoId());

                    //to set video title
                    intent.putExtra("videoTitle",selectedCard.getTitle());
                        startActivity(intent);
                        Log.d(TAG,"open live tv details page");


                }
            });
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d(TAG,"Live broadcast: on create (before create rows)");

            createRows();

            Log.d(TAG,"Live broadcast: on create (after create rows)");

            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            //expand
            setExpand(true);



            return super.onCreateView(inflater,container,savedInstanceState);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            view.setContentDescription("Live TV Fragment");

            super.onViewCreated(view, savedInstanceState);

            Log.d(TAG,"Live broadcast: on view created");

        }

        @Override
       public void onTransitionEnd(){
           //expand
           setExpand(true);
           Log.d(TAG,"Live broadcast: transition ends");
           super.onTransitionEnd();
       }

        /**
         * Create rows based on cards data declared in {@link android.support.v17.leanback.supportleanbackshowcase.R.raw#grid_live_broadcast}
         */
        private void createRows() {
            String json = Utils.inputStreamToString(getResources().openRawResource(
                    R.raw.grid_live_broadcast));
            CardRow[] rows = new Gson().fromJson(json, CardRow[].class);
            for (CardRow row : rows) {
                if (row.getType() == CardRow.TYPE_DEFAULT) {
                    mRowsAdapter.add(createCardRow(row));
                }
            }
            Log.d(TAG,"Live broadcast: rows created");

        }

        private Row createCardRow(CardRow cardRow) {
            PresenterSelector presenterSelector = new CardPresenterSelector(getActivity());
            ArrayObjectAdapter adapter = new ArrayObjectAdapter(presenterSelector);

            //add card based on content provider
            String provider = MySettingsFragment.getDefaults("pref_providers_key",getContext());


            for (Card card : cardRow.getCards()) {
                if(card.getContentProvider().equals(Card.Provider.DEFAULT)){
                    adapter.add(card);
                }
                else if(provider.equals("fptplay")&&card.getContentProvider().equals(Card.Provider.FPTPLAY)){
                    adapter.add(card);
                }
                else if(provider.equals("hotstar")&&card.getContentProvider().equals(Card.Provider.HOTSTAR)){
                    adapter.add(card);
                }

            }

            IconHeaderItem headerItem = new IconHeaderItem(cardRow.getTitle(),R.drawable.live_icon_text);
            //HeaderItem headerItem = new HeaderItem(cardRow.getTitle());
            return new CardListRow(headerItem, adapter, cardRow);
        }
    }

    /**
     * Fragment for the default landing page (Home).
     */
    public static class FragmentHome extends RowsFragment {
        private final ArrayObjectAdapter mRowsAdapter;

        public FragmentHome() {
            mRowsAdapter = new ArrayObjectAdapter(new CustomShadowRowPresenterSelector());

            setAdapter(mRowsAdapter);
            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                @Override
                public void onItemClicked(
                        Presenter.ViewHolder itemViewHolder,
                        Object item,
                        RowPresenter.ViewHolder rowViewHolder,
                        Row row) {

                    Card selectedCard = (Card)item;
                    Intent intent = new Intent(getContext(), DetailViewActivity.class);
                    intent.putExtra("videoId",selectedCard.getVideoId());

                    //to set video title
                    intent.putExtra("videoTitle",selectedCard.getTitle());

                    startActivity(intent);
                }
            });
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            createRows();
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            //expand
            setExpand(true);

            return super.onCreateView(inflater,container,savedInstanceState);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            view.setContentDescription("Home Fragment");

            super.onViewCreated(view, savedInstanceState);

            Log.d(TAG,"Home: on view created");

        }

        @Override
        public void onTransitionEnd(){
            //expand
            setExpand(true);

            super.onTransitionEnd();
        }

        /**
         * Create rows based on cards data declared in {@link android.support.v17.leanback.supportleanbackshowcase.R.raw#home_browse_row}
         */
        private void createRows() {
            String json = Utils.inputStreamToString(getResources().openRawResource(
                    R.raw.home_browse_row));
            CardRow[] rows = new Gson().fromJson(json, CardRow[].class);
            for (CardRow row : rows) {
                if (row.getType() == CardRow.TYPE_DEFAULT) {
                    mRowsAdapter.add(createCardRow(row));
                }
            }
        }

        private Row createCardRow(CardRow cardRow) {
            PresenterSelector presenterSelector = new CardPresenterSelector(getActivity());
            ArrayObjectAdapter adapter = new ArrayObjectAdapter(presenterSelector);
            //add card based on content provider
            String provider = MySettingsFragment.getDefaults("pref_providers_key",getContext());


            for (Card card : cardRow.getCards()) {
                if(card.getContentProvider().equals(Card.Provider.DEFAULT)){
                    adapter.add(card);
                }
                else if(provider.equals("fptplay")&&card.getContentProvider().equals(Card.Provider.FPTPLAY)){
                    adapter.add(card);
                }
                else if(provider.equals("hotstar")&&card.getContentProvider().equals(Card.Provider.HOTSTAR)){
                    adapter.add(card);
                }

            }

            IconHeaderItem headerItem;

            if(cardRow.getTitle().toLowerCase().contains("recommended")||cardRow.getTitle().toLowerCase().contains("last watched")){
                headerItem = new IconHeaderItem(cardRow.getTitle(),R.drawable.vod_icon_text);
            }
            else{
                headerItem = new IconHeaderItem(cardRow.getTitle(),R.drawable.live_icon_text);
            }

            //HeaderItem headerItem = new HeaderItem(cardRow.getTitle());
            return new CardListRow(headerItem, adapter, cardRow);
        }
    }

    /**
     * Fragment for category TV Shows.
     */
    public static class FragmentTvShow extends RowsFragment {
        private final ArrayObjectAdapter mRowsAdapter;

        public FragmentTvShow() {
            mRowsAdapter = new ArrayObjectAdapter(new CustomShadowRowPresenterSelector());
            setAdapter(mRowsAdapter);


            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                @Override
                public void onItemClicked(
                        Presenter.ViewHolder itemViewHolder,
                        Object item,
                        RowPresenter.ViewHolder rowViewHolder,
                        Row row) {

                    Card selectedCard = (Card)item;
                    Intent intent = new Intent(getContext(), DetailViewActivity.class);
                    intent.putExtra("videoId",selectedCard.getVideoId());

                    //to set video title
                    intent.putExtra("videoTitle",selectedCard.getTitle());



                    startActivity(intent);
                    Log.d(TAG,"open tv show details page");




                }
            });
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            createRows();
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            //expand
            setExpand(true);

            return super.onCreateView(inflater,container,savedInstanceState);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            view.setContentDescription("TV Show Fragment");

            super.onViewCreated(view, savedInstanceState);

            Log.d(TAG,"TV Show: on view created");

        }

        @Override
        public void onTransitionEnd(){
            //expand
            setExpand(true);

            super.onTransitionEnd();
        }

        /**
         * Create rows based on cards data declared in {@link android.support.v17.leanback.supportleanbackshowcase.R.raw#tvshow_browse_row}
         */
        private void createRows() {
            String json = Utils.inputStreamToString(getResources().openRawResource(
                    R.raw.tvshow_browse_row));
            CardRow[] rows = new Gson().fromJson(json, CardRow[].class);
            for (CardRow row : rows) {
                if (row.getType() == CardRow.TYPE_DEFAULT) {
                    mRowsAdapter.add(createCardRow(row));
                }
            }
        }

        private Row createCardRow(CardRow cardRow) {
            PresenterSelector presenterSelector = new CardPresenterSelector(getActivity());
            ArrayObjectAdapter adapter = new ArrayObjectAdapter(presenterSelector);
            //add card based on content provider
            String provider = MySettingsFragment.getDefaults("pref_providers_key",getContext());


            for (Card card : cardRow.getCards()) {
                if(card.getContentProvider().equals(Card.Provider.DEFAULT)){
                    adapter.add(card);
                }
                else if(provider.equals("fptplay")&&card.getContentProvider().equals(Card.Provider.FPTPLAY)){
                    adapter.add(card);
                }
                else if(provider.equals("hotstar")&&card.getContentProvider().equals(Card.Provider.HOTSTAR)){
                    adapter.add(card);
                }

            }

            //IconHeaderItem headerItem = new IconHeaderItem(cardRow.getTitle());
            IconHeaderItem headerItem;
            //long headerId = getArguments().getLong("headerId");

            headerItem =  new IconHeaderItem(cardRow.getTitle(),R.drawable.vod_icon_text_yellow);



            return new CardListRow(headerItem, adapter, cardRow);
        }
    }

    /**
     * Fragment for category Nesw & Sports.
     */
    public static class FragmentNewsSports extends RowsFragment {
        private final ArrayObjectAdapter mRowsAdapter;

        public FragmentNewsSports() {
            mRowsAdapter = new ArrayObjectAdapter(new CustomShadowRowPresenterSelector());

            setAdapter(mRowsAdapter);
            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                @Override
                public void onItemClicked(
                        Presenter.ViewHolder itemViewHolder,
                        Object item,
                        RowPresenter.ViewHolder rowViewHolder,
                        Row row) {
                    /*Toast.makeText(getActivity(), "Implement click handler", Toast.LENGTH_SHORT)
                            .show();*/
                    Card selectedCard = (Card)item;

                    Intent intent = new Intent(getContext(), DetailViewActivity.class);
                    intent.putExtra("videoId",selectedCard.getVideoId());

                    //to set video title
                    intent.putExtra("videoTitle",selectedCard.getTitle());


                    startActivity(intent);




                }
            });
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            createRows();
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            //expand
            setExpand(true);

            return super.onCreateView(inflater,container,savedInstanceState);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            view.setContentDescription("News & Sports Fragment");

            super.onViewCreated(view, savedInstanceState);

            Log.d(TAG,"News & Sports : on view created");

        }

        @Override
        public void onTransitionEnd(){
            //expand
            setExpand(true);

            super.onTransitionEnd();
        }

        /**
         * Create rows based on cards data declared in {@link android.support.v17.leanback.supportleanbackshowcase.R.raw#news_sports_browse_row}
         */
        private void createRows() {
            String json = Utils.inputStreamToString(getResources().openRawResource(
                    R.raw.news_sports_browse_row));
            CardRow[] rows = new Gson().fromJson(json, CardRow[].class);
            for (CardRow row : rows) {
                if (row.getType() == CardRow.TYPE_DEFAULT) {
                    mRowsAdapter.add(createCardRow(row));
                }
            }
        }

        private Row createCardRow(CardRow cardRow) {
            PresenterSelector presenterSelector = new CardPresenterSelector(getActivity());
            ArrayObjectAdapter adapter = new ArrayObjectAdapter(presenterSelector);
            //add card based on content provider
            String provider = MySettingsFragment.getDefaults("pref_providers_key",getContext());


            for (Card card : cardRow.getCards()) {
                if(card.getContentProvider().equals(Card.Provider.DEFAULT)){
                    adapter.add(card);
                }
                else if(provider.equals("fptplay")&&card.getContentProvider().equals(Card.Provider.FPTPLAY)){
                    adapter.add(card);
                }
                else if(provider.equals("hotstar")&&card.getContentProvider().equals(Card.Provider.HOTSTAR)){
                    adapter.add(card);
                }

            }

            //IconHeaderItem headerItem = new IconHeaderItem(cardRow.getTitle());
            IconHeaderItem headerItem;
            //long headerId = getArguments().getLong("headerId");
            headerItem =  new IconHeaderItem(cardRow.getTitle(),R.drawable.vod_icon_text_green);



            return new CardListRow(headerItem, adapter, cardRow);
        }
    }

    /**
     * Fragment for category Movies.
     */
    public static class FragmentMovie extends RowsFragment {
        private final ArrayObjectAdapter mRowsAdapter;

        public FragmentMovie() {
            mRowsAdapter = new ArrayObjectAdapter(new CustomShadowRowPresenterSelector());

            setAdapter(mRowsAdapter);
            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                @Override
                public void onItemClicked(
                        Presenter.ViewHolder itemViewHolder,
                        Object item,
                        RowPresenter.ViewHolder rowViewHolder,
                        Row row) {


                    Card selectedCard = (Card)item;
                    Intent intent = new Intent(getContext(), DetailViewActivity.class);
                    intent.putExtra("videoId",selectedCard.getVideoId());

                    //to set video title
                    intent.putExtra("videoTitle",selectedCard.getTitle());

                        Log.d(TAG,"open movie details page");

                        startActivity(intent);
                    //getActivity().startActivityForResult(intent,101);

                    //}
                    /*else{
                        Toast.makeText(getActivity(), "this is a dummy movie", Toast.LENGTH_SHORT)
                                .show();
                    }*/


                }
            });
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            createRows();
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            //expand
            setExpand(true);

            return super.onCreateView(inflater,container,savedInstanceState);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            view.setContentDescription("Movies Fragment");

            super.onViewCreated(view, savedInstanceState);

            Log.d(TAG,"Movies : on view created");

        }

        @Override
        public void onTransitionEnd(){
            //expand
            setExpand(true);

            super.onTransitionEnd();
        }

        /**
         * Create rows based on cards data declared in {@link android.support.v17.leanback.supportleanbackshowcase.R.raw#movie_browse_row}
         */
        private void createRows() {
            String json = Utils.inputStreamToString(getResources().openRawResource(
                    R.raw.movie_browse_row));
            CardRow[] rows = new Gson().fromJson(json, CardRow[].class);
            for (CardRow row : rows) {
                if (row.getType() == CardRow.TYPE_DEFAULT) {
                    mRowsAdapter.add(createCardRow(row));
                }
            }
        }

        private Row createCardRow(CardRow cardRow) {
            PresenterSelector presenterSelector = new CardPresenterSelector(getActivity());
            ArrayObjectAdapter adapter = new ArrayObjectAdapter(presenterSelector);
            //add card based on content provider
            String provider = MySettingsFragment.getDefaults("pref_providers_key",getContext());


            for (Card card : cardRow.getCards()) {
                if(card.getContentProvider().equals(Card.Provider.DEFAULT)){
                    adapter.add(card);
                }
                else if(provider.equals("fptplay")&&card.getContentProvider().equals(Card.Provider.FPTPLAY)){
                    adapter.add(card);
                }
                else if(provider.equals("hotstar")&&card.getContentProvider().equals(Card.Provider.HOTSTAR)){
                    adapter.add(card);
                }

            }

            IconHeaderItem headerItem = new IconHeaderItem(cardRow.getTitle(),R.drawable.vod_icon_text_blue);
            //HeaderItem headerItem = new HeaderItem(cardRow.getTitle());
            return new CardListRow(headerItem, adapter, cardRow);
        }
    }

    /**
     * Fragment for Settings menu.
     */
    public static class SettingsFragment extends RowsFragment {
        private final ArrayObjectAdapter mRowsAdapter;

        private final int SETTINGS_PROVIDERS = 1;
        private final int SETTINGS_KEY_SHORTCUTS = 2;
        private final int SETTINGS_HISTORY = 3;
        private final int SETTINGS_INTERFACE = 4;
        private final int SETTINGS_PRIVACY = 5;


        public SettingsFragment() {
            ListRowPresenter selector = new ListRowPresenter();
            selector.setNumRows(1);
            mRowsAdapter = new ArrayObjectAdapter(selector);
            setAdapter(mRowsAdapter);

            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                @Override
                public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

                   // int logo = getActivity().getIntent().getBundleExtra("extras").getInt("logo");

                    Card settings = (Card)item;
                    Intent intent = new Intent(getContext(), MySettingsActivity.class);
                    //Bundle extras = new Bundle();
                   // extras.putInt("logo",logo);

                    switch (settings.getId()){
                        case SETTINGS_INTERFACE:
                            getActivity().startActivityForResult(intent,1);

                            break;
                        case SETTINGS_PROVIDERS:
                            getActivity().startActivityForResult(intent,1);
                            break;

                        default:
                            Toast.makeText(getActivity(), settings.getTitle()+" clicked", Toast.LENGTH_SHORT)
                                        .show();

                    }
                }
            });
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            }, 200);
        }

        private void loadData() {
            if (isAdded()) {
                String json = Utils.inputStreamToString(getResources().openRawResource(
                        R.raw.icon_settings));
                CardRow cardRow = new Gson().fromJson(json, CardRow.class);
                mRowsAdapter.add(createCardRow(cardRow));
                getMainFragmentAdapter().getFragmentHost().notifyDataReady(
                        getMainFragmentAdapter());
            }
        }

        private ListRow createCardRow(CardRow cardRow) {
            SettingsIconPresenter iconCardPresenter = new SettingsIconPresenter(getActivity());
            ArrayObjectAdapter adapter = new ArrayObjectAdapter(iconCardPresenter);
            for(Card card : cardRow.getCards()) {
                adapter.add(card);

                Log.d(TAG,"icon card type: "+ card.getType());
            }

            //IconHeaderItem headerItem = new IconHeaderItem(cardRow.getTitle());
            HeaderItem headerItem = new HeaderItem(cardRow.getTitle());
            return new CardListRow(headerItem, adapter, cardRow);
        }


    }

    /**
     * Fragment for Login and Sign-up menu.
     */
    public static class FragmentLoginSignUp extends Fragment implements MainFragmentAdapterProvider {
        private MainFragmentAdapter mMainFragmentAdapter = new MainFragmentAdapter(this);
        private WebView mWebview;

        @Override
        public MainFragmentAdapter getMainFragmentAdapter() {
            return mMainFragmentAdapter;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getMainFragmentAdapter().getFragmentHost().showTitleView(false);
        }

        @Override
        public View onCreateView(
                LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            FrameLayout root = new FrameLayout(getActivity());
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            lp.setMarginStart(32);
            mWebview = new WebView(getActivity());
            mWebview.setWebViewClient(new WebViewClient());
            mWebview.getSettings().setJavaScriptEnabled(true);
            root.addView(mWebview, lp);
            return root;
        }

        @Override
        public void onResume() {
            super.onResume();
            mWebview.loadUrl("https://accounts.google.com/signin/v2/identifier?continue=https%3A%2F%2Fwww.youtube.com%2Fsignin%3Fnext%3D%252Faccount%26action_handle_signin%3Dtrue%26feature%3Dredirect_login%26hl%3Den%26app%3Ddesktop&hl=en&service=youtube&flowName=GlifWebSignIn&flowEntry=ServiceLogin");
            //mWebview.loadUrl("https://chromecast-5545d.firebaseapp.com/");
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());


        }
    }


}

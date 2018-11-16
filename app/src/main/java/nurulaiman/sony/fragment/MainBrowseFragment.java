package nurulaiman.sony.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.app.RowsFragment;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.app.details.ShadowRowPresenterSelector;
import android.support.v17.leanback.supportleanbackshowcase.app.page.GridFragment;
import android.support.v17.leanback.supportleanbackshowcase.app.page.PageAndListRowFragment;
import android.support.v17.leanback.supportleanbackshowcase.app.page.SettingsIconPresenter;
import android.support.v17.leanback.supportleanbackshowcase.cards.presenters.CardPresenterSelector;
import android.support.v17.leanback.supportleanbackshowcase.models.Card;
import android.support.v17.leanback.supportleanbackshowcase.models.CardRow;
import android.support.v17.leanback.supportleanbackshowcase.models.DetailedCard;
import android.support.v17.leanback.supportleanbackshowcase.utils.CardListRow;
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
import android.support.v17.leanback.widget.VerticalGridPresenter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

//implement voice interaction
import android.app.VoiceInteractor;
import android.app.VoiceInteractor.PickOptionRequest;
import android.app.VoiceInteractor.PickOptionRequest.Option;

import nurulaiman.sony.activity.DetailViewLiveBroadcastActivity;
import nurulaiman.sony.activity.DetailViewMovieActivity;
import nurulaiman.sony.activity.DetailViewTvShowActivity;
import nurulaiman.sony.activity.LiveActivity;
import nurulaiman.sony.activity.MainActivity;
import nurulaiman.sony.activity.MySettingsActivity;
import nurulaiman.sony.activity.SearchActivity;
import nurulaiman.sony.activity.YoutubePlayerActivity;
import nurulaiman.sony.models.IconHeaderItem;
import nurulaiman.sony.ui.presenter.CustomShadowRowPresenterSelector;
import nurulaiman.sony.ui.presenter.IconHeaderItemPresenter;
import nurulaiman.sony.utils.MatchingCardUtils;

public class MainBrowseFragment extends BrowseFragment {
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

    private static String TAG = "MainBrowseFragment";

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

    @Override
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



    }



    public static MainBrowseFragment newInstance() {
        Log.d(TAG, "newInstance: ");
        MainBrowseFragment fragment = new MainBrowseFragment();
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
// with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("activity-says-hi"));
    }

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
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
                logo = R.drawable.hotstar;
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
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setAdapter(mRowsAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                createRows();
                startEntranceTransition();
            }
        }, 2000);
    }

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
                return new MainBrowseFragment.FragmentLoginSignUp();
            }
            else if(row.getHeaderItem().getId() == HEADER_ID_10){
                return new MainBrowseFragment.SettingsFragment();
            }
            else if(row.getHeaderItem().getId() == HEADER_ID_1){
                return new MainBrowseFragment.FragmentHome();
            }
            else if(row.getHeaderItem().getId() == HEADER_ID_2){
                return new MainBrowseFragment.FragmentLiveBroadcast();
            }
            else if(row.getHeaderItem().getId() == HEADER_ID_4){

                return new MainBrowseFragment.FragmentTvShow();
            }
            else if(row.getHeaderItem().getId() == HEADER_ID_5){
                return new MainBrowseFragment.FragmentMovie();
            }

            else if(row.getHeaderItem().getId() == HEADER_ID_3){

                return new MainBrowseFragment.FragmentNewsSports();
            }


            throw new IllegalArgumentException(String.format("Invalid row %s", rowObj));
        }
    }

    public static class PageFragmentAdapterImpl extends MainFragmentAdapter<PageAndListRowFragment.SampleFragmentA> {

        public PageFragmentAdapterImpl(PageAndListRowFragment.SampleFragmentA fragment) {
            super(fragment);
        }
    }

    /**
     * Simple page fragment implementation.
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
                    Intent intent;
                    Card card = (Card)item;



                        intent = new Intent(getContext(), DetailViewLiveBroadcastActivity.class);
                        intent.putExtra("videoId",card.getVideoId());
                        //to set video title
                        intent.putExtra("videoTitle",card.getTitle());
                        startActivity(intent);
                        Log.d(TAG,"open live tv details page");


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
       public void onTransitionEnd(){
           //expand
           setExpand(true);

           super.onTransitionEnd();
       }

        private void createRows() {
            String json = Utils.inputStreamToString(getResources().openRawResource(
                    R.raw.grid_live_broadcast));
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
            for (Card card : cardRow.getCards()) {
                adapter.add(card);
            }

            IconHeaderItem headerItem = new IconHeaderItem(cardRow.getTitle(),R.drawable.live_icon_text);
            //HeaderItem headerItem = new HeaderItem(cardRow.getTitle());
            return new CardListRow(headerItem, adapter, cardRow);
        }
    }

    /**
     * Page fragment embeds a rows fragment.
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
                    //.makeText(getActivity(), "Implement click handler", Toast.LENGTH_SHORT.show();
                    Intent intent = new Intent(getContext(), YoutubePlayerActivity.class);
                    Card selectedCard = (Card)item;


                    intent.putExtra("videoId",selectedCard.getVideoId());
                    startActivity(intent);
                    Log.d(TAG,"play non-live youtube video");
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
        public void onTransitionEnd(){
            //expand
            setExpand(true);

            super.onTransitionEnd();
        }

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
            for (Card card : cardRow.getCards()) {
                adapter.add(card);
            }

            IconHeaderItem headerItem;

            if(cardRow.getTitle().toLowerCase().contains("recommended")||cardRow.getTitle().toLowerCase().contains("popular")){
                headerItem = new IconHeaderItem(cardRow.getTitle(),R.drawable.vod_icon_text);
            }
            else{
                headerItem = new IconHeaderItem(cardRow.getTitle(),R.drawable.live_icon_text);
            }

            //HeaderItem headerItem = new HeaderItem(cardRow.getTitle());
            return new CardListRow(headerItem, adapter, cardRow);
        }
    }

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

                    Intent intent = null;
                    Card selectedCard = (Card)item;

                    intent = new Intent(getContext(), DetailViewTvShowActivity.class);
                    intent.putExtra("videoId",selectedCard.getVideoId());
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
        public void onTransitionEnd(){
            //expand
            setExpand(true);

            super.onTransitionEnd();
        }

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
            for (Card card : cardRow.getCards()) {
                adapter.add(card);
            }

            //IconHeaderItem headerItem = new IconHeaderItem(cardRow.getTitle());
            IconHeaderItem headerItem;
            //long headerId = getArguments().getLong("headerId");

            headerItem =  new IconHeaderItem(cardRow.getTitle(),R.drawable.vod_icon_text_yellow);



            return new CardListRow(headerItem, adapter, cardRow);
        }
    }

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
                    Intent intent = null;
                    Card selectedCard = (Card)item;

                    intent = new Intent(getContext(), DetailViewTvShowActivity.class);
                    intent.putExtra("videoId",selectedCard.getVideoId());
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
        public void onTransitionEnd(){
            //expand
            setExpand(true);

            super.onTransitionEnd();
        }

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
            for (Card card : cardRow.getCards()) {
                adapter.add(card);
            }

            //IconHeaderItem headerItem = new IconHeaderItem(cardRow.getTitle());
            IconHeaderItem headerItem;
            //long headerId = getArguments().getLong("headerId");
            headerItem =  new IconHeaderItem(cardRow.getTitle(),R.drawable.vod_icon_text_green);



            return new CardListRow(headerItem, adapter, cardRow);
        }
    }

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


                    Intent intent = new Intent(getContext(), DetailViewMovieActivity.class);
                    Card selectedCard = (Card)item;



                    //if(selectedCard.getDescription().toLowerCase().contains("korean")||selectedCard.getTitle().toLowerCase().contains("gone")) {

                        intent.putExtra("videoId",selectedCard.getVideoId());
                        intent.putExtra("videoTitle",selectedCard.getTitle());
                        //intent.putExtra("cardType",selectedCard.getType().toString());

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
        public void onTransitionEnd(){
            //expand
            setExpand(true);

            super.onTransitionEnd();
        }

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
            for (Card card : cardRow.getCards()) {
                adapter.add(card);
            }

            IconHeaderItem headerItem = new IconHeaderItem(cardRow.getTitle(),R.drawable.vod_icon_text_blue);
            //HeaderItem headerItem = new HeaderItem(cardRow.getTitle());
            return new CardListRow(headerItem, adapter, cardRow);
        }
    }

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
                            //TODO change between developer/user interface
                            getActivity().startActivityForResult(intent,1);

                            break;
                        case SETTINGS_PROVIDERS:
                            //TODO change provider's logo
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
            //mWebview.loadUrl("https://accounts.google.com/signin/v2/identifier?continue=https%3A%2F%2Fwww.youtube.com%2Fsignin%3Fnext%3D%252Faccount%26action_handle_signin%3Dtrue%26feature%3Dredirect_login%26hl%3Den%26app%3Ddesktop&hl=en&service=youtube&flowName=GlifWebSignIn&flowEntry=ServiceLogin");
            mWebview.loadUrl("https://chromecast-5545d.firebaseapp.com/");
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());


        }
    }
}

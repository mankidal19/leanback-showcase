package nurulaiman.sony;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;


public class MainBrowseFragment extends BrowseFragment {
    private static final long HEADER_ID_1 = 1;
    private static final String HEADER_NAME_1 = "HOME";
    private static final long HEADER_ID_2 = 2;
    private static final String HEADER_NAME_2 = "LIVE BROADCASTS";
    private static final long HEADER_ID_3 = 3;
    private static final String HEADER_NAME_3 = "TV SHOWS";
    private static final long HEADER_ID_4 = 4;
    private static final String HEADER_NAME_4 = "MOVIES";

    private static final long HEADER_ID_5 = 5;
    private static final String HEADER_NAME_5 = "Login/Sign Up";
    private static final long HEADER_ID_6 = 6;
    private static final String HEADER_NAME_6 = "Settings";


    private BackgroundManager mBackgroundManager;

    private ArrayObjectAdapter mRowsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
        loadData();
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        getMainFragmentRegistry().registerFragment(PageRow.class,
                new PageRowFragmentFactory(mBackgroundManager));
    }

    private void setupUi() {
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(getResources().getColor(R.color.fastlane_background));
        setTitle("YouTube OpApp");
        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(
                        getActivity(), getString(R.string.implement_search), Toast.LENGTH_SHORT)
                        .show();
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
        HeaderItem headerItem1 = new HeaderItem(HEADER_ID_1, HEADER_NAME_1);
        PageRow pageRow1 = new PageRow(headerItem1);
        mRowsAdapter.add(pageRow1);

        HeaderItem headerItem2 = new HeaderItem(HEADER_ID_2, HEADER_NAME_2);
        PageRow pageRow2 = new PageRow(headerItem2);
        mRowsAdapter.add(pageRow2);

        HeaderItem headerItem3 = new HeaderItem(HEADER_ID_3, HEADER_NAME_3);
        PageRow pageRow3 = new PageRow(headerItem3);
        mRowsAdapter.add(pageRow3);

        HeaderItem headerItem4 = new HeaderItem(HEADER_ID_4, HEADER_NAME_4);
        PageRow pageRow4 = new PageRow(headerItem4);
        mRowsAdapter.add(pageRow4);

        HeaderItem headerItem5 = new HeaderItem(HEADER_ID_5, HEADER_NAME_5);
        PageRow pageRow5 = new PageRow(headerItem5);
        mRowsAdapter.add(pageRow5);

        HeaderItem headerItem6 = new HeaderItem(HEADER_ID_6, HEADER_NAME_6);
        PageRow pageRow6 = new PageRow(headerItem6);
        mRowsAdapter.add(pageRow6);
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

            if(row.getHeaderItem().getId() == HEADER_ID_5){
                return new MainBrowseFragment.FragmentLoginSignUp();
            }
            else if(row.getHeaderItem().getId() == HEADER_ID_6){
                return new MainBrowseFragment.SettingsFragment();
            }
            else if(row.getHeaderItem().getId() == HEADER_ID_1){
                return new MainBrowseFragment.FragmentHome();
            }
            else if(row.getHeaderItem().getId() == HEADER_ID_2){
                return new MainBrowseFragment.FragmentLiveBroadcast();
            }
            else if(row.getHeaderItem().getId() == HEADER_ID_3){
                return new MainBrowseFragment.FragmentTvShow();
            }
            else if(row.getHeaderItem().getId() == HEADER_ID_4){
                return new MainBrowseFragment.FragmentMovie();
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


    public static class FragmentLiveBroadcast extends GridFragment {
        private static final int COLUMNS = 3;
        private final int ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_SMALL;
        private ArrayObjectAdapter mAdapter;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setupAdapter();
            loadData();
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }


        private void setupAdapter() {
            VerticalGridPresenter presenter = new VerticalGridPresenter(ZOOM_FACTOR);
            presenter.setNumberOfColumns(COLUMNS);
            setGridPresenter(presenter);

            CardPresenterSelector cardPresenter = new CardPresenterSelector(getActivity());
            mAdapter = new ArrayObjectAdapter(cardPresenter);
            setAdapter(mAdapter);

            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                @Override
                public void onItemClicked(
                        Presenter.ViewHolder itemViewHolder,
                        Object item,
                        RowPresenter.ViewHolder rowViewHolder,
                        Row row) {
                    Card card = (Card)item;
                    Toast.makeText(getActivity(),
                            "Clicked on "+card.getTitle(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void loadData() {
            String json = Utils.inputStreamToString(getResources().openRawResource(
                    R.raw.grid_live_broadcast));
            CardRow cardRow = new Gson().fromJson(json, CardRow.class);
            mAdapter.addAll(0, cardRow.getCards());
        }
    }

    /**
     * Page fragment embeds a rows fragment.
     */
    public static class FragmentHome extends RowsFragment {
        private final ArrayObjectAdapter mRowsAdapter;

        public FragmentHome() {
            mRowsAdapter = new ArrayObjectAdapter(new ShadowRowPresenterSelector());

            setAdapter(mRowsAdapter);
            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                @Override
                public void onItemClicked(
                        Presenter.ViewHolder itemViewHolder,
                        Object item,
                        RowPresenter.ViewHolder rowViewHolder,
                        Row row) {
                    Toast.makeText(getActivity(), "Implement click handler", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            createRows();
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
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

            HeaderItem headerItem = new HeaderItem(cardRow.getTitle());
            return new CardListRow(headerItem, adapter, cardRow);
        }
    }

    public static class FragmentTvShow extends RowsFragment {
        private final ArrayObjectAdapter mRowsAdapter;

        public FragmentTvShow() {
            mRowsAdapter = new ArrayObjectAdapter(new ShadowRowPresenterSelector());

            setAdapter(mRowsAdapter);
            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                @Override
                public void onItemClicked(
                        Presenter.ViewHolder itemViewHolder,
                        Object item,
                        RowPresenter.ViewHolder rowViewHolder,
                        Row row) {
                    Toast.makeText(getActivity(), "Implement click handler", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            createRows();
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
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

            HeaderItem headerItem = new HeaderItem(cardRow.getTitle());
            return new CardListRow(headerItem, adapter, cardRow);
        }
    }

    public static class FragmentMovie extends RowsFragment {
        private final ArrayObjectAdapter mRowsAdapter;

        public FragmentMovie() {
            mRowsAdapter = new ArrayObjectAdapter(new ShadowRowPresenterSelector());

            setAdapter(mRowsAdapter);
            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                @Override
                public void onItemClicked(
                        Presenter.ViewHolder itemViewHolder,
                        Object item,
                        RowPresenter.ViewHolder rowViewHolder,
                        Row row) {
                    Toast.makeText(getActivity(), "Implement click handler", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            createRows();
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
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

            HeaderItem headerItem = new HeaderItem(cardRow.getTitle());
            return new CardListRow(headerItem, adapter, cardRow);
        }
    }

    public static class SettingsFragment extends RowsFragment {
        private final ArrayObjectAdapter mRowsAdapter;

        public SettingsFragment() {
            ListRowPresenter selector = new ListRowPresenter();
            selector.setNumRows(1);
            mRowsAdapter = new ArrayObjectAdapter(selector);
            setAdapter(mRowsAdapter);
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
            }

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
            mWebview.loadUrl("https://accounts.google.com/signin/v2/identifier?continue=https%3A%2F%2Fwww.youtube.com%2Fsignin%3Fnext%3D%252Faccount%26action_handle_signin%3Dtrue%26feature%3Dredirect_login%26hl%3Den%26app%3Ddesktop&hl=en&service=youtube&flowName=GlifWebSignIn&flowEntry=ServiceLogin");
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }
    }
}

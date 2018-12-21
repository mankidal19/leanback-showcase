package nurulaiman.sony.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.cards.presenters.CardPresenter;
import android.support.v17.leanback.supportleanbackshowcase.cards.presenters.CardPresenterSelector;
import android.support.v17.leanback.supportleanbackshowcase.models.Card;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.BuildConfig;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import nurulaiman.sony.activity.DetailViewLiveBroadcastActivity;
import nurulaiman.sony.activity.DetailViewMovieActivity;
import nurulaiman.sony.activity.DetailViewTvShowActivity;
import nurulaiman.sony.activity.LiveActivity;
import nurulaiman.sony.activity.MainActivity;
import nurulaiman.sony.activity.YoutubePlayerActivity;
import nurulaiman.sony.data.MockDatabase;


public class MySearchFragment extends android.support.v17.leanback.app.SearchFragment
        implements android.support.v17.leanback.app.SearchFragment.SearchResultProvider {

    private static final String TAG = "SearchFragment";

    private ArrayObjectAdapter mRowsAdapter;
    private Handler mHandler = new Handler();
    private SearchRunnable mDelayedLoad;
    protected Activity mActivity;

    private static final boolean FINISH_ON_RECOGNIZER_CANCELED = true;
    private static final int REQUEST_SPEECH = 0x00000010;
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private boolean mResultsFound = false;

    private Intent intent = null;

    /*private MockDatabase mockDatabase = new MockDatabase(getContext());*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setSearchResultProvider(this);
        setOnItemViewClickedListener(getDefaultItemClickedListener());
        mDelayedLoad = new SearchRunnable();
        mActivity = getActivity();
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacksAndMessages(null);
        if(intent==null){
            intent = new Intent(getContext(),MainActivity.class);
            startActivity(intent);
        }

        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SPEECH:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        setSearchQuery(data, true);
                        break;
                    default:
                        // If recognizer is canceled or failed, keep focus on the search orb
                        if (FINISH_ON_RECOGNIZER_CANCELED) {
                            if (!hasResults()) {
                                if (DEBUG) Log.v(TAG, "Voice search canceled");
                                getView().findViewById(R.id.lb_search_bar_speech_orb).requestFocus();
                            }
                        }
                        break;
                }
                break;
        }
    }

    public boolean hasResults() {
        return mRowsAdapter.size() > 0 && mResultsFound;
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return mRowsAdapter;
    }

    private void queryByWords(String words) {
        mRowsAdapter.clear();
        if (!TextUtils.isEmpty(words) && words.length() > 2) {
            mDelayedLoad.setSearchQuery(words);
            //mDelayedLoad.setSearchType(MediaWrapper.TYPE_ALL);
            new Thread(mDelayedLoad).start();
        }
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        queryByWords(newQuery);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        queryByWords(query);
        return true;
    }



    private void loadRows(String query, int type) {
        //ArrayList<MediaWrapper> mediaList = MediaLibrary.getInstance().searchMedia(query);
        MockDatabase mockDatabase = new MockDatabase(getContext());
        List<Card> cardList = mockDatabase.search(query);

        final ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter(getContext()));
        //final ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenterSelector(mActivity));


        //listRowAdapter.addAll(0, mediaList);
        listRowAdapter.addAll(0, cardList);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                int titleRes = -1;
                if (cardList.isEmpty()){
                    titleRes = R.string.no_search_results;
                    HeaderItem header = new HeaderItem(0,getString(titleRes,query));
                    mRowsAdapter.add(new ListRow(header, listRowAdapter));
                    mResultsFound = false;
                }
                else{
                    titleRes = R.string.search_results;
                    HeaderItem header = new HeaderItem(0,getString(titleRes,query));
                    mRowsAdapter.add(new ListRow(header, listRowAdapter));
                    mResultsFound = true;
                }

            }
        });
    }

    protected OnItemViewClickedListener getDefaultItemClickedListener() {
        return new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
               /* if (item instanceof MediaWrapper) {
                    TvUtil.openMedia(mActivity, (MediaWrapper) item, row);
                }*/

                if(item instanceof Card){
                    Card selectedCard = (Card)item;

                    if(selectedCard.isLive()){

                            intent = new Intent(getContext(), DetailViewLiveBroadcastActivity.class);
                            //intent.putExtra("mediaId",id);
                            intent.putExtra("videoId",selectedCard.getVideoId());

                            //to set video title
                            intent.putExtra("videoTitle",selectedCard.getTitle());

                            startActivity(intent);
                            Log.d(TAG,"open sample live tv details page");

                        /*//directly play video
                        else{
                            intent = new Intent(getContext(), LiveActivity.class);
                            //intent.putExtra("mediaId",id);
                            intent.putExtra("videoId",selectedCard.getVideoId());

                            //to set video title
                            intent.putExtra("videoTitle",selectedCard.getTitle());

                            startActivity(intent);
                            Log.d(TAG,"play live video");
                        }*/

                    }

                    else{
                        //for sample movie details page
                        if(selectedCard.getTitle().toLowerCase().contains("gone")||selectedCard.getTitle().toLowerCase().contains("korean")){
                            intent = new Intent(getContext(), DetailViewMovieActivity.class);
                            // intent.putExtra("mediaId",id);
                            intent.putExtra("videoId",selectedCard.getVideoId());
                            intent.putExtra("videoTitle",selectedCard.getTitle());

                            startActivity(intent);
                            Log.d(TAG,"open movie details page");
                        }
                        //for dummy movie
                        else if(selectedCard.getDescription().toLowerCase().contains("$3.99")){
                            Toast.makeText(getActivity(), "this is a dummy movie", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(),MainActivity.class));
                        }

                        else{
                            intent = new Intent(getContext(), DetailViewTvShowActivity.class);
                            //intent.putExtra("mediaId",id);
                            intent.putExtra("videoId",selectedCard.getVideoId());
                            intent.putExtra("videoTitle",selectedCard.getTitle());

                            startActivity(intent);
                            Log.d(TAG,"open tv show details page");
                        }


                    }

                }
            }
        };
    }

    private class SearchRunnable implements Runnable {

        private volatile String searchQuery;
        private volatile int searchType;

        public SearchRunnable() {}

        public void run() {
            loadRows(searchQuery, searchType);
        }

        public void setSearchQuery(String value) {
            this.searchQuery = value;
        }
        public void setSearchType(int value) {
            this.searchType = value;
        }
    }
}


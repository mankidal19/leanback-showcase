/*
 * Copyright (c) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nurulaiman.sony.data;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.supportleanbackshowcase.models.Card;
import android.util.Log;

import java.util.List;


/**
 * Provides global search on the app's movie catalog. The assistant will query this provider for
 * results. <br>
 * Note: If you provide WatchAction feeds to Google, then you do not need this class. You should
 * still handle the playback intent and media controls in your fragment. This class enables <a
 * href="https://developer.android.com/training/tv/discovery/searchable.html">on-device search.</a>.
 */
public class VideoContentProvider extends ContentProvider {

    private static final String TAG = "VideoContentProvider";

    private static final String AUTHORITY = "nurulaiman.sony";

    private MockDatabase mDatabase;

    //for in-app search
    private static int idIndex;
    private static int nameIndex;
    private static int descIndex;
    private static int iconIndex;
    private static int dataTypeIndex;
    private static int videoWidthIndex;
    private static int videoHeightIndex;
    private static int isLiveIndex;
    private static int actionIndex;
    private static int suggestColumnIndex;



    // UriMatcher constant for search suggestions
    private static final int SEARCH_SUGGEST = 1;

    private UriMatcher mUriMatcher;

    private final String[] queryProjection =
            new String[] {
                BaseColumns._ID,
                MockDatabase.KEY_NAME,
                MockDatabase.KEY_DESCRIPTION,
                MockDatabase.KEY_ICON,
                MockDatabase.KEY_DATA_TYPE,
                MockDatabase.KEY_VIDEO_WIDTH,
                MockDatabase.KEY_VIDEO_HEIGHT,
                    MockDatabase.KEY_IS_LIVE,
                MockDatabase.KEY_ACTION,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
            };

    @Override
    public boolean onCreate() {
        mDatabase = new MockDatabase(getContext());
        mUriMatcher = buildUriMatcher();
        return true;
    }

    private UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(
                AUTHORITY, "/search/" + SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        uriMatcher.addURI(
                AUTHORITY,
                "/search/" + SearchManager.SUGGEST_URI_PATH_QUERY + "/*",
                SEARCH_SUGGEST);
        return uriMatcher;
    }

    @Nullable
    @Override
    public Cursor query(
            @NonNull Uri uri,
            @Nullable String[] projection,
            @Nullable String selection,
            @Nullable String[] selectionArgs,
            @Nullable String sortOrder) {

        Log.d(TAG, uri.toString());

        if (mUriMatcher.match(uri) == SEARCH_SUGGEST) {
            Log.d(TAG, "Search suggestions requested.");

            return search(uri.getLastPathSegment());

        } else {
            Log.d(TAG, "Unknown uri to query: " + uri);
            throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    private Cursor search(String query) {
        List<Card> results = mDatabase.search(query);

        MatrixCursor matrixCursor = new MatrixCursor(queryProjection);
        if (!results.isEmpty()) {
            for (Card movie : results) {
                matrixCursor.addRow(convertMovieIntoRow(movie));
            }
            Log.d(TAG, "match found ");
        }
        else {
            Log.d(TAG, "match not found ");
        }

        return matrixCursor;
    }

    private Object[] convertMovieIntoRow(Card card) {
        return new Object[] {
            card.getId(),
            card.getTitle(),
            card.getDescription(),
            card.getLocalImageResourceId(getContext()),
                "video/mp4",
            card.getWidth(),
            card.getHeight(),
                card.isLive(),
            "GLOBALSEARCH",
            card.getId()


        };
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        throw new UnsupportedOperationException("Insert is not implemented.");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Delete is not implemented.");
    }

    @Override
    public int update(
            @NonNull Uri uri,
            @Nullable ContentValues contentValues,
            @Nullable String s,
            @Nullable String[] strings) {
        throw new UnsupportedOperationException("Update is not implemented.");
    }

}

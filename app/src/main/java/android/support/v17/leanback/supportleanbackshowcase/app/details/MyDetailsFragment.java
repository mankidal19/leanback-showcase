package android.support.v17.leanback.supportleanbackshowcase.app.details;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.BaseOnItemViewClickedListener;
import android.support.v17.leanback.widget.BaseOnItemViewSelectedListener;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.widget.Toast;

public class MyDetailsFragment extends DetailsFragment  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //check internet connection
        if(!isInternetAvailable()){
            Toast.makeText(getActivity(), "Internet unavailable, couldn't load.", Toast.LENGTH_LONG)
                    .show();
            getActivity().finish();
        }

        super.onCreate(savedInstanceState);


    }


    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();

    }

}

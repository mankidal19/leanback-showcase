/*
* Created by Nurul Aiman, as an Open Source Project
* Documented on 04/01/2019
* Other interesting source code can be found at https://bitbucket.org/mankidal19/
*
*
* This AppUiUtils purpose is to customized details page background color
* based on content provider selected.
* Currently only FPTPlay & Hotstar will display different background color
* respectively, other providers will use default color
*/


package android.support.v17.leanback.supportleanbackshowcase.utils;

import android.content.Context;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.widget.RowPresenter;
import android.view.View;

import android.support.v17.leanback.supportleanbackshowcase.app.settings.MySettingsFragment;


public class AppUiUtils {

    private Context context;
    private String provider;
    private String interfaceMode;

    public AppUiUtils(Context context){
        this.context = context;
        provider = MySettingsFragment.getDefaults("pref_providers_key",context);
        interfaceMode = MySettingsFragment.getDefaults("pref_interface_key",context);
    }

    public void setDetailsPageBg(View actionsView, RowPresenter.ViewHolder viewHolder){
        if(provider.equals("fptplay")){
            actionsView.setBackgroundColor(context.getResources().
                    getColor(R.color.fptplay_detail_view_actionbar_background));

            View detailsView = viewHolder.view.findViewById(R.id.details_frame);
            detailsView.setBackgroundColor(
                    context.getResources().getColor(R.color.fptplay_detail_view_background));
        }

        else if(provider.equals("hotstar")){
            actionsView.setBackgroundColor(context.getResources().
                    getColor(R.color.hotstar_detail_view_actionbar_background));

            View detailsView = viewHolder.view.findViewById(R.id.details_frame);
            detailsView.setBackgroundColor(
                    context.getColor(R.color.hotstar_detail_view_background));
        }

        else{
            actionsView.setBackgroundColor(context.getResources().
                    getColor(R.color.detail_view_actionbar_background));

            View detailsView = viewHolder.view.findViewById(R.id.details_frame);
            detailsView.setBackgroundColor(
                    context.getColor(R.color.detail_view_background));
        }
    }


}

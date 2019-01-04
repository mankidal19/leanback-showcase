package android.support.v17.leanback.supportleanbackshowcase.cards.presenters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.models.IconHeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.PageRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.RowHeaderPresenter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class IconHeaderItemPresenter  extends RowHeaderPresenter {

    private static final String TAG = IconHeaderItemPresenter.class.getSimpleName();

    private float mUnselectedAlpha;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        mUnselectedAlpha = viewGroup.getResources()
                .getFraction(R.fraction.lb_browse_header_unselect_alpha, 1, 1);
        LayoutInflater inflater = (LayoutInflater) viewGroup.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.icon_header_item, null);
        view.setAlpha(mUnselectedAlpha);


        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {


                if(hasFocus){

                    view.setBackgroundColor(viewGroup.getResources().getColor(R.color.custom_header_select));

                    //view.getBackground().setVisible(true,false);

                }
                else {
                    //view.getBackground().setVisible(false,false);
                    //set bg to transparent
                    view.setBackgroundColor(Color.TRANSPARENT);

                }
                Log.d(TAG,"focus changed for view "+view.toString() + " to " + hasFocus);

            }

        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object o) {
        IconHeaderItem iconHeaderItem = null;
        if(o instanceof PageRow){
            iconHeaderItem = (IconHeaderItem) ((PageRow) o).getHeaderItem();
        }

        else{
            iconHeaderItem = (IconHeaderItem) ((ListRow) o).getHeaderItem();
        }

        View rootView = viewHolder.view;

        rootView.setFocusable(true);

        ImageView iconView = (ImageView) rootView.findViewById(R.id.header_icon);
        int iconResId = iconHeaderItem.getIconResId();
        if( iconResId != IconHeaderItem.ICON_NONE) { // Show icon only when it is set.
            Drawable icon = rootView.getResources().getDrawable(iconResId, null);
            iconView.setImageDrawable(icon);
        }

        TextView label = (TextView) rootView.findViewById(R.id.header_label);
        label.setText(iconHeaderItem.getName());

        //set content description for testing
        iconHeaderItem.setContentDescription(iconHeaderItem.getName());

        //change margin before displaying login header
        /*if(iconHeaderItem.getName().toLowerCase().equals("login/sign up")){
            FrameLayout.LayoutParams layoutParams =  new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,40,0,0);

            rootView.setLayoutParams(layoutParams);
        }*/
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        // no op
    }

    // TODO: TEMP - remove me when leanback onCreateViewHolder no longer sets the mUnselectAlpha,AND
    // also assumes the xml inflation will return a RowHeaderView
    @Override
    protected void onSelectLevelChanged(RowHeaderPresenter.ViewHolder holder) {
        // this is a temporary fix
        holder.view.setAlpha(mUnselectedAlpha + holder.getSelectLevel() *
                (1.0f - mUnselectedAlpha));
    }

}
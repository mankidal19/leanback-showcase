/*
 * Created by Nurul Aiman, as an Open Source Project
 * Documented on 04/01/2019
 * Other interesting source code can be found at https://bitbucket.org/mankidal19/
 *
 *
 */

package android.support.v17.leanback.supportleanbackshowcase.cards;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.app.settings.MySettingsFragment;
import android.support.v17.leanback.widget.BaseCardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class extends {@link BaseCardView} and is a customized version of {@link android.support.v17.leanback.widget.ImageCardView} class
 * provided by leanback library. This class is created to customized how {@link android.support.v17.leanback.widget.ImageCardView}
 * behaves and looks like.
 */
public class CustomImageCardView extends BaseCardView {

    private ImageView mImageView;
    private View mInfoArea;
    private TextView mTitleView;
    private TextView mContentView;
    private ImageView mBadgeImage;
    private ImageView mBadgeFadeMask;
    private String interfaceMode;


    public CustomImageCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.lb_custom_image_card_view, this);
        mImageView = (ImageView) v.findViewById(R.id.main_image);
        mImageView.setVisibility(View.INVISIBLE);
        mInfoArea = v.findViewById(R.id.info_field);
        mTitleView = (TextView) v.findViewById(R.id.title_text);
        mContentView = (TextView) v.findViewById(R.id.content_text);
        mBadgeImage = (ImageView) v.findViewById(R.id.extra_badge);
        mBadgeFadeMask = (ImageView) v.findViewById(R.id.fade_mask);


        interfaceMode = MySettingsFragment.getDefaults("pref_interface_key",getContext());


        //if interface mode is enduser, customized the card view
        if(interfaceMode.equals("enduser")){
            //default, hide info
            mInfoArea.setBackgroundColor(getResources().getColor(R.color.custom_info));


            //change color
            mTitleView.setTextColor(getResources().getColor(R.color.custom_title));
            mContentView.setTextColor(getResources().getColor(R.color.custom_description));

        }


        this.setFocusable(true);


    }

    public CustomImageCardView(Context context) {
        this(context, null);


        this.setFocusable(true);
    }

    public CustomImageCardView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.imageCardViewStyle);


        this.setFocusable(true);
    }

    public final ImageView getMainImageView() {
        return mImageView;
    }
    public void setMainImageAdjustViewBounds(boolean adjustViewBounds) {
        if (mImageView != null) {
            mImageView.setAdjustViewBounds(adjustViewBounds);
        }
    }
    public void setMainImageScaleType(ImageView.ScaleType scaleType) {
        if (mImageView != null) {
            mImageView.setScaleType(scaleType);
        }
    }
    public void setMainImage(Drawable drawable) {
        if (mImageView == null) {
            return;
        }
        mImageView.setImageDrawable(drawable);
        if (drawable == null) {
            mImageView.setVisibility(View.INVISIBLE);
        } else {
            mImageView.setVisibility(View.VISIBLE);
            fadeIn(mImageView);
        }
    }
    public void setMainImageDimensions(int width, int height) {
        ViewGroup.LayoutParams lp = mImageView.getLayoutParams();
        lp.width = width;
        lp.height = height;
        mImageView.setLayoutParams(lp);
    }
    public Drawable getMainImage() {
        if (mImageView == null) {
            return null;
        }
        return mImageView.getDrawable();
    }
    public void setTitleText(CharSequence text) {
        if (mTitleView == null) {
            return;
        }
        mTitleView.setText(text);
        setTextMaxLines();
    }
    public CharSequence getTitleText() {
        if (mTitleView == null) {
            return null;
        }
        return mTitleView.getText();
    }
    public void setContentText(CharSequence text) {
        if (mContentView == null) {
            return;
        }
        mContentView.setText(text);
        setTextMaxLines();
    }
    public CharSequence getContentText() {
        if (mContentView == null) {
            return null;
        }
        return mContentView.getText();
    }
    public void setBadgeImage(Drawable drawable) {
        if (mBadgeImage == null) {
            return;
        }
        if (drawable != null) {
            mBadgeImage.setImageDrawable(drawable);
            mBadgeImage.setVisibility(View.VISIBLE);
            mBadgeFadeMask.setVisibility(View.VISIBLE);
        } else {
            mBadgeImage.setVisibility(View.GONE);
            mBadgeFadeMask.setVisibility(View.GONE);
        }
    }
    public Drawable getBadgeImage() {
        if (mBadgeImage == null) {
            return null;
        }
        return mBadgeImage.getDrawable();
    }
    private void fadeIn(View v) {
        v.setAlpha(0f);
        v.animate().alpha(1f).setDuration(v.getContext().getResources().getInteger(
                android.R.integer.config_shortAnimTime)).start();
    }
    private void setTextMaxLines() {
        if (TextUtils.isEmpty(getTitleText())) {
            mContentView.setMaxLines(2);
        } else {
            mContentView.setMaxLines(1);
        }
        if (TextUtils.isEmpty(getContentText())) {
            mTitleView.setMaxLines(2);
        } else {
            mTitleView.setMaxLines(1);
        }
    }

    /**
     * To toggle between showing and hiding the card's info area.
     * Only usable when interface mode is set for end user.
     * @param show boolean to show the info or not
     */
    public void showInfo(boolean show){


        if(show && interfaceMode.equals("enduser")){
            mInfoArea.setVisibility(View.VISIBLE);
            showContent(true);

            mInfoArea.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.lb_basic_card_info_height);
            mInfoArea.requestLayout();
        }
        else{
            //mInfoArea.setVisibility(View.GONE);
            showContent(false);
            mTitleView.setVisibility(VISIBLE);

            //change height to wrap content
            mInfoArea.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mInfoArea.requestLayout();


        }
    }


    /**
     * Method to change info area color when current thumbnail is on focus
     * @param hasFocus boolean value is the card currently has focus or not
     */
    public void changeInfoAreaColor(boolean hasFocus){

        if(interfaceMode.equals("enduser")){

            if(hasFocus){
                mInfoArea.setBackgroundColor(getResources().getColor(R.color.custom_info_selected));
                mTitleView.setTextColor(Color.WHITE);
                mContentView.setTextColor(Color.WHITE);
            }

            else {
                mInfoArea.setBackgroundColor(getResources().getColor(R.color.custom_info));
                mTitleView.setTextColor(getResources().getColor(R.color.custom_title));
                mContentView.setTextColor(getResources().getColor(R.color.custom_description));

            }
        }

    }

    /**
     * Method to show card's content or not. Is called by method showInfo.
     * @param show boolean to show the info or not
     */
    public void showContent(boolean show){


        if(show){
            mContentView.setVisibility(VISIBLE);

        }
        else{
            mContentView.setVisibility(GONE);
        }
    }



}

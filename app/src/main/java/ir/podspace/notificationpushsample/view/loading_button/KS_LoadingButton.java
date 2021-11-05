package ir.podspace.notificationpushsample.view.loading_button;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import ir.podspace.notificationpushsample.R;


public class KS_LoadingButton extends RelativeLayout {
    public String fontsPath = "fonts/";

    private TextView mTextView;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    private String mText;
    private int mTextColor;
    private Typeface mTypeface;
    private float mTextSize;
    private int mProgressColor;
    private int mProgressPadding;
    private int mImagePadding;
    private boolean enable = true;
    private Integer drawable;

    private boolean isLoading = false;


    public KS_LoadingButton(Context context) {
        super(context);

        if (isInEditMode()) return;

        setAttributes(context, null);
        init();
    }

    public KS_LoadingButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) return;

        setAttributes(context, attrs);
        init();
    }

    public KS_LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (isInEditMode()) return;

        setAttributes(context, attrs);
        init();
    }

    private void init() {
        setClickable(true);
        LayoutParams textViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        LayoutParams progressBarParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        progressBarParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        mTextView = new TextView(getContext());
        mTextView.setLayoutParams(textViewParams);
        mTextView.setText(mText);
        mTextView.setTextColor(mTextColor);
        mTextView.setTypeface(mTypeface);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

        mProgressBar = new ProgressBar(getContext());
        mProgressBar.setLayoutParams(progressBarParams);
        mProgressBar.setVisibility(View.INVISIBLE);
        mProgressBar.getIndeterminateDrawable().setColorFilter(mProgressColor, PorterDuff.Mode.SRC_IN);
        mProgressBar.setPadding(mProgressPadding, mProgressPadding, mProgressPadding, mProgressPadding);

        mImageView = new ImageView(getContext());
        mImageView.setVisibility(INVISIBLE);
        mImageView.setLayoutParams(progressBarParams);
        mImageView.setPadding(mImagePadding, mImagePadding, mImagePadding, mImagePadding);
        if (drawable != 0) {
            mImageView.setImageResource(drawable);
            mImageView.setVisibility(VISIBLE);
        }

        addView(mTextView);
        addView(mProgressBar);
        addView(mImageView);

        setElevation(5f);
        setEnabled(enable);
    }


    private void setAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.KS_LoadingButton, 0, 0);
        try {
            mText = ta.getString(R.styleable.KS_LoadingButton_lb_text);
            mTextColor = ta.getColor(R.styleable.KS_LoadingButton_lb_text_color, getResources().getColor(R.color.black));
            mProgressColor = ta.getColor(R.styleable.KS_LoadingButton_lb_progress_color, getResources().getColor(R.color.white));
            mTextSize = ta.getDimensionPixelSize(R.styleable.KS_LoadingButton_lb_text_size, 0);
            mProgressPadding = ta.getDimensionPixelSize(R.styleable.KS_LoadingButton_lb_progress_padding, 0);
            mImagePadding = ta.getDimensionPixelSize(R.styleable.KS_LoadingButton_lb_image_padding, 0);
            drawable = ta.getResourceId(R.styleable.KS_LoadingButton_lb_image,0);

            int fontName = ta.getResourceId(R.styleable.KS_LoadingButton_ks_fontFamily, R.font.iransans);

            setFont(context, fontName);
        } finally {
            ta.recycle();
        }
    }

    public void setFont(Context context, int fontNameEnum) {
        mTypeface = ResourcesCompat.getFont(context, fontNameEnum);
    }


    public void startLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.INVISIBLE);
        mImageView.setVisibility(View.INVISIBLE);
        setEnabled(false);
        isLoading = true;
    }

    public void stopLoading() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.VISIBLE);
        setEnabled(true);
        isLoading = false;
    }

    public void setmText(String mText) {
        mTextView.setText(mText);
    }

    public String getmText() {
        return mText;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        setEnabled(enable);
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
package com.sns.repost.helpers.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by nguyenvanhien on 6/26/17.
 */

public class AspectRatioImageView extends ImageView {
    public AspectRatioImageView(Context context) {
        super(context);
    }

    public AspectRatioImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectRatioImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(getDrawable()!=null) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}

package com.questions.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.questions.R;
import com.questions.utils.MyUtils;

import com.questions.convenientbanner.holder.Holder;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by 11470 on 2017/10/18.
 */

public class BannerAdapter implements Holder<String>{

    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        Glide.with(context).load(data).bitmapTransform(new RoundedCornersTransformation(context,
                MyUtils.getInstance().dp2px(context, 10), 1)).placeholder(R.mipmap.defult_img)
                .fallback(R.mipmap.defult_img).error(R.mipmap.defult_img)
                .into(imageView);
    }
}

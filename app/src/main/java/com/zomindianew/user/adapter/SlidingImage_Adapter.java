package com.zomindianew.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zomindianew.R;
import com.zomindianew.bean.SliderModel;
import com.zomindianew.user.activity.SubCategoryActivity;

import java.util.ArrayList;

public class SlidingImage_Adapter extends PagerAdapter {


    private ArrayList<SliderModel> IMAGES;
    private LayoutInflater inflater;
    private Context context;

    public SlidingImage_Adapter(Context context,ArrayList<SliderModel> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.slider_banner_lay, view, false);
//        SliderModel  =IMAGES.get(position);
        assert imageLayout != null;
          ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        Log.e("bsnner adapert ","jjjkjk bsnner"+IMAGES.get(position).getSlider_img());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_login = new Intent(context, SubCategoryActivity.class);
                intent_login.putExtra("categoryName", IMAGES.get(position).getCategory_name());
                intent_login.putExtra("id", IMAGES.get(position).getSlider_id());
                context.startActivity(intent_login);
//

            }
        });

//        imageView.setImageResource(IMAGES.get(position));
//
//        Glide.with(context).load(IMAGES.get(position).getSlider_img())
//                .into(imageView);



        Picasso.with(context).load(IMAGES.get(position).getSlider_img()).into(imageView);
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}

package com.zomindianew.user.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zomindianew.R;
import com.zomindianew.bean.SubCategoryBean;

import java.util.ArrayList;


public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    private ItemInterFace itemClick;
    private Context context;
    private ArrayList<SubCategoryBean> subCategoryBeanArrayList;

//    int[] iconArray = {R.mipmap.home_appliances, R.mipmap.carpenter, R.mipmap.plumber, R.mipmap.beautiparlur, R.mipmap.cleaning, R.mipmap.carwash};
//    String Names[] = {"Home Appliances", "Carpenter", "Plumber", "Beautiparlur", "Cleaning", "Carwash"};

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image,imageinfo,arrowright;
        TextView name,rating,information, less;
        LinearLayout subcategoryRelative;
        RelativeLayout relative_arrow;
        RatingBar ratingBar;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            imageinfo = view.findViewById(R.id.img_info);
            name = view.findViewById(R.id.name);
            less = view.findViewById(R.id.less);
            information = view.findViewById(R.id.information);
            ratingBar = view.findViewById(R.id.ratingBar);
            arrowright = view.findViewById(R.id.arrowright);
            subcategoryRelative = view.findViewById(R.id.subcategoryRelative);
            relative_arrow = view.findViewById(R.id.relative_arrow);
            arrowright.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClick != null) {
                itemClick.onItemClick(v, getAdapterPosition());
         //     Toast.makeText(context,"onclick",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onItemClickMethod(ItemInterFace itemClick) {

        this.itemClick = itemClick;

    }

    public interface ItemInterFace {
        void onItemClick(View view, int position);
    }

    public SubCategoryAdapter(Context context, ArrayList<SubCategoryBean> subCategoryBeanArrayList) {

        this.context = context;
        this.subCategoryBeanArrayList = subCategoryBeanArrayList;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.sub_category_adpter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final SubCategoryBean subCategoryBean = subCategoryBeanArrayList.get(position);
        holder.name.setText(subCategoryBeanArrayList.get(position).getCategory_name());
        holder.ratingBar.setRating(Float.parseFloat(subCategoryBean.getRating_point()));
        holder.information.setText(subCategoryBeanArrayList.get(position).getInfo_desc());


        Glide.with(context)
                .load(subCategoryBeanArrayList.get(position).getCategory_icon())
               // .apply(RequestOptions.circleCropTransform())
                .into(holder.image);

        holder.imageinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.information.setVisibility(View.VISIBLE);
                holder.less.setVisibility(View.VISIBLE);

            }
        });
        holder.less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.information.setVisibility(View.GONE);
                holder.less.setVisibility(View.GONE);

            }
        });



    }
    @Override
    public int getItemCount() {
        return subCategoryBeanArrayList.size();

    }
}
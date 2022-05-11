package com.zomindianew.user.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zomindianew.R;
import com.zomindianew.bean.CategoryBean;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private ItemInterFace itemClick;
    private Context context;
    private ArrayList<CategoryBean> categoryBeanArrayList;

//    int[] iconArray = {R.mipmap.home_appliances, R.mipmap.carpenter, R.mipmap.plumber, R.mipmap.beautiparlur, R.mipmap.cleaning, R.mipmap.carwash};
//    String Names[] = {"Home Appliances", "Carpenter", "Plumber", "Beautiparlur", "Cleaning", "Carwash"};

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name;
        CardView cardView;


        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name);
            cardView = view.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClick != null) {
                itemClick.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public void onItemClickMethod(ItemInterFace itemClick) {

        this.itemClick = itemClick;


    }

    public interface ItemInterFace {
        void onItemClick(View view, int position);
    }

    public CategoryAdapter(Context context, ArrayList<CategoryBean> categoryBeanArrayList) {

        this.context = context;
        this.categoryBeanArrayList = categoryBeanArrayList;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.category_adpter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.name.setText(categoryBeanArrayList.get(position).getCategory_name());

        Glide.with(context)
                .load(categoryBeanArrayList.get(position).getCategory_icon())
                //.apply(RequestOptions.circleCropTransform())
                .into(holder.image);


    }


    @Override
    public int getItemCount() {
        return categoryBeanArrayList.size();

    }
}
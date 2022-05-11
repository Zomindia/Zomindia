package com.zomindianew.user.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zomindianew.R;
import com.zomindianew.bean.ServiceSubCategoryBean;

import java.util.ArrayList;


public class ServiceCategoryAdapter extends RecyclerView.Adapter<ServiceCategoryAdapter.MyViewHolder> {

    private ItemInterFace itemClick;
    private Context context;
    private ArrayList<ServiceSubCategoryBean> subCategoryBeanArrayList;

//    int[] iconArray = {R.mipmap.home_appliances, R.mipmap.carpenter, R.mipmap.plumber, R.mipmap.beautiparlur, R.mipmap.cleaning, R.mipmap.carwash};
//    String Names[] = {"Home Appliances", "Carpenter", "Plumber", "Beautiparlur", "Cleaning", "Carwash"};

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name;
        RelativeLayout subcategoryRelative;
        TextView priceTextView;
        ImageView addItem;
        TextView quntityTextView;
        ImageView removeItem;
        private ImageView checkbox;


        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            priceTextView = view.findViewById(R.id.priceTextView);
            name = view.findViewById(R.id.name);
            checkbox = view.findViewById(R.id.checkbox);

            addItem = view.findViewById(R.id.addItem);
            quntityTextView = view.findViewById(R.id.quntityTextView);
            removeItem = view.findViewById(R.id.removeItem);

            subcategoryRelative = view.findViewById(R.id.subcategoryRelative);
            subcategoryRelative.setOnClickListener(this);
            addItem.setOnClickListener(this);
            removeItem.setOnClickListener(this);
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

    public ServiceCategoryAdapter(Context context, ArrayList<ServiceSubCategoryBean> subCategoryBeanArrayList) {

        this.context = context;
        this.subCategoryBeanArrayList = subCategoryBeanArrayList;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.service_type_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (subCategoryBeanArrayList.get(position).isSlected()) {
            holder.checkbox.setImageResource(R.mipmap.right);
        } else {
            holder.checkbox.setImageResource(R.mipmap.right_unselect);
        }
        holder.name.setText(subCategoryBeanArrayList.get(position).getName());
        holder.priceTextView.setText(subCategoryBeanArrayList.get(position).getAmount() + " ₹");
        holder.quntityTextView.setText(subCategoryBeanArrayList.get(position).getQuntity());
    }


    @Override
    public int getItemCount() {
        return subCategoryBeanArrayList.size();

    }
}
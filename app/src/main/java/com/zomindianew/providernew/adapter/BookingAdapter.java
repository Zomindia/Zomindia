package com.zomindianew.providernew.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zomindianew.R;
import com.zomindianew.bean.AppointmentBean;
import com.zomindianew.helper.Constants;

import java.util.ArrayList;


public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {

    private ItemInterFace itemClick;
    private Context context;
    ArrayList<AppointmentBean> appointmentBeanArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name;
        TextView dateTextView;
        TextView categoryTextView;
        TextView addressText;
        TextView time;
        TextView acceptButton;
        TextView declineButton;
        CardView cardView;
        TextView subCategoryTV;
        TextView categoryTV;
        TextView subCategoryType;


        public MyViewHolder(View view) {
            super(view);
            subCategoryTV = view.findViewById(R.id.subCategoryTV);
            categoryTV = view.findViewById(R.id.categoryTV);
            subCategoryType = view.findViewById(R.id.subCategoryType);
            acceptButton = view.findViewById(R.id.acceptButton);
            declineButton = view.findViewById(R.id.declineButton);
            image = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name);
            dateTextView = view.findViewById(R.id.dateTextView);
            categoryTextView = view.findViewById(R.id.categoryTextView);
            addressText = view.findViewById(R.id.addressText);
            time = view.findViewById(R.id.time);
            cardView = view.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
            acceptButton.setOnClickListener(this);
            declineButton.setOnClickListener(this);
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

    public BookingAdapter(Context context, ArrayList<AppointmentBean> appointmentBeanArrayList) {
        this.appointmentBeanArrayList = appointmentBeanArrayList;
        this.context = context;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.booking_adpter, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.categoryTV.setText("Category: " + appointmentBeanArrayList.get(position).getCategory());

        holder.subCategoryTV.setText("Sub Category: " + appointmentBeanArrayList.get(position).getSubCategory());
        holder.subCategoryType.setText("Sub Category Type: " + appointmentBeanArrayList.get(position).getSubCategoryType());
        holder.name.setText(appointmentBeanArrayList.get(position).getName());
        holder.categoryTextView.setText("#" + appointmentBeanArrayList.get(position).getReference_id());
        holder.dateTextView.setText(appointmentBeanArrayList.get(position).getDate());
        holder.time.setText(Constants.getDateInFormat("HH:mm:ss", "hh:mm aa", appointmentBeanArrayList.get(position).getTime()));
        holder.addressText.setText(appointmentBeanArrayList.get(position).getAddress());

//        holder.name.setText(Names[position]);
//
        Glide.with(context)
                .load(appointmentBeanArrayList.get(position).getUser_profile())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.image);


    }


    @Override
    public int getItemCount() {
        return appointmentBeanArrayList.size();

    }
}
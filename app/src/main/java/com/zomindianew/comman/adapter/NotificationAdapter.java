package com.zomindianew.comman.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zomindianew.R;
import com.zomindianew.bean.NotificationBean;
import com.zomindianew.helper.Constants;
import com.zomindianew.providernew.activity.BookingDetailActivity;

import java.util.ArrayList;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private ItemInterFace itemClick;
    private Context context;
    private ArrayList<NotificationBean> notificationList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView userImageView;
        TextView timeAgo;
        TextView nameTextView;
        TextView message;
        TextView categoryTV;
        TextView subcategoryTV;
        RelativeLayout rl_inner_lay;


        public MyViewHolder(View view) {
            super(view);
            categoryTV = view.findViewById(R.id.categoryTV);
            subcategoryTV = view.findViewById(R.id.subcategoryTV);
            userImageView = view.findViewById(R.id.userImageView);
            timeAgo = view.findViewById(R.id.timeAgo);
            nameTextView = view.findViewById(R.id.nameTextView);
            message = view.findViewById(R.id.message);
            rl_inner_lay = view.findViewById(R.id.rl_inner_lay);

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

    public NotificationAdapter(Context context, ArrayList<NotificationBean> notificationList) {

        this.context = context;
        this.notificationList = notificationList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.on_notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.nameTextView.setText(notificationList.get(position).getFromName());
        holder.message.setText(notificationList.get(position).getMessage());
        holder.timeAgo.setText(Constants.timeAgo(notificationList.get(position).getCreated_at(), context));

        if (notificationList.get(position).getType().equals("request_booked")) {
            holder.categoryTV.setVisibility(View.VISIBLE);
            holder.subcategoryTV.setVisibility(View.VISIBLE);
            /*holder.categoryTV.setText("Category: " + notificationList.get(position).getCategory());*/
            holder.subcategoryTV.setText("your service booked for- " + notificationList.get(position).getSubcategory());

        } else {
            holder.categoryTV.setVisibility(View.GONE);
            holder.subcategoryTV.setVisibility(View.GONE);

        }

        Glide.with(context)
                .load(notificationList.get(position).getFromImage())
                .placeholder(R.drawable.defult_user).dontAnimate()
                .apply(RequestOptions.circleCropTransform())
                .into(holder.userImageView);
        holder.rl_inner_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemClick != null) {
                    itemClick.onItemClick(view, position);
                }
/*
                Intent intent = new Intent(context, BookingDetailActivity.class);
                intent.putExtra("come_from", "upcoming");
                intent.putExtra("bookingID", notificationList.get(position).getBooking_id());
                context.startActivity(intent);
*/

            }
        });
    }


    @Override
    public int getItemCount() {
        return notificationList.size();

    }
}
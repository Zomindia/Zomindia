package com.zomindianew.providernew.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zomindianew.R;
import com.zomindianew.bean.AppointmentBean;
import com.zomindianew.helper.Constants;

import java.util.ArrayList;


public class PastAdapter extends RecyclerView.Adapter<PastAdapter.MyViewHolder> {

    private ItemInterFace itemClick;
    private Context context;
    ArrayList<AppointmentBean> appointmentBeanArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView userImageView;
        TextView userName;

        TextView appointmentID;
        TextView dateTime;
        TextView amountTextView;
        TextView statusText;
        CardView cardView;
        RelativeLayout onGoingRelative;


        public MyViewHolder(View view) {
            super(view);
            userImageView = view.findViewById(R.id.userImageView);
            userName = view.findViewById(R.id.userName);
            appointmentID = view.findViewById(R.id.appointmentID);
            dateTime = view.findViewById(R.id.dateTime);
            amountTextView = view.findViewById(R.id.amountTextView);
            statusText = view.findViewById(R.id.statusText);
            onGoingRelative = view.findViewById(R.id.onGoingRelative);
            cardView = view.findViewById(R.id.cardView);
            onGoingRelative.setOnClickListener(this);
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

    public PastAdapter(Context context, ArrayList<AppointmentBean> appointmentBeanArrayList) {

        this.context = context;
        this.appointmentBeanArrayList = appointmentBeanArrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.on_going_adpter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.amountTextView.setText("" + appointmentBeanArrayList.get(position).getAmount() + " ₹");

        holder.userName.setText(appointmentBeanArrayList.get(position).getName());
        holder.appointmentID.setText("#" + appointmentBeanArrayList.get(position).getReference_id());
        holder.statusText.setText(appointmentBeanArrayList.get(position).getStatus());
        holder.dateTime.setText(appointmentBeanArrayList.get(position).getDate() + ", " + Constants.getDateInFormat("HH:mm:ss", "hh:mm aa", appointmentBeanArrayList.get(position).getTime()));


        Glide.with(context)
                .load(appointmentBeanArrayList.get(position).getUser_profile())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.userImageView);

    }


    @Override
    public int getItemCount() {
        return appointmentBeanArrayList.size();

    }
}
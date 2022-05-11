package com.zomindianew.providernew.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zomindianew.R;
import com.zomindianew.bean.AppointmentBean;
import com.zomindianew.helper.Constants;

import java.util.ArrayList;


public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.MyViewHolder> {

    private ItemInterFace itemClick;
    private Context context;
    ArrayList<AppointmentBean> appointmentBeanArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView tvUserImage;
        TextView tvService;

        TextView tvAppointmentId;
        TextView tvServiceDate;
        TextView tvBookDate;
        TextView tvAmount;
        TextView tvStatus;
        LinearLayout llBookingUser;


        public MyViewHolder(View view) {
            super(view);
            tvUserImage = view.findViewById(R.id.ivBookingUser);
            tvService = view.findViewById(R.id.tvBookingService);
            tvBookDate = view.findViewById(R.id.tvBookingBookDate);
            tvServiceDate = view.findViewById(R.id.tvBookingServiceDate);

            tvAppointmentId = view.findViewById(R.id.tvBookingAppointmentId);
            tvAmount = view.findViewById(R.id.tvBookingAmount);
            tvStatus = view.findViewById(R.id.tvBookingStatus);

            llBookingUser = view.findViewById(R.id.llBookingUser);
            llBookingUser.setOnClickListener(this);
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

    public UpcomingAdapter(Context context, ArrayList<AppointmentBean> appointmentBeanArrayList) {

        this.context = context;
        this.appointmentBeanArrayList = appointmentBeanArrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_booking_user, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        AppointmentBean pojo = appointmentBeanArrayList.get(position);
        holder.tvService.setText(pojo.getSubCategoryType()+" | "+pojo.getSubCategory()+" | "+pojo.getCategory());
        holder.tvBookDate.setText("Booking: "+Constants.convertDateTime(pojo.getCreated_at()));
        holder.tvServiceDate.setText("Service: "+Constants.convertDateTime(pojo.getDate() + " " + pojo.getTime()));

        holder.tvAppointmentId.setText("#" + appointmentBeanArrayList.get(position).getReference_id());
        holder.tvAmount.setText("" + appointmentBeanArrayList.get(position).getAmount() + " ₹");
        holder.tvStatus.setText(appointmentBeanArrayList.get(position).getStatus());

        Glide.with(context)
                .load(appointmentBeanArrayList.get(position).getUser_profile())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.tvUserImage);


    }


    @Override
    public int getItemCount() {
        return appointmentBeanArrayList.size();

    }
}
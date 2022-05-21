package com.zomindianew.user.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zomindianew.R;
import com.zomindianew.bean.SelectSubAdapterBean;
import com.zomindianew.bean.ServiceSubCategoryBean;
import com.zomindianew.user.activity.ConfirmBookingScreen;
import com.zomindianew.user.activity.PdfShow;
import com.zomindianew.user.activity.SelectSubActivity;
import com.zomindianew.user.activity.SubCategoryActivity;

import java.util.ArrayList;
import java.util.List;

public class SelectSubAdapter extends RecyclerView.Adapter<SelectSubAdapter.MyViewHolder> {
    Context context;
    SelectSubAdapterBean selectSubAdapterBean;
    List<ServiceSubCategoryBean> item_datalist = new ArrayList<>();

    private Typeface myFont;

    public SelectSubAdapter(Context context, List<ServiceSubCategoryBean> item_datalist) {
        this.context = context;
        this.item_datalist = item_datalist;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selectsub_adapter_layout, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ServiceSubCategoryBean serviceSubCategoryBean = item_datalist.get(position);

//        myFont = Typeface.createFromAsset(getAssets(), "gujarti.ttf");
//
        holder.name.setText(serviceSubCategoryBean.getName());
        holder.ratingBar.setRating(Float.parseFloat(serviceSubCategoryBean.getRetingpoint()));
        holder.info.setText(serviceSubCategoryBean.getInfodsc());
        holder.quntity.setText(serviceSubCategoryBean.getQuntity());
        holder.price.setText("₹" + serviceSubCategoryBean.getAmount());
        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ConfirmBookingScreen.class);
                intent.putExtra("categoryID", item_datalist.get(position).getCid());
                intent.putExtra("subCategoryID", item_datalist.get(position).getSid());
                intent.putExtra("subCategoryName", item_datalist.get(position).getsName());
                intent.putExtra("categoryName", item_datalist.get(position).getCategoryname());
                intent.putExtra("amount", item_datalist.get(position).getAmount());
                intent.putExtra("serviceID", item_datalist.get(position).getId());
                intent.putExtra("count", holder.quntity.getText().toString());
                intent.putExtra("subSubCategory", item_datalist.get(position).getName());
                context.startActivity(intent);

            }
        });


        holder.relative_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showInfoDialog(position,holder.quntity.getText().toString());
               /* holder.info.setVisibility(View.VISIBLE);
                holder.lesee_slct.setVisibility(View.VISIBLE);
                holder.pdf_view.setVisibility(View.VISIBLE);
                holder.linear_viewprice_less.setVisibility(View.VISIBLE);
*/
            }
        });
        holder.linear_viewprice_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.info.setVisibility(View.GONE);
                holder.lesee_slct.setVisibility(View.GONE);
                holder.pdf_view.setVisibility(View.GONE);
                holder.linear_viewprice_less.setVisibility(View.GONE);
            }
        });

//        holder.lesee_slct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.info.setVisibility(View.GONE);
//                holder.lesee_slct.setVisibility(View.GONE);
//                holder.pdf_view.setVisibility(View.GONE);
//                holder.linear_viewprice_less.setVisibility(View.GONE);
//            }
//        });

        holder.rightarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ConfirmBookingScreen.class);
                intent.putExtra("categoryID", item_datalist.get(position).getCid());
                intent.putExtra("subCategoryID", item_datalist.get(position).getSid());
                intent.putExtra("subCategoryName", item_datalist.get(position).getsName());
                intent.putExtra("categoryName", item_datalist.get(position).getCategoryname());
                intent.putExtra("amount", item_datalist.get(position).getAmount());
                intent.putExtra("serviceID", item_datalist.get(position).getId());
                intent.putExtra("count", holder.quntity.getText().toString());
                intent.putExtra("subSubCategory", item_datalist.get(position).getName());
                context.startActivity(intent);
            }
        });

        holder.pdf_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, PdfShow.class);
                intent.putExtra("url", serviceSubCategoryBean.getRate_list());
                intent.putExtra("title", serviceSubCategoryBean.getName());
                context.startActivity(intent);

            }
        });

        Picasso.with(context).load(item_datalist.get(position).getCategory_pic()).into(holder.infimgsub);


    }


    @Override
    public int getItemCount() {
        return item_datalist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quntity, info, lesee_slct, pdf_view;
        ImageView imageViewminus, imageViewplus, infoimg, infimgsub, rightarrow;
        RatingBar ratingBar;
        LinearLayout linear_viewprice_less, llParent;
        RelativeLayout relative_1;

        public MyViewHolder(View itemView) {
            super(itemView);

//            imageView = (ImageView) itemView.findViewById(R.id.img_noti);
//            textViewdate = (TextView) itemView.findViewById(R.id.noti_date);
            name = (TextView) itemView.findViewById(R.id.prd_name);
            price = (TextView) itemView.findViewById(R.id.prd_price);
            infoimg = (ImageView) itemView.findViewById(R.id.set_img_info);
            infimgsub = (ImageView) itemView.findViewById(R.id.image_sub);
            info = (TextView) itemView.findViewById(R.id.sct_information);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            lesee_slct = (TextView) itemView.findViewById(R.id.less_selct);
            pdf_view = (TextView) itemView.findViewById(R.id.pdf_view);
            llParent = (LinearLayout) itemView.findViewById(R.id.llParent);
            linear_viewprice_less = (LinearLayout) itemView.findViewById(R.id.linear_viewprice_less);
            quntity = (TextView) itemView.findViewById(R.id.prd_value);
            rightarrow = (ImageView) itemView.findViewById(R.id.arrowright);
            relative_1 = (RelativeLayout) itemView.findViewById(R.id.relative_1);
//            imageViewminus = (ImageView) itemView.findViewById(R.id.prd_minus);
//            imageViewplus = (ImageView) itemView.findViewById(R.id.prd_plus);

        }
    }


    private void showInfoDialog(final int position, final String count) {

        try {


            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_infol);
            TextView category_naame = dialog.findViewById(R.id.category_naame);
            TextView tv_info_detail = dialog.findViewById(R.id.tv_info_detail);
            TextView select_service = dialog.findViewById(R.id.select_service);
            TextView cancel = dialog.findViewById(R.id.cancel);
            TextView view = dialog.findViewById(R.id.view);
if (item_datalist.get(position).getInfodsc().equalsIgnoreCase(""))
{
    tv_info_detail.setVisibility(View.GONE);
}

            tv_info_detail.setText(item_datalist.get(position).getInfodsc());
            category_naame.setText(item_datalist.get(position).getName() + " info");
            view.setVisibility(View.VISIBLE);

            select_service.setText("Book Service");
            view.setText("View Pricing");


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PdfShow.class);
                    intent.putExtra("url", item_datalist.get(position).getRate_list());
                    intent.putExtra("title", item_datalist.get(position).getName());
                    context.startActivity(intent);
                    dialog.cancel();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();

                }
            });
            select_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ConfirmBookingScreen.class);
                    intent.putExtra("categoryID", item_datalist.get(position).getCid());
                    intent.putExtra("subCategoryID", item_datalist.get(position).getSid());
                    intent.putExtra("subCategoryName", item_datalist.get(position).getsName());
                    intent.putExtra("categoryName", item_datalist.get(position).getCategoryname());
                    intent.putExtra("amount", item_datalist.get(position).getAmount());
                    intent.putExtra("serviceID", item_datalist.get(position).getId());
                    intent.putExtra("count", count);
                    intent.putExtra("subSubCategory", item_datalist.get(position).getName());
                    context.startActivity(intent);
                    dialog.cancel();

                }
            });

            dialog.show();
        }
        catch (Exception e)
        {
            Log.e("error",e.toString());
        }

    }
}